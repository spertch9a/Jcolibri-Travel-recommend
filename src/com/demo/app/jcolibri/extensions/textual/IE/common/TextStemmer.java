/**
 * Stemmer.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 20/06/2007
 */
package com.demo.app.jcolibri.extensions.textual.IE.common;

import java.util.Collection;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.extensions.textual.IE.IEutils;
import jcolibri.extensions.textual.IE.gate.GatePhrasesExtractor;
import jcolibri.extensions.textual.IE.representation.IEText;
import jcolibri.extensions.textual.IE.representation.Token;
import jcolibri.extensions.textual.stemmer.Stemmer;
import jcolibri.util.AttributeUtils;
import jcolibri.util.ProgressController;

/**
 * Stemes the tokens of the text using the SnowBall package. 
 * <a href="http://snowball.tartarus.org">http://snowball.tartarus.org</a>
 * <br>
 * It stores the stem in each token using the flag with the same name.
 * <p>
 * This method uses the SnowBall package: 
 * </p>
 * <p>
 * First version was developed at: Robert Gordon University - Aberdeen & Facultad Inform�tica,
 * Universidad Complutense de Madrid (GAIA)
 * </p>
 * @author Juan A. Recio-Garcia
 * @version 2.0
 *
 */
public class TextStemmer
{
    static Stemmer stemmer = new Stemmer();
 
    /**
     * Performs the algorithm in the given attributes of a collection of cases.
     * These attributes must be IEText objects.
     */
    public static void stem(Collection<CBRCase> cases, Collection<Attribute> attributes)
    {
	org.apache.commons.logging.LogFactory.getLog(TextStemmer.class).info("Stemming text.");
	ProgressController.init(TextStemmer.class, "Stemming text...", cases.size());
	for(CBRCase c: cases)
	{
	    for(Attribute a: attributes)
	    {
		Object o = AttributeUtils.findValue(a, c);
		stem((IEText)o);
	    }
	    ProgressController.step(GatePhrasesExtractor.class);
	}
	ProgressController.finish(GatePhrasesExtractor.class);
    }

    /**
     * Performs the algorithm in the given attributes of a query.
     * These attributes must be IEText objects.
     */
    public static void stem(CBRQuery query, Collection<Attribute> attributes)
    {
	    org.apache.commons.logging.LogFactory.getLog(TextStemmer.class).info("Stemming text.");
	    for(Attribute a: attributes)
	    {
		Object o = AttributeUtils.findValue(a, query);
		stem((IEText)o);
	    }
    }
    
    /**
     * Performs the algorithm in all the attributes of a collection of cases
     * These attributes must be IEText objects.
     */
    public static void stem(Collection<CBRCase> cases)
    {
	org.apache.commons.logging.LogFactory.getLog(TextStemmer.class).info("Stemming text.");
	ProgressController.init(TextStemmer.class, "Stemming text...", cases.size());
	for(CBRCase c: cases)
	{
	    Collection<IEText> texts = IEutils.getTexts(c);
	    for(IEText t : texts)
		stem(t);
	    ProgressController.step(GatePhrasesExtractor.class);
	}
	ProgressController.finish(GatePhrasesExtractor.class);
    }
    
    /**
     * Performs the algorithm in all the attributes of a query
     * These attributes must be IEText objects.
     */
    public static void stem(CBRQuery query)
    {	   
	org.apache.commons.logging.LogFactory.getLog(TextStemmer.class).info("Stemming text.");
	Collection<IEText> texts = IEutils.getTexts(query);
        for(IEText t : texts)
            stem(t);
    }
    
    
    /**
     * Stems the tokens of the text. If no stem is found, it stores the original word as the stem.
     * @param text to stem
     */
    public static void stem(IEText text)
    {
	for(Token t: text.getAllTokens())
	    if(!t.isStopWord())
	    {
		String stem = stemmer.stem(t.getRawContent());
		if(stem == null)
		    stem = t.getRawContent();
		t.setStem(stem);
	    }
    }
}
