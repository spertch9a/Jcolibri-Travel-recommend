/**
 * OpennlpMainNamesExtractor.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 20/06/2007
 */
package com.demo.app.jcolibri.extensions.textual.IE.opennlp;

import java.util.Collection;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.extensions.textual.IE.IEutils;
import jcolibri.extensions.textual.IE.representation.IEText;
import jcolibri.extensions.textual.IE.representation.Token;
import jcolibri.util.AttributeUtils;
import jcolibri.util.ProgressController;
import opennlp.grok.preprocess.namefind.EnglishNameFinderME;
import opennlp.grok.preprocess.namefind.NameFinderME;

import org.jdom.Element;

/**
 * Identifies the tokens that are main names in the sencence using a Maximum entrophy algorithm.
 * The "isMainName" flag of the Token object is activated if a token is a main name.
 * @author Juan A. Recio-Garcia
 * @version 2.0
 *
 */
public class OpennlpMainNamesExtractor
{
    /**
     * Performs the algorithm in the given attributes of a collection of cases.
     * These attributes must be IETextOpenNLP objects.
     */
    public static void extractMainNames(Collection<CBRCase> cases, Collection<Attribute> attributes)
    {
	org.apache.commons.logging.LogFactory.getLog(OpennlpMainNamesExtractor.class).info("Extracting main names.");
	ProgressController.init(OpennlpMainNamesExtractor.class, "Extracting main names...", cases.size());
	for(CBRCase c: cases)
	{
	    for(Attribute a: attributes)
	    {
		Object o = AttributeUtils.findValue(a, c);
		if(o instanceof IETextOpenNLP)
		    extractMainNames((IETextOpenNLP)o);
	    }
	    ProgressController.step(OpennlpMainNamesExtractor.class);
	}
	ProgressController.finish(OpennlpMainNamesExtractor.class);
    }

    /**
     * Performs the algorithm in the given attributes of a query.
     * These attributes must be IETextOpenNLP objects.
     */
    public static void extractMainNames(CBRQuery query, Collection<Attribute> attributes)
    {
	    org.apache.commons.logging.LogFactory.getLog(OpennlpMainNamesExtractor.class).info("Extracting main names.");
	    for(Attribute a: attributes)
	    {
		Object o = AttributeUtils.findValue(a, query);
		if(o instanceof IETextOpenNLP)
		    extractMainNames((IETextOpenNLP)o);
	    }
    }
    
    /**
     * Performs the algorithm in all the IETextOpenNLP typed attributes of a collection of cases.
     */
    public static void extractMainNames(Collection<CBRCase> cases)
    {
	org.apache.commons.logging.LogFactory.getLog(OpennlpMainNamesExtractor.class).info("Extracting main names.");
	ProgressController.init(OpennlpMainNamesExtractor.class, "Extracting main names", cases.size());
	for(CBRCase c: cases)
	{
	    Collection<IEText> texts = IEutils.getTexts(c);
	    for(IEText t : texts)
		if(t instanceof IETextOpenNLP)
		extractMainNames((IETextOpenNLP)t);
	    ProgressController.step(OpennlpMainNamesExtractor.class);
	}
	ProgressController.finish(OpennlpMainNamesExtractor.class);
    }
    
    /**
     * Performs the algorithm in all the IETextOpenNLP typed attributes of a query.
     */ 
    public static void extractMainNames(CBRQuery query)
    {	   
	org.apache.commons.logging.LogFactory.getLog(OpennlpMainNamesExtractor.class).info("Extracting main names.");
	Collection<IEText> texts = IEutils.getTexts(query);
        for(IEText t : texts)
            if(t instanceof IETextOpenNLP)
        	 extractMainNames((IETextOpenNLP)t);
    }
    
    /**
     * Performs the algorithm in a given IETextOpenNLP object
     */
    public static void extractMainNames(IETextOpenNLP text)
    {
	NameFinderME nameFinder = getNameFinder();
	try
	{
	    nameFinder.process(text.getDocument());
	} catch (Exception e)
	{
	    //org.apache.commons.logging.LogFactory.getLog(OpennlpMainNamesExtractor.class).warn("There was an error extracting main names. Continuing..."); 
	}
	
	for(Token t: text.getAllTokens())
	{
	    Element tok = text.getTokenMapping(t);
	    String val  = tok.getAttributeValue("type");
	    t.setMainName((val!=null)&&val.equals("name"));
	}
    }
    
    private static NameFinderME nameFinder = null;
    private static NameFinderME getNameFinder()
    {
	if(nameFinder == null)
	    nameFinder = new EnglishNameFinderME();
	return nameFinder;
    }
}
