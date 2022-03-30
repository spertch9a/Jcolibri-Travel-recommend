/**
 * PhrasesExtractor.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 20/06/2007
 */
package com.demo.app.jcolibri.extensions.textual.IE.common;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.extensions.textual.IE.IEutils;
import jcolibri.extensions.textual.IE.gate.GatePhrasesExtractor;
import jcolibri.extensions.textual.IE.representation.IEText;
import jcolibri.extensions.textual.IE.representation.info.PhraseInfo;
import jcolibri.util.AttributeUtils;
import jcolibri.util.ProgressController;



/**
 *
 * <p>
 * Extracts Phrases using Regular Expressions.
 * </p>
 * <p>
 * Rules file format is:
 * </p>
 * <p>
 * [PhraseName]PhraseRegularExpresion
 * <ul>
 * <il>PhraseName is used to store the extracted information <il>Regular
 * Expressions are deffined following java.util.regex.Pattern syntaxis. (See API
 * for details)
 * </ul>
 * <p>
 * The first version was developed at: Robert Gordon University - Aberdeen & Facultad Inform�tica,
 * Universidad Complutense de Madrid (GAIA)
 * </p>
 * @author Juan A. Recio-Garcia
 * @version 2.0
 * 
 */
public class PhrasesExtractor
{
    /**
     * Performs the algorithm in the given attributes of a collection of cases.
     * These attributes must be IEText objects.
     */
    public static void extractPhrases(Collection<CBRCase> cases, Collection<Attribute> attributes)
    {
	org.apache.commons.logging.LogFactory.getLog(PhrasesExtractor.class).info("Extracting phrases.");
	ProgressController.init(PhrasesExtractor.class, "Extracting phrases ...", cases.size());
	for(CBRCase c: cases)
	{
	    for(Attribute a: attributes)
	    {
		Object o = AttributeUtils.findValue(a, c);
		extractPhrases((IEText)o);
	    }
	    ProgressController.step(GatePhrasesExtractor.class);
	}
	ProgressController.finish(GatePhrasesExtractor.class);
    }

    /**
     * Performs the algorithm in the given attributes of a query.
     * These attributes must be IEText objects.
     */
    public static void extractPhrases(CBRQuery query, Collection<Attribute> attributes)
    {
	org.apache.commons.logging.LogFactory.getLog(PhrasesExtractor.class).info("Extracting phrases.");
	for(Attribute a: attributes)
	{
	    Object o = AttributeUtils.findValue(a, query);
	    extractPhrases((IEText)o);
	}
    }
    
    /**
     * Performs the algorithm in all the attributes of a collection of cases
     * These attributes must be IEText objects.
     */
    public static void extractPhrases(Collection<CBRCase> cases)
    {
	org.apache.commons.logging.LogFactory.getLog(PhrasesExtractor.class).info("Extracting phrases.");
	ProgressController.init(PhrasesExtractor.class, "Extracting phrases ...", cases.size());
	for(CBRCase c: cases)
	{
	    Collection<IEText> texts = IEutils.getTexts(c);
	    for(IEText t : texts)
		extractPhrases(t);
	    ProgressController.step(GatePhrasesExtractor.class);
	}
	ProgressController.finish(GatePhrasesExtractor.class);
    }
    
    /**
     * Performs the algorithm in all the attributes of a query
     * These attributes must be IEText objects.
     */
    public static void extractPhrases(CBRQuery query)
    {	  
	org.apache.commons.logging.LogFactory.getLog(PhrasesExtractor.class).info("Extracting phrases.");
	Collection<IEText> texts = IEutils.getTexts(query);
        for(IEText t : texts)
            extractPhrases(t);
    }

    
    
    static HashMap<String, Pattern> rulesList;
    
    /**
     * Performs the algorithm in a given IEText object
     */
    public static void extractPhrases(IEText text)
    {
	String rawText = text.getRAWContent();
	
	for(String rule : rulesList.keySet())
	{
	    Pattern pattern = rulesList.get(rule);
	    Matcher m = pattern.matcher(rawText);
	    while (m.find()) {
		text.addPhrase(new PhraseInfo(rule, m.start(), m.end()));
	    }
			
	}
    }
    
    
    
    /**
    * Loads a rules file
    */
    public static void loadRules(String filename)
    {
	try
	{
	    URL file = jcolibri.util.FileIO.findFile(filename);
	    BufferedReader br = new BufferedReader( new InputStreamReader(file.openStream()));
	    rulesList = new HashMap<String,Pattern>();
	  
	    String line = "";
	    while ((line = br.readLine()) != null)
	    {
	        if (line.startsWith("#"))
	    	continue;
	        int pos = line.indexOf(']');
	        if (pos == -1)
	    	throw new Exception(line + "  Feature field not found");
	        String _feature = line.substring(1, pos);
	        String _rule = line.substring(pos + 1);
	        rulesList.put(cleanSpaces(_feature), Pattern.compile(_rule));
	    }
	    br.close();
	} catch (Exception e)
	{
	    org.apache.commons.logging.LogFactory.getLog(PhrasesExtractor.class).error(e); 
	}
    }

    private static String cleanSpaces(String w)
    {
	String res = "";
	StringTokenizer st = new StringTokenizer(w, " ");
	while (st.hasMoreTokens())
	{
	    res += st.nextToken();
	    if (st.hasMoreTokens())
		res += " ";
	}
	return res;
    }
}
