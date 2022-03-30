/**
 * ThesaurusLinker.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 21/06/2007
 */
package com.demo.app.jcolibri.extensions.textual.IE.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.cbrcore.CaseComponent;
import jcolibri.exception.AttributeAccessException;
import jcolibri.extensions.textual.IE.IEutils;
import jcolibri.extensions.textual.IE.representation.IEText;
import jcolibri.extensions.textual.IE.representation.Token;
import jcolibri.extensions.textual.IE.representation.info.WeightedRelation;
import jcolibri.extensions.textual.wordnet.WordNetBridge;
import jcolibri.extensions.textual.wordnet.WordNetBridge.POS;
import jcolibri.util.AttributeUtils;
import jcolibri.util.ProgressController;

/**
 * Relates query words to cases words using WordNet. 
 * Words are related if belong to the same synset.
 * <p>
 * Tokens are related using a list of WeightedRelation objects.
 * These relations are stored in each Token instance.
 * <p>
 * First version was developed at: Robert Gordon University - Aberdeen & Facultad Inform�tica,
 * Universidad Complutense de Madrid (GAIA)
 * @author Juan A. Recio-Garcia
 * @version 2.0
 *
 */
public class ThesaurusLinker
{
    /**
     * Performs the algorithm in all the attributes of a collection of cases and a query.
     * These attributes must be IEText objects.
     */  
    public static void linkWithWordNet(Collection<CBRCase> cases, CBRQuery query)
    {
	org.apache.commons.logging.LogFactory.getLog(ThesaurusLinker.class).info("Linking tokens with WordNet.");
	ProgressController.init(ThesaurusLinker.class, "Linking tokens with WordNet ...", cases.size());
	
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
	    	linkWithWordNet(caseText, queryText);
	    }
	    ProgressController.step(ThesaurusLinker.class);
	}
	ProgressController.finish(ThesaurusLinker.class);

    }

    /**
     * Performs the algorithm in the given attributes of a collection of cases and a query.
     * These attributes must be IEText objects.
     */
    public static void linkWithWordNet(Collection<CBRCase> cases, CBRQuery query, Collection<Attribute> attributes)
    {
	org.apache.commons.logging.LogFactory.getLog(ThesaurusLinker.class).info("Linking tokens with WordNet.");
	ProgressController.init(ThesaurusLinker.class, "Linking tokens with WordNet ...", cases.size());
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
		    linkWithWordNet(caseText, queryText);
		} catch (AttributeAccessException e)
		{
		    org.apache.commons.logging.LogFactory.getLog(GlossaryLinker.class).error(e);
		}
	    }
	    ProgressController.step(ThesaurusLinker.class);
	}
	ProgressController.finish(ThesaurusLinker.class);

    }
    
    /**
     * Links two texts using wordNet. It only relates words in the same sysnset.
     */
    public static void linkWithWordNet(IEText caseText, IEText queryText)
    {
        List<Token> queryTokens = queryText.getAllTokens();
        List<Token> caseTokens  = caseText.getAllTokens();
        
   	for(Token queryTok : queryTokens)
        {
   		for(Token caseTok: caseTokens)
   		{
   		    WordNetBridge.POS queryPOS = lookupWordNetPos(queryTok.getPostag());
   		    WordNetBridge.POS casePOS  = lookupWordNetPos(caseTok.getPostag());
   	   	    if(queryPOS != casePOS)
   	   		    continue;
   	   	    if(queryTok.isStopWord())
   	   		    continue;
   	   	    if(caseTok.isStopWord())
   	   		    continue;
   	   	    if(queryTok.getStem().equals(caseTok.getStem()))
   	   		    continue;
   	   	    if(WordNetBridge.sameSynset(queryTok.getRawContent(), queryPOS, caseTok.getRawContent(), casePOS))
   	   	    {
   	   		queryTok.addRelation(new WeightedRelation(queryTok, caseTok, 0.75));
   	   		//org.apache.commons.logging.LogFactory.getLog(ThesaurusLinker.class).info("Adding relation: "+queryTok.getRawContent()+" --> "+caseTok.getRawContent()+". Weight: "+ 0.75);
   	   	    }
   		}
        }
    }
    
    /**
     * Initializes WordNet.
     */
    public static void loadWordNet()
    {
	WordNetBridge.init();
    }
    
    /**
     * This method transforms POS tags defined in PartofSpeechMethod to the
     * tags used in the glossary file
     * 
     * @param tag
     *                POS tag
     * @return NOUN, VERB, ADJECTIVE or ADVERB
     */
    static WordNetBridge.POS lookupWordNetPos(String tag)
    {
	/*
         * 12. NN Noun, singular or mass 13. NNS Noun, plural
         */
	if (tag.equals("NN") || tag.equals("NNS"))
	    return POS.NOUN;
	/*
         * 27. VB Verb, base form 28. VBD Verb, past tense 29. VBG Verb, gerund
         * or present participle 30. VBN Verb, past participle 31. VBP Verb,
         * non-3rd person singular present 32. VBZ Verb, 3rd person singular
         * present
         */
	if (tag.startsWith("V"))
	    return POS.VERB;

	/*
         * 7. JJ Adjective 8. JJR Adjective, comparative 9. JJS Adjective,
         * superlative
         */
	if (tag.startsWith("J"))
	    return POS.ADJECTIVE;

	/*
         * 20. RB Adverb 21. RBR Adverb, comparative 22. RBS Adverb, superlative
         */
	if (tag.startsWith("RB"))
	    return POS.ADVERB;

	return null;
    }

}
