/**
 * GlossaryLinker.java
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.cbrcore.CaseComponent;
import jcolibri.exception.AttributeAccessException;
import jcolibri.extensions.textual.IE.IEutils;
import jcolibri.extensions.textual.IE.representation.IEText;
import jcolibri.extensions.textual.IE.representation.Token;
import jcolibri.extensions.textual.IE.representation.info.WeightedRelation;
import jcolibri.extensions.textual.stemmer.Stemmer;
import jcolibri.util.AttributeUtils;
import jcolibri.util.ProgressController;

/**
 * 
 * Relates query words to cases words using a domain specific glossary.
 * <p>
 * Tokens are related using a list of WeightedRelation objects.
 * These relations are stored in each Token instance.
 * <p>
 * Glossary Format:
 * <p>
 * [Part-of-Speech Tag]{Similarity} word1 word2 ... wordn
 * <ul>
 * <li>Part-of-Speech Tag: Sometimes words can have different POS tags, this
 * parameter marks that the following words are only related when they appear in
 * a sentence with that tag.
 * <p>
 * Possible values: NOUN, VERB, ADJECTIVE, ADVERB
 * <li>Similarity: Indicates the similarity relation.
 * <p>
 * Possible values: 1, 2, 3. (1 - very similar, 2 - similar, 3 - not very
 * similar)
 * <li>Words must be separated with white spaces.
 * </ul>
 * <p>
 * The first version was developed at: Robert Gordon University - Aberdeen & Facultad Inform�tica,
 * Universidad Complutense de Madrid (GAIA)
 * </p>
 * @author Juan A. Recio-Garcia
 * @version 2.0
 * 
 */
public class GlossaryLinker
{
    /**
     * Performs the algorithm in all the ttributes of a collection of cases and a query.
     * These attributes must be IEText objects.
     */
    public static void LinkWithGlossary(Collection<CBRCase> cases, CBRQuery query)
    {	
	org.apache.commons.logging.LogFactory.getLog(GlossaryLinker.class).info("Linking tokens with user glossary.");
	ProgressController.init(GlossaryLinker.class, "Linking tokens with user glossary ...", cases.size());

	List<IEText> queryTexts = new ArrayList<IEText>();
	IEutils.addTexts(query.getDescription(), queryTexts);

	for(CBRCase c: cases)
	{
	    List<IEText> caseTexts = new ArrayList<IEText>();
	    IEutils.addTexts(c.getDescription(), caseTexts);
	    
	    for(int i=0; i<queryTexts.size(); i++)
	    {
		IEText queryText = queryTexts.get(i);
	    	IEText caseText  = caseTexts.get(i);
		linkWithGlossary(caseText, queryText);
	    }
	    ProgressController.step(GlossaryLinker.class);
	}
	ProgressController.finish(GlossaryLinker.class);
    }

    /**
     * Performs the algorithm in the given attributes of a collection of cases and a query.
     * These attributes must be IEText objects.
     */
    public static void linkWithGlossary(Collection<CBRCase> cases, CBRQuery query, Collection<Attribute> attributes)
    {
	org.apache.commons.logging.LogFactory.getLog(GlossaryLinker.class).info("Linking tokens with user glossary.");
	ProgressController.init(GlossaryLinker.class, "Linking tokens with user glossary ...", cases.size());
	
	for(CBRCase c: cases)
	{
	    for(Attribute at: attributes)
	    {
		CaseComponent caseCC  = AttributeUtils.findBelongingComponent(at, c);
		CaseComponent queryCC = AttributeUtils.findBelongingComponent(at, query);
		
		try
		{
		    IEText queryText = (IEText)at.getValue(queryCC);
		    IEText caseText  = (IEText)at.getValue(caseCC);
		    linkWithGlossary(caseText, queryText);
		} catch (AttributeAccessException e)
		{
		    org.apache.commons.logging.LogFactory.getLog(GlossaryLinker.class).error(e);
		}
	    }
	    ProgressController.step(GlossaryLinker.class);
	}
	ProgressController.finish(GlossaryLinker.class);
    }
    
    
    protected static ArrayList<GlossaryTriple> glossary;

    /**
     * Links two text objects using the glossary.
     */
    public static void linkWithGlossary(IEText caseText, IEText queryText)
    {
        List<Token> queryTokens = queryText.getAllTokens();
        List<Token> caseTokens  = caseText.getAllTokens();
        
	for(GlossaryTriple gt : glossary)
        {
            String posTag     = gt._posTag;
            Set<String> words = gt._words;
            int weight        = gt._weight;
            
       	    for(Token queryTok : queryTokens)
            {
       		String queryStem = queryTok.getStem();
       		if(!words.contains(queryStem))
       		    continue;
  		String queryPOS = lookupGlossaryPos(queryTok.getPostag());
  		if(!queryPOS.equals(posTag))
  		    continue;

       		for(Token caseTok: caseTokens)
       		{
       		    String caseStem = caseTok.getStem();
       		    if(!words.contains(caseStem))
       			continue;
       		    if(caseStem.equals(queryStem))
       			continue;
       		    
       		    String casePOS  = lookupGlossaryPos(caseTok.getPostag());
       		    
       		    if(!queryPOS.equals(casePOS))
       			continue;
       		    
       		    queryTok.addRelation(new WeightedRelation(queryTok, caseTok, 1/weight));
       		    org.apache.commons.logging.LogFactory.getLog(GlossaryLinker.class).info("Adding relation: "+queryTok.getRawContent()+" --> "+caseTok.getRawContent()+". Weight: "+ 1/weight);
       		}
            }
	}
    }
    
    
    /**
     * Load glossary reations stored in GLOSSARY_FILE
     */
    public static void loadGlossary(String filename)
    {
	glossary = new ArrayList<GlossaryTriple>();

	try
	{
	    URL file = jcolibri.util.FileIO.findFile(filename);
	    BufferedReader br = new BufferedReader( new InputStreamReader(file.openStream()));


	    String line = "";

	    Stemmer stemmer = new Stemmer();
	    while ((line = br.readLine()) != null)
	    {
		if (line.startsWith("#"))
		    continue;
		int pos = line.indexOf(']');
		if (pos == -1)
		    throw new Exception(line + "  POSTag field not found");
		String _posTag = line.substring(1, pos);
		String _rest = line.substring(pos + 1);
		pos = _rest.indexOf('}');
		if (pos == -1)
		    throw new Exception(line + "  Weight field not found");
		String _weight = _rest.substring(1, pos);
		int weight = Integer.parseInt(_weight);
		String _words = _rest.substring(pos + 1);
		StringTokenizer st = new StringTokenizer(_words, " ");
		Set<String> words = new HashSet<String>();
		while (st.hasMoreTokens())
		{
		    String sw = st.nextToken();
		    words.add(stemmer.stem(sw));
		}

		glossary.add(new GlossaryTriple(_posTag, words, weight));
	    }
	    br.close();
	} catch (Exception e)
	{
	    org.apache.commons.logging.LogFactory.getLog(GlossaryLinker.class)
		    .error(e);
	}

    }

    /**
     * This method transforms POS tags defined in PartofSpeechMethod to the
     * tags used in the glossary file
     * 
     * @param tag
     *                POS tag
     * @return NOUN, VERB, ADJECTIVE or ADVERB
     */
    static String lookupGlossaryPos(String tag)
    {
	/*
         * 12. NN Noun, singular or mass 13. NNS Noun, plural
         */
	if (tag.equals("NN") || tag.equals("NNS"))
	    return "NOUN";
	/*
         * 27. VB Verb, base form 28. VBD Verb, past tense 29. VBG Verb, gerund
         * or present participle 30. VBN Verb, past participle 31. VBP Verb,
         * non-3rd person singular present 32. VBZ Verb, 3rd person singular
         * present
         */
	if (tag.startsWith("V"))
	    return "VERB";

	/*
         * 7. JJ Adjective 8. JJR Adjective, comparative 9. JJS Adjective,
         * superlative
         */
	if (tag.startsWith("J"))
	    return "ADJECTIVE";

	/*
         * 20. RB Adverb 21. RBR Adverb, comparative 22. RBS Adverb, superlative
         */
	if (tag.startsWith("RB"))
	    return "ADVERB";

	return null;
    }

    static private class GlossaryTriple
    {
	String _posTag;

	Set<String> _words;

	int _weight;

	GlossaryTriple(String p, Set<String> wor, int w)
	{
	    _posTag = p;
	    _words = wor;
	    _weight = w;
	}
    }

}
