/**
 * DomainTopicClassifier.java
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.extensions.textual.IE.IEutils;
import jcolibri.extensions.textual.IE.representation.IEText;
import jcolibri.extensions.textual.IE.representation.info.FeatureInfo;
import jcolibri.extensions.textual.IE.representation.info.PhraseInfo;
import jcolibri.util.AttributeUtils;
import jcolibri.util.ProgressController;

/**
 * Classifies textual objects with a topic depending on the features and phrases.
 * <br>
 * This method uses a configuration file with rules following the syntaxis:
 * <p>[Topic] &lt;FeatureName,FeatureValue&gt; &lt;FeatureName,FeatureValue&gt; ... &lt;Phrase&gt; &lt;Phrase&gt;</p>
 * where:
 * <ul>
 * <li>Topic: Topic name
 * <li>FeatureName: FeatureName extracted by features extraction method
 * <li>FeatureValue: Feature value. It also can be '?', meaning any value.
 * <li>Phrase: Any phrase identifier extracted by the phrases extraction method.
 * </ul>
 * <p>
 * First version was developed at: Robert Gordon University - Aberdeen & Facultad Inform�tica,
 * Universidad Complutense de Madrid (GAIA)
 * </p>
 * @author Juan A. Recio-Garcia
 * @version 2.0
 * 
 */
public class DomainTopicClassifier
{
    static ArrayList<TopicRule> topicsRules;

    /**
     * Performs the algorithm in the given attributes of a collection of cases.
     * These attributes must be IEText objects.
     */
    public static void classifyWithTopic(Collection<CBRCase> cases, Collection<Attribute> attributes)
    {
	org.apache.commons.logging.LogFactory.getLog(DomainTopicClassifier.class).info("Classifying with topic.");
	ProgressController.init(DomainTopicClassifier.class, "Classifying with topic  ...", cases.size());
	for(CBRCase c: cases)
	{
	    for(Attribute a: attributes)
	    {
		Object o = AttributeUtils.findValue(a, c);
		classifyWithTopic((IEText)o);
	    }
	    ProgressController.step(DomainTopicClassifier.class);
	}
	ProgressController.finish(DomainTopicClassifier.class);
    }

    /**
     * Performs the algorithm in the given attributes of a query.
     * These attributes must be IEText objects.
     */
    public static void classifyWithTopic(CBRQuery query, Collection<Attribute> attributes)
    {
	org.apache.commons.logging.LogFactory.getLog(DomainTopicClassifier.class).info("Classifying with topic.");
	for(Attribute a: attributes)
	{
	    Object o = AttributeUtils.findValue(a, query);
	    classifyWithTopic((IEText)o);
	}
    }
    
    /**
     * Performs the algorithm in all the attributes of a collection of cases
     * These attributes must be IEText objects.
     */
    public static void classifyWithTopic(Collection<CBRCase> cases)
    {
	org.apache.commons.logging.LogFactory.getLog(DomainTopicClassifier.class).info("Classifying with topic.");
	ProgressController.init(DomainTopicClassifier.class, "Classifying with topic  ...", cases.size());
	for(CBRCase c: cases)
	{
	    Collection<IEText> texts = IEutils.getTexts(c);
	    for(IEText t : texts)
		classifyWithTopic(t);
	    ProgressController.step(DomainTopicClassifier.class);
	}
	ProgressController.finish(DomainTopicClassifier.class);
    }
    
    /**
     * Performs the algorithm in all the attributes of a query
     * These attributes must be IEText objects.
     */
    public static void classifyWithTopic(CBRQuery query)
    {
	org.apache.commons.logging.LogFactory.getLog(DomainTopicClassifier.class).info("Classifying with topic.");
	Collection<IEText> texts = IEutils.getTexts(query);
        for(IEText t : texts)
            classifyWithTopic(t);
    }  
    
    /**
     * Performs the algorithm in a given IEText object
     */
    public static void classifyWithTopic(IEText text)
    {
	Collection<PhraseInfo> _phrases = text.getPhrases();
	Collection<FeatureInfo> _features = text.getFeatures();
	for (TopicRule rule : topicsRules)
	{
	    // Chech rule conditions
	    boolean valid = true;
	    HashMap<String, String> conditions = rule._data;
	    Iterator<String> fOpIter = conditions.keySet().iterator();
	    // For each condition
	    while (fOpIter.hasNext() && valid)
	    {
		String featureOrPhrase = (String) fOpIter.next();
		String value = (String) conditions.get(featureOrPhrase);
		// It's a phrase condition
		if (value == null)
		{
		    boolean found = false;
		    for (Iterator<PhraseInfo> it = _phrases.iterator(); it.hasNext() && !found;)
		    {
			PhraseInfo pi = it.next();
			if (pi.getPhrase().equals(featureOrPhrase))
			    found = true;
		    }
		    valid = found;
		}
		// It's a feature condition
		else
		{
		    boolean found = false;
		    for (Iterator<FeatureInfo> it = _features.iterator(); it.hasNext() && !found;)
		    {
			FeatureInfo fi = it.next();
			if (!value.equals("?"))
			    found = (fi.getFeature().equals(featureOrPhrase) && fi.getValue().equals(value));
			else
			    found = fi.getFeature().equals(featureOrPhrase);
		    }
		    valid = found;
		}
	    }
	    // If rule conditions are true -> include rule name in
	    // Topics
	    if (valid)
		text.addTopic(rule._name);
	}
    }

    /**
     * Load topic classification rules file.
     */
    public static void loadRules(String filename)
    {
	try
	{
	    topicsRules = new ArrayList<TopicRule>();
	    URL file = jcolibri.util.FileIO.findFile(filename);
	    BufferedReader br = new BufferedReader( new InputStreamReader(file.openStream()));
	    String line = "";
	    while ((line = br.readLine()) != null)
	    {
		if (line.startsWith("#"))
		    continue;
		int pos = line.indexOf(']');
		if (pos == -1)
		    throw new Exception(line + "  Topic field not found");
		String _feature = line.substring(1, pos);
		String _rest = line.substring(pos + 1);

		HashMap<String, String> data = new HashMap<String, String>();
		int indexOpen;
		int indexClose;
		while (((indexOpen = _rest.indexOf("<")) != -1) && ((indexClose = _rest.indexOf(">")) != -1))
		{
		    String content = _rest.substring(indexOpen, indexClose);
		    StringTokenizer st = new StringTokenizer(content, "<,>");
		    if (!st.hasMoreTokens())
			continue;
		    String featureOrPhrase = st.nextToken();
		    String value = null;
		    if (st.hasMoreTokens())
			value = st.nextToken();
		    // If its a Phrase condition -> value == null
		    data.put(cleanSpaces(featureOrPhrase), cleanSpaces(value));
		    _rest = _rest.substring(indexClose + 1, _rest.length());
		}

		TopicRule rule = new TopicRule(_feature, data);
		topicsRules.add(rule);
	    }
	    br.close();
	} catch (Exception e)
	{
	    org.apache.commons.logging.LogFactory.getLog(DomainTopicClassifier.class).error(e);
	}
    }

    static private class TopicRule
    {
	String _name;

	HashMap<String, String> _data;

	TopicRule(String n, HashMap<String, String> d)
	{
	    _name = n;
	    _data = d;
	}
    }

    static private String cleanSpaces(String w)
    {
	if (w == null)
	    return null;
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
