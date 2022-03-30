/**
 * GatePhrasesExtractor.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 21/06/2007
 */
package com.demo.app.jcolibri.extensions.textual.IE.gate;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Factory;
import gate.creole.ExecutionException;
import gate.creole.gazetteer.DefaultGazetteer;

import java.util.Collection;
import java.util.Iterator;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.extensions.textual.IE.IEutils;
import jcolibri.extensions.textual.IE.representation.IEText;
import jcolibri.extensions.textual.IE.representation.Token;
import jcolibri.extensions.textual.IE.representation.info.PhraseInfo;
import jcolibri.util.AttributeUtils;
import jcolibri.util.ProgressController;

/**
 * Phrases extractor based on the Gate Gazetteer. 
 * It is compatible with the generic PhrasesExtractor so they can be executed together.
 * GATE's default rules file or any other file can be loaded.
 * <br>
 * For more information see the GATE tutorial.
 * @author Juan A. Recio-Garcia
 * @version 1.0
 *
 */
public class GatePhrasesExtractor
{
    private static DefaultGazetteer gaze = null;
    
    /**
     * Performs the algorithm in the given attributes of a collection of cases.
     * These attributes must be IETextGate objects.
     */    
    public static void extractPhrases(Collection<CBRCase> cases, Collection<Attribute> attributes)
    {
	org.apache.commons.logging.LogFactory.getLog(GatePhrasesExtractor.class).info("Extracting phrases.");
	ProgressController.init(GatePhrasesExtractor.class, "Extracting phrases", cases.size());
	for(CBRCase c: cases)
	{
	    for(Attribute a: attributes)
	    {
		Object o = AttributeUtils.findValue(a, c);
		if(o instanceof IETextGate)
		    extractPhrases((IETextGate)o);
	    }
	    ProgressController.step(GatePhrasesExtractor.class);
	}
	ProgressController.finish(GatePhrasesExtractor.class);
    }

    /**
     * Performs the algorithm in the given attributes of a query.
     * These attributes must be IETextGate objects.
     */
    public static void extractPhrases(CBRQuery query, Collection<Attribute> attributes)
    {
	    org.apache.commons.logging.LogFactory.getLog(GatePhrasesExtractor.class).info("Extracting phrases.");
	    for(Attribute a: attributes)
	    {
		Object o = AttributeUtils.findValue(a, query);
		if(o instanceof IETextGate)
		    extractPhrases((IETextGate)o);
	    }
    }
    
    /**
     * Performs the algorithm in all the IETextGate typed attributes of a collection of cases.
     */ 
    public static void extractPhrases(Collection<CBRCase> cases)
    {
	org.apache.commons.logging.LogFactory.getLog(GatePhrasesExtractor.class).info("Extracting phrases.");
	ProgressController.init(GatePhrasesExtractor.class, "Extracting phrases", cases.size());
	for(CBRCase c: cases)
	{
	    Collection<IEText> texts = IEutils.getTexts(c);
	    for(IEText t : texts)
		if(t instanceof IETextGate)
		    extractPhrases((IETextGate)t);
	    ProgressController.step(GatePhrasesExtractor.class);
	}
	ProgressController.finish(GatePhrasesExtractor.class);
    }
    
    /**
     * Performs the algorithm in all the IETextGate typed attributes of a query.
     */
    public static void extractPhrases(CBRQuery query)
    {	    
	org.apache.commons.logging.LogFactory.getLog(GatePhrasesExtractor.class).info("Extracting phrases.");
	Collection<IEText> texts = IEutils.getTexts(query);
        for(IEText t : texts)
            if(t instanceof IETextGate)
        	extractPhrases((IETextGate)t);
    }
    
    /**
     * Performs the algorithm in a given IETextGate object
     */
    public static void extractPhrases(IETextGate text)
    {
	try
	{
	    gaze.setDocument(text.getDocument());
	    gaze.execute();
	    
	    AnnotationSet lookupAnnotations = text.getDocument().getAnnotations().get("Lookup");
	    
	    for(Token t: text.getAllTokens())
	    {
		Annotation anotToken = text.getTokenMapping(t);
		AnnotationSet lookupAnnots = lookupAnnotations.get(anotToken.getStartNode().getOffset(), anotToken.getEndNode().getOffset());
		for(Iterator iter = lookupAnnots.iterator(); iter.hasNext(); )
		{
		    Annotation anot = (Annotation)iter.next();
		    String Type = (String)anot.getFeatures().get("majorType");
		    String minorType = (String)anot.getFeatures().get("minorType");
		    if(minorType!= null)
			Type = Type+"."+minorType;
		    text.addPhrase(new PhraseInfo(Type, 
			    		anot.getStartNode().getOffset().intValue(), 
			    		anot.getEndNode().getOffset().intValue()));
		}
		
	    }
	    
	    //System.err.print(text.getDocument());
	    
	} catch (ExecutionException e)
	{
	    org.apache.commons.logging.LogFactory.getLog(GatePhrasesExtractor.class).error(e);
	}
    }
    
    private static String defaultRulesFileName = "jcolibri/extensions/textual/IE/gate/gateinit/plugins/ANNIE/resources/gazetteer/lists.def";
    
    public static void loadDefaultRules()
    {
	loadRules(defaultRulesFileName);
    }
    
    /**
     * Loads a rules file
     */
     public static void loadRules(String filename)
     {
	 try
	{
	    gaze = (DefaultGazetteer) Factory
		.createResource("gate.creole.gazetteer.DefaultGazetteer");
	    gaze.setListsURL(jcolibri.util.FileIO.findFile(filename));
	    gaze.init();
	} catch (Exception e)
	{
	    org.apache.commons.logging.LogFactory.getLog(GatePhrasesExtractor.class).error(e);
	    
	}
	 
     }
     

}
