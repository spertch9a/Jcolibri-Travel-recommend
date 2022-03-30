/**
 * FeaturesExtractor.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 21/06/2007
 */
package com.demo.app.jcolibri.extensions.textual.IE.common;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.extensions.textual.IE.IEutils;
import jcolibri.extensions.textual.IE.gate.GatePhrasesExtractor;
import jcolibri.extensions.textual.IE.representation.IEText;
import jcolibri.extensions.textual.IE.representation.info.FeatureInfo;
import jcolibri.util.AttributeUtils;
import jcolibri.util.ProgressController;

/**
 *
 * <p>
 * Extracts features using Regular Expressions.
 * </p>
 * <p>
 * Rules format is:
 * </p>
 * <p>
 * [FeatureName]{FeaturePosition}FeatureRegularExpresion
 * <ul>
 * <li>FeatureName is used to store the extracted information
 * <li>FeaturePosition indicates the position of the information that we want
 * to extract inside the regular expression. The feature is indicated by
 * counting the opening parentheses from left to right.
 * <p>
 * In the expression ((A)(B(C))), for example, there are four such groups:
 * <ol>
 * <li> ((A)(B(C)))
 * <li> (A)
 * <li> (B(C))4(C)
 * </ol>
 * <p>
 * Group zero always stands for the entire expression
 * <li>Regular Expressions are deffined following java.util.regex.Pattern
 * syntaxis. (See API for details)
 * </ul>
 * <p>
 * The first version was developed at: Robert Gordon University - Aberdeen & Facultad Inform�tica,
 * Universidad Complutense de Madrid (GAIA)
 * </p>
 * @author Juan A. Recio-Garcia
 * @version 2.0
 * 
 */
public class FeaturesExtractor
{
    static ArrayList<FeatureRule> featuresRules;

    /**
     * Performs the algorithm in the given attributes of a collection of cases.
     * These attributes must be IEText objects.
     */
    public static void extractFeatures(Collection<CBRCase> cases, Collection<Attribute> attributes)
    {
	org.apache.commons.logging.LogFactory.getLog(FeaturesExtractor.class).info("Extracting features.");
	ProgressController.init(PhrasesExtractor.class, "Extracting features ...", cases.size());
	for(CBRCase c: cases)
	{
	    for(Attribute a: attributes)
	    {
		Object o = AttributeUtils.findValue(a, c);
		extractFeatures((IEText)o);
	    }
	    ProgressController.step(GatePhrasesExtractor.class);
	}
	ProgressController.finish(GatePhrasesExtractor.class);
    }

    /**
     * Performs the algorithm in the given attributes of a query.
     * These attributes must be IEText objects.
     */
    public static void extractFeatures(CBRQuery query, Collection<Attribute> attributes)
    {
	org.apache.commons.logging.LogFactory.getLog(FeaturesExtractor.class).info("Extracting features.");
	for(Attribute a: attributes)
	{
	    Object o = AttributeUtils.findValue(a, query);
	    extractFeatures((IEText)o);
	}
    }
    
    /**
     * Performs the algorithm in all the attributes of a collection of cases
     * These attributes must be IEText objects.
     */
    public static void extractFeatures(Collection<CBRCase> cases)
    {
	org.apache.commons.logging.LogFactory.getLog(FeaturesExtractor.class).info("Extracting features.");
	ProgressController.init(PhrasesExtractor.class, "Extracting features ...", cases.size());
	for(CBRCase c: cases)
	{
	    Collection<IEText> texts = IEutils.getTexts(c);
	    for(IEText t : texts)
		extractFeatures(t);
	    ProgressController.step(GatePhrasesExtractor.class);
	}
	ProgressController.finish(GatePhrasesExtractor.class);
    }
    
    /**
     * Performs the algorithm in all the attributes of a query
     * These attributes must be IEText objects.
     */
    public static void extractFeatures(CBRQuery query)
    {	 
	org.apache.commons.logging.LogFactory.getLog(FeaturesExtractor.class).info("Extracting features.");
	Collection<IEText> texts = IEutils.getTexts(query);
        for(IEText t : texts)
            extractFeatures(t);
    }    
    
    /**
     * Performs the algorithm in a given IEText object
     */
    public static void extractFeatures(IEText text)
    {
	String rawText = text.getRAWContent();
	for (FeatureRule rule : featuresRules)
	{
	    Matcher m = rule._pattern.matcher(rawText);
	    while (m.find())
	    {
		String group = m.group(rule._group);
		group = cleanSpaces(group);
		text.addFeature(new FeatureInfo(rule._feature, group, m.start(), m.end()));
	    }
	}
    }

    static private String cleanSpaces(String w)
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

    /**
         * Load the features rules
         */
    public static void loadRules(String filename)
    {
	try
	{
	    featuresRules = new ArrayList<FeatureRule>();
	    URL file = jcolibri.util.FileIO.findFile(filename);
	    BufferedReader br = new BufferedReader( new InputStreamReader(file.openStream()));

	    String line = "";
	    while ((line = br.readLine()) != null)
	    {
		if (line.startsWith("#"))
		    continue;
		int pos = line.indexOf(']');
		if (pos == -1)
		    throw new Exception(line + "  Feature field not found");
		String _feature = line.substring(1, pos);
		String _rest = line.substring(pos + 1);
		pos = _rest.indexOf('}');
		if (pos == -1)
		    throw new Exception(line
			    + "  FeaturePostion field not found");
		String _group = _rest.substring(1, pos);
		String _rule = _rest.substring(pos + 1);
		int g = Integer.parseInt(_group);
		featuresRules.add(new FeatureRule(_feature, Pattern.compile(_rule),g));
	    }
	    br.close();
	} catch (Exception e)
	{
	    org.apache.commons.logging.LogFactory.getLog(
		    FeaturesExtractor.class).error(e);
	}
    }

    private static class FeatureRule
    {
	String _feature;

	Pattern _pattern;

	int _group;

	FeatureRule(String _f, Pattern _p, int _g)
	{
	    _feature = _f;
	    _pattern = _p;
	    _group = _g;
	}
    }
}
