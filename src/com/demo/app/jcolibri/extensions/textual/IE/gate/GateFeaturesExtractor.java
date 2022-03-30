/**
 * GateFeaturesExtractor.java
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
import gate.creole.Transducer;

import java.util.Collection;
import java.util.Iterator;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.extensions.textual.IE.IEutils;
import jcolibri.extensions.textual.IE.representation.IEText;
import jcolibri.extensions.textual.IE.representation.info.FeatureInfo;
import jcolibri.util.AttributeUtils;
import jcolibri.util.ProgressController;

/**
 * Extract features from text using the GATE grammars in jape format.
 * This method uses internally an ANNIETransducer object.
 * GATE's default rules file or any other file can be loaded.
 * <br>
 * It is compatible with the generic FeaturesExtractor so they can be executed together.
 * <br>
 * For more information see the GATE tutorial.
 * @author Juan A. Recio-Garcia
 * @version 1.0
 * 
 */
public class GateFeaturesExtractor
{

    /**
     * Performs the algorithm in the given attributes of a collection of cases.
     * These attributes must be IETextGate objects.
     */
    public static void extractFeatures(Collection<CBRCase> cases, Collection<Attribute> attributes)
    {
	org.apache.commons.logging.LogFactory.getLog(GateFeaturesExtractor.class).info("Extracting features.");
	ProgressController.init(GateFeaturesExtractor.class, "Extracting features ...", cases.size());
	for(CBRCase c: cases)
	{
	    for(Attribute a: attributes)
	    {
		Object o = AttributeUtils.findValue(a, c);
		extractFeatures((IETextGate)o);
	    }
	    ProgressController.step(GateFeaturesExtractor.class);
	}
	ProgressController.finish(GateFeaturesExtractor.class);
    }

    /**
     * Performs the algorithm in the given attributes of a query.
     * These attributes must be IETextGate objects.
     */
    public static void extractFeatures(CBRQuery query, Collection<Attribute> attributes)
    {
	org.apache.commons.logging.LogFactory.getLog(GateFeaturesExtractor.class).info("Extracting features.");
	for(Attribute a: attributes)
	{
	    Object o = AttributeUtils.findValue(a, query);
	    extractFeatures((IETextGate)o);
	}
    }

    /**
     * Performs the algorithm in all the IETextGate typed attributes of a collection of cases.
     */  
    public static void extractFeatures(Collection<CBRCase> cases)
    {
	org.apache.commons.logging.LogFactory.getLog(GateFeaturesExtractor.class).info("Extracting features.");
	ProgressController.init(GateFeaturesExtractor.class, "Extracting features ...", cases.size());
	for(CBRCase c: cases)
	{
	    Collection<IEText> texts = IEutils.getTexts(c);
	    for(IEText t : texts)
	        if(t instanceof IETextGate)
	            extractFeatures((IETextGate)t);
	    ProgressController.step(GateFeaturesExtractor.class);
	}
	ProgressController.finish(GateFeaturesExtractor.class);
    }
    
    /**
     * Performs the algorithm in all the IETextGate typed attributes of a query.
     */ 
    public static void extractFeatures(CBRQuery query)
    {	    
	org.apache.commons.logging.LogFactory.getLog(GateFeaturesExtractor.class).info("Extracting features.");
	Collection<IEText> texts = IEutils.getTexts(query);
        for(IEText t : texts)
            if(t instanceof IETextGate)
        	extractFeatures((IETextGate)t);
    }   
    
    /**
     * Performs the algorithm in a given IETextGate object
     */
    public static void extractFeatures(IETextGate text)
    {
	try
	{

	    featureExtractor.setDocument(text.getDocument());
	    featureExtractor.execute();

	    String content = text.getRAWContent();

	    AnnotationSet featuresAnnotations = text.getDocument().getAnnotations("Features");

	    for (Iterator iter = featuresAnnotations.iterator(); iter.hasNext();)
	    {
		Annotation anot = (Annotation) iter.next();
		String Type = (String) anot.getType();
		int begin = anot.getStartNode().getOffset().intValue();
		int end = anot.getEndNode().getOffset().intValue();
		String value = content.substring(begin, end);
		text.addFeature(new FeatureInfo(Type, value, begin, end));
	    }

	    // System.err.print(text.getDocument());

	} catch (ExecutionException e)
	{
	    org.apache.commons.logging.LogFactory.getLog(GateFeaturesExtractor.class).error(e);
	}
    }

    static Transducer featureExtractor;

    private static String defaultRulesFileName ="jcolibri/extensions/textual/IE/gate/gateinit/plugins/ANNIE/resources/NE/main.jape"; 
    
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
	    featureExtractor = (Transducer) Factory.createResource("gate.creole.ANNIETransducer");
	    featureExtractor.setGrammarURL(jcolibri.util.FileIO.findFile(filename));
	    featureExtractor.setOutputASName("Features");
	    featureExtractor.init();
	} catch (Exception e)
	{
	    org.apache.commons.logging.LogFactory.getLog(GatePhrasesExtractor.class).error(e);

	}

    }
}
