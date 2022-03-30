/**
 * OpennlpPOStagger.java
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
import opennlp.grok.preprocess.postag.EnglishPOSTaggerME;

import org.jdom.Element;

/**
 * Performs the POS tagging using a OpenNLP Maximum Entropy algorithm. This algorithm uses the same tags than GATE.
 * <br>
 * Part-Of-Speech tags (the original GATE set):
 * <ul>
 * <li>CC - coordinating conjunction: �and�, �but�, �nor�, �or�, �yet�, plus, minus, less, times (multiplication), over (division). Also �for� (because) and �so� (i.e., �so that�).
 * <li>CD - cardinal number
 * <li>DT - determiner: Articles including �a�, �an�, �every�, �no�, �the�, �another�, �any�, �some�, �those�.
 * <li>EX - existential there: Unstressed �there� that triggers inversion of the inflected verb and the logical subject; �There was a party in progress�.
 * <li>FW - foreign word
 * <li>IN - preposition or subordinating conjunction
 * <li>JJ - adjective: Hyphenated compounds that are used as modifiers; happy-go-lucky.
 * <li>JJR - adjective - comparative: Adjectives with the comparative ending �-er� and a comparative meaning. Sometimes �more� and �less�.
 * <li>JJS - adjective - superlative: Adjectives with the superlative ending �-est� (and �worst�). Sometimes �most�and �least�.
 * <li>JJSS - -unknown-, but probably a variant of JJS
 * <li>-LRB- - -unknown-
 * <li>LS - list item marker: Numbers and letters used as identifiers of items in a list.
 * <li>MD - modal: All verbs that don�t take an �-s� ending in the third person singular present: �can�, �could�, �dare�, �may�, �might�, �must�, �ought�, �shall�, �should�, �will�, �would�.
 * <li>NN - noun - singular or mass
 * <li>NNP - proper noun - singular: All words in names usually are capitalized but titles might not be.
 * <li>NNPS - proper noun - plural: All words in names usually are capitalized but titles might not be.
 * <li>NNS - noun - plural
 * <li>NP - proper noun - singular
 * <li>ML Configuration 283
 * <li>NPS - proper noun - plural
 * <li>PDT - predeterminer: Determinerlike elements preceding an article or possessive pronoun;
 * <li>�all/PDT his marbles�, �quite/PDT a mess�.
 * <li>POS - possesive ending: Nouns ending in ��s� or ���.
 * <li>PP - personal pronoun
 * <li>PRPR$ - unknown-, but probably possessive pronoun
 * <li>PRP - unknown-, but probably possessive pronoun
 * <li>PRP$ - unknown, but probably possessive pronoun,such as �my�, �your�, �his�, �his�, �its�, �one�s�, �our�, and �their�.
 * <li>RB - adverb: most words ending in �-ly�. Also �quite�, �too�, �very�, �enough�, �indeed�, �not�, �-n�t�, and �never�.
 * <li>RBR - adverb - comparative: adverbs ending with �-er� with a comparative meaning.
 * <li>RBS - adverb - superlative
 * <li>RP - particle: Mostly monosyllabic words that also double as directional adverbs.
 * <li>STAART - start state marker (used internally)
 * <li>SYM - symbol: technical symbols or expressions that aren�t English words.
 * <li>TO - literal to
 * <li>UH - interjection: Such as �my�, �oh�, �please�, �uh�, �well�, �yes�.
 * <li>VBD - verb - past tense: includes conditional form of the verb �to be�; �If I were/VBD rich...�.
 * <li>VBG - verb - gerund or present participle
 * <li>VBN - verb - past participle
 * <li>VBP - verb - non-3rd person singular present
 * <li>VB - verb - base form: subsumes imperatives, infinitives and subjunctives.
 * <li>VBZ - verb - 3rd person singular present
 * <li>WDT - wh-determiner
 * <li>WP$ - possesive wh-pronoun: includes �whose�
 * <li>WP - wh-pronoun: includes �what�, �who�, and �whom�.
 * <li>WRB - wh-adverb: includes �how�, �where�, �why�. Includes �when� when used in a temporal sense.
 * <li>:: - literal colon
 * <li>, - literal comma
 * <li>$ - literal dollar sign
 * <li>- - literal double-dash
 * <li>- literal double quotes
 * <li>- literal grave
 * <li>( - literal left parenthesis
 * <li>. - literal period
 * <li># - literal pound sign
 * <li>) - literal right parenthesis
 * <li>- literal single quote or apostrophe
 * </ul>
 * 
 * @author Juan A. Recio-Garcia
 * @version 2.0
 * 
 */
public class OpennlpPOStagger
{
    
    /**
     * Performs the algorithm in the given attributes of a collection of cases.
     * These attributes must be IETextOpenNLP objects.
     */
    public static void tag(Collection<CBRCase> cases, Collection<Attribute> attributes)
    {
	org.apache.commons.logging.LogFactory.getLog(OpennlpPOStagger.class).info("OpenNLP POS tagging.");
	ProgressController.init(OpennlpPOStagger.class, "OpenNLP POS tagging", cases.size());
	for(CBRCase c: cases)
	{
	    for(Attribute a: attributes)
	    {
		Object o = AttributeUtils.findValue(a, c);
		if(o instanceof IETextOpenNLP)
		    tag((IETextOpenNLP)o);
	    }
	    ProgressController.step(OpennlpPOStagger.class);
	}
	ProgressController.finish(OpennlpPOStagger.class);
    }

    /**
     * Performs the algorithm in the given attributes of a query.
     * These attributes must be IETextOpenNLP objects.
     */
    public static void tag(CBRQuery query, Collection<Attribute> attributes)
    {
	org.apache.commons.logging.LogFactory.getLog(OpennlpPOStagger.class).info("OpenNLP POS tagging.");
	    for(Attribute a: attributes)
	    {
		Object o = AttributeUtils.findValue(a, query);
		if(o instanceof IETextOpenNLP)
		    tag((IETextOpenNLP)o);
	    }
    }
    
    /**
     * Performs the algorithm in all the IETextOpenNLP typed attributes of a collection of cases.
     */
    public static void tag(Collection<CBRCase> cases)
    {
	org.apache.commons.logging.LogFactory.getLog(OpennlpPOStagger.class).info("OpenNLP POS tagging.");
	ProgressController.init(OpennlpPOStagger.class, "OpenNLP POS tagging", cases.size());
	for(CBRCase c: cases)
	{
	    Collection<IEText> texts = IEutils.getTexts(c);
	    for(IEText t : texts)
		if(t instanceof IETextOpenNLP)
		    tag((IETextOpenNLP)t);
	    ProgressController.step(OpennlpPOStagger.class);
	}
	ProgressController.finish(OpennlpPOStagger.class);
    }
    
    /**
     * Performs the algorithm in all the IETextOpenNLP typed attributes of a query.
     */ 
    public static void tag(CBRQuery query)
    {	    
	org.apache.commons.logging.LogFactory.getLog(OpennlpPOStagger.class).info("OpenNLP POS tagging.");
	Collection<IEText> texts = IEutils.getTexts(query);
        for(IEText t : texts)
            if(t instanceof IETextOpenNLP)
        	tag((IETextOpenNLP)t);
    }
    
    /**
     * Performs the algorithm in a given IETextOpenNLP object
     */
    public static void tag(IETextOpenNLP text)
    {
	EnglishPOSTaggerME tagger = getSentenceDetector();
	
	tagger.process(text.getDocument());
	
	for(Token t: text.getAllTokens())
	{
	    Element elem = text.getTokenMapping(t);
	    Element word = elem.getChild("w");
	    String posTag = word.getAttributeValue("pos");
	    t.setPostag(posTag);
	}
    }
    
    
    
    
    private static EnglishPOSTaggerME englishPOStagger = null;
    private static EnglishPOSTaggerME getSentenceDetector()
    {
	if(englishPOStagger == null)
	    englishPOStagger = new EnglishPOSTaggerME();
	return englishPOStagger;
    }
}
