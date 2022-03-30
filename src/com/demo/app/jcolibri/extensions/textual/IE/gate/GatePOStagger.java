/**
 * GatePOStagger.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 20/06/2007
 */
package com.demo.app.jcolibri.extensions.textual.IE.gate;

import java.util.Collection;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Factory;
import gate.creole.POSTagger;
import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.extensions.textual.IE.IEutils;
import jcolibri.extensions.textual.IE.representation.IEText;
import jcolibri.extensions.textual.IE.representation.Token;
import jcolibri.util.AttributeUtils;
import jcolibri.util.ProgressController;

/**
 * Performs the POS tagging using the GATE algorithm.
 * <br>
 * Part-Of-Speech tags (the original GATE set):
 * <ul>
 * <li>CC - coordinating conjunction: �and�, �but�, �nor�, �or�, �yet�, plus,
 * minus, less, times (multiplication), over (division). Also �for� (because)
 * and �so� (i.e., �so that�).
 * <li>CD - cardinal number
 * <li>DT - determiner: Articles including �a�, �an�, �every�, �no�, �the�,
 * �another�, �any�, �some�, �those�.
 * <li>EX - existential there: Unstressed �there� that triggers inversion of
 * the inflected verb and the logical subject; �There was a party in progress�.
 * <li>FW - foreign word
 * <li>IN - preposition or subordinating conjunction
 * <li>JJ - adjective: Hyphenated compounds that are used as modifiers;
 * happy-go-lucky.
 * <li>JJR - adjective - comparative: Adjectives with the comparative ending
 * �-er� and a comparative meaning. Sometimes �more� and �less�.
 * <li>JJS - adjective - superlative: Adjectives with the superlative ending
 * �-est� (and �worst�). Sometimes �most�and �least�.
 * <li>JJSS - -unknown-, but probably a variant of JJS
 * <li>-LRB- - -unknown-
 * <li>LS - list item marker: Numbers and letters used as identifiers of items
 * in a list.
 * <li>MD - modal: All verbs that don�t take an �-s� ending in the third person
 * singular present: �can�, �could�, �dare�, �may�, �might�, �must�, �ought�,
 * �shall�, �should�, �will�, �would�.
 * <li>NN - noun - singular or mass
 * <li>NNP - proper noun - singular: All words in names usually are capitalized
 * but titles might not be.
 * <li>NNPS - proper noun - plural: All words in names usually are capitalized
 * but titles might not be.
 * <li>NNS - noun - plural
 * <li>NP - proper noun - singular
 * <li>ML Configuration 283
 * <li>NPS - proper noun - plural
 * <li>PDT - predeterminer: Determinerlike elements preceding an article or
 * possessive pronoun;
 * <li>�all/PDT his marbles�, �quite/PDT a mess�.
 * <li>POS - possesive ending: Nouns ending in ��s� or ���.
 * <li>PP - personal pronoun
 * <li>PRPR$ - unknown-, but probably possessive pronoun
 * <li>PRP - unknown-, but probably possessive pronoun
 * <li>PRP$ - unknown, but probably possessive pronoun,such as �my�, �your�,
 * �his�, �his�, �its�, �one�s�, �our�, and �their�.
 * <li>RB - adverb: most words ending in �-ly�. Also �quite�, �too�, �very�,
 * �enough�, �indeed�, �not�, �-n�t�, and �never�.
 * <li>RBR - adverb - comparative: adverbs ending with �-er� with a comparative
 * meaning.
 * <li>RBS - adverb - superlative
 * <li>RP - particle: Mostly monosyllabic words that also double as directional
 * adverbs.
 * <li>STAART - start state marker (used internally)
 * <li>SYM - symbol: technical symbols or expressions that aren�t English
 * words.
 * <li>TO - literal to
 * <li>UH - interjection: Such as �my�, �oh�, �please�, �uh�, �well�, �yes�.
 * <li>VBD - verb - past tense: includes conditional form of the verb �to be�;
 * �If I were/VBD rich...�.
 * <li>VBG - verb - gerund or present participle
 * <li>VBN - verb - past participle
 * <li>VBP - verb - non-3rd person singular present
 * <li>VB - verb - base form: subsumes imperatives, infinitives and
 * subjunctives.
 * <li>VBZ - verb - 3rd person singular present
 * <li>WDT - wh-determiner
 * <li>WP$ - possesive wh-pronoun: includes �whose�
 * <li>WP - wh-pronoun: includes �what�, �who�, and �whom�.
 * <li>WRB - wh-adverb: includes �how�, �where�, �why�. Includes �when� when
 * used in a temporal sense.
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
 * @version 1.0
 * 
 */
public class GatePOStagger
{
    
    /**
     * Performs the algorithm in the given attributes of a collection of cases.
     * These attributes must be IETextGate objects.
     */
    public static void tag(Collection<CBRCase> cases, Collection<Attribute> attributes)
    {
	org.apache.commons.logging.LogFactory.getLog(GatePOStagger.class).info("Gate POS tagging...");
	ProgressController.init(GatePOStagger.class, "Gate POS tagging...", cases.size());
	for (CBRCase c : cases)
	{
	    for (Attribute a : attributes)
	    {
		Object o = AttributeUtils.findValue(a, c);
		if (o instanceof IETextGate)
		    tag((IETextGate) o);
	    }
	    ProgressController.step(GatePOStagger.class);
	}
	ProgressController.finish(GatePOStagger.class);
    }

    /**
     * Performs the algorithm in the given attributes of a query.
     * These attributes must be IETextGate objects.
     */
    public static void tag(CBRQuery query, Collection<Attribute> attributes)
    {
	org.apache.commons.logging.LogFactory.getLog(GatePOStagger.class).info("Gate POS tagging...");
	for (Attribute a : attributes)
	{
	    Object o = AttributeUtils.findValue(a, query);
	    if (o instanceof IETextGate)
		tag((IETextGate) o);
	}
    }
    
    /**
     * Performs the algorithm in all the IETextGate typed attributes of a collection of cases.
     */ 
    public static void tag(Collection<CBRCase> cases)
    {
	org.apache.commons.logging.LogFactory.getLog(GatePOStagger.class).info("Gate POS tagging...");
	ProgressController.init(GatePOStagger.class, "Gate POS tagging...", cases.size());
	for (CBRCase c : cases)
	{
	    Collection<IEText> texts = IEutils.getTexts(c);
	    for (IEText t : texts)
		if (t instanceof IETextGate)
		    tag((IETextGate) t);
	    ProgressController.step(GatePOStagger.class);
	}
	ProgressController.finish(GatePOStagger.class);
    }
    
    /**
     * Performs the algorithm in all the IETextGate typed attributes of a query.
     */
    public static void tag(CBRQuery query)
    {
	org.apache.commons.logging.LogFactory.getLog(GatePOStagger.class).info("Gate POS tagging...");
	Collection<IEText> texts = IEutils.getTexts(query);
	for (IEText t : texts)
	    if (t instanceof IETextGate)
		tag((IETextGate) t);
    }

    /**
     * Performs the algorithm in a given IETextGate object
     */
    public static void tag(IETextGate text)
    {
	try
	{
	    POSTagger tagger = getTokeniser();
	    tagger.setDocument(text.getDocument());
	    tagger.execute();

	    AnnotationSet posAnnotations = text.getDocument().getAnnotations()
		    .get("POS");

	    for (Token t : text.getAllTokens())
	    {
		Annotation anotToken = text.getTokenMapping(t);
		AnnotationSet posAnnots = posAnnotations.get(anotToken
			.getStartNode().getOffset(), anotToken.getEndNode()
			.getOffset());
		Annotation anot = (Annotation) posAnnots.iterator().next();
		String postag = (String) anot.getFeatures().get("category");
		t.setPostag(postag);
	    }

	} catch (Exception e)
	{
	    org.apache.commons.logging.LogFactory.getLog(GatePOStagger.class)
		    .error(e);

	}
    }

    private static POSTagger tagger = null;

    private static POSTagger getTokeniser() throws Exception
    {
	if (tagger == null)
	{
	    tagger = (POSTagger) Factory
		    .createResource("gate.creole.POSTagger");
	    tagger.setBaseSentenceAnnotationType("Sentence");
	    tagger.setBaseTokenAnnotationType("Token");
	    tagger.setOutputAnnotationType("POS");
	    tagger.init();
	}
	return tagger;
    }
}
