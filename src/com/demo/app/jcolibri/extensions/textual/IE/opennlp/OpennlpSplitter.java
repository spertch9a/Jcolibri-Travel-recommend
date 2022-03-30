/**
 * OpennlpSplitter.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 21/06/2007
 */

package com.demo.app.jcolibri.extensions.textual.IE.opennlp;

import java.util.Collection;
import java.util.List;

import org.jdom.Element;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.extensions.textual.IE.IEutils;
import jcolibri.extensions.textual.IE.gate.GateSplitter;
import jcolibri.extensions.textual.IE.representation.IEText;
import jcolibri.extensions.textual.IE.representation.Paragraph;
import jcolibri.extensions.textual.IE.representation.Sentence;
import jcolibri.extensions.textual.IE.representation.Token;
import jcolibri.util.AttributeUtils;
import jcolibri.util.ProgressController;
import opennlp.common.xml.NLPDocument;
import opennlp.grok.preprocess.sentdetect.EnglishSentenceDetectorME;
import opennlp.grok.preprocess.tokenize.EnglishTokenizerME;
import opennlp.grok.preprocess.tokenize.TokenizerME;

/**
 * Organizes an IETextOpenNLP object in paragraphs, sentences and tokens.
 * This implementation uses maximum entropy algorithms to obtain sentences and tokens.
 * @author Juan A. Recio-Garcia
 * @version 1.0
 *
 */
public class OpennlpSplitter
{    
    /**
     * Performs the algorithm in the given attributes of a collection of cases.
     * These attributes must be IETextOpenNLP objects.
     */
    public static void split(Collection<CBRCase> cases, Collection<Attribute> attributes)
    {
	org.apache.commons.logging.LogFactory.getLog(OpennlpSplitter.class).info("Splitting OpenNLP text.");
	ProgressController.init(OpennlpSplitter.class, "Splitting OpenNLP text", cases.size());
	for(CBRCase c: cases)
	{
	    for(Attribute a: attributes)
	    {
		Object o = AttributeUtils.findValue(a, c);
		if(o instanceof IETextOpenNLP)
		    split((IETextOpenNLP)o);
	    }
	    ProgressController.step(OpennlpSplitter.class);
	}
	ProgressController.finish(OpennlpSplitter.class);
    }

    /**
     * Performs the algorithm in the given attributes of a query.
     * These attributes must be IETextOpenNLP objects.
     */
    public static void split(CBRQuery query, Collection<Attribute> attributes)
    {
	org.apache.commons.logging.LogFactory.getLog(OpennlpSplitter.class).info("Splitting OpenNLP text.");
	    for(Attribute a: attributes)
	    {
		Object o = AttributeUtils.findValue(a, query);
		if(o instanceof IETextOpenNLP)
		    split((IETextOpenNLP)o);
	    }
    }
    
    /**
     * Performs the algorithm in all the IETextOpenNLP typed attributes of a collection of cases.
     */
    public static void split(Collection<CBRCase> cases)
    {
	org.apache.commons.logging.LogFactory.getLog(OpennlpSplitter.class).info("Splitting OpenNLP text.");
	ProgressController.init(OpennlpSplitter.class, "Splitting OpenNLP text", cases.size());
	for(CBRCase c: cases)
	{
	    Collection<IEText> texts = IEutils.getTexts(c);
	    for(IEText t : texts)
		if(t instanceof IETextOpenNLP)
		    split((IETextOpenNLP)t);
	    ProgressController.step(OpennlpSplitter.class);
	}
	ProgressController.finish(OpennlpSplitter.class);
    }
    
    /**
     * Performs the algorithm in all the IETextOpenNLP typed attributes of a query.
     */ 
    public static void split(CBRQuery query)
    {	 
	org.apache.commons.logging.LogFactory.getLog(OpennlpSplitter.class).info("Splitting OpenNLP text.");
	Collection<IEText> texts = IEutils.getTexts(query);
        for(IEText t : texts)
            if(t instanceof IETextOpenNLP)
        	split((IETextOpenNLP)t);
    }
    
    
    
    public static void split(IETextOpenNLP text)
    {
	try
	{
	    TokenizerME tokeniser = getTokeniser();
	    tokeniser.process(text.getDocument());
	    
	    EnglishSentenceDetectorME sd = getSentenceDetector();
	    sd.process(text.getDocument());
	    
	    organizeText(text);
	    
	} catch (Exception e)
	{
	    org.apache.commons.logging.LogFactory.getLog(GateSplitter.class).error(e);   
	}
    }
    
    /**
     * Performs the algorithm in a given IETextOpenNLP object
     */
    @SuppressWarnings("unchecked")
    protected static void organizeText(IETextOpenNLP text)
    {
	NLPDocument doc = text.getDocument();
	
	Element root = doc.getRootElement();
	Element texte = (Element)root.getChild("text");
	List<Element> pars = texte.getChildren();
	String[] parsText  = doc.getParagraphs();
	for(int p = 0; p<parsText.length; p++)
	{
	    Element par = pars.get(p);
	    String parText = parsText[p];
	   
	    Paragraph myPar= new Paragraph(parText);
	    text.setParagraphMapping(myPar, par);
	    text.addParagraph(myPar);
	    
	    
	    List<Element> sents = par.getChildren();
	    String[] sentsText = doc.getSentences(par);
	    for(int s=0; s<sentsText.length; s++)
	    {
		Element sent = sents.get(s);
		String sentText = sentsText[s];
		
		Sentence mySent = new Sentence(sentText);
		myPar.addSentence(mySent);
		text.setSentenceMapping(mySent, sent);
		
		List<Element> toks = sent.getChildren();
		String[] toksText =  doc.getWords(sent);
		for(int t=0; t<toksText.length; t++)
		{
		    Element tok = toks.get(t);
		    String tokText = toksText[t];
		    
		    Token myTok = new Token(tokText);
		    mySent.addToken(myTok);
		    text.setTokenMapping(myTok, tok);
		}
	    }
	}
	
	
    }
    
    
    private static TokenizerME tokeniser = null;
    private static TokenizerME getTokeniser() throws Exception
    {
	if(tokeniser == null)
	    tokeniser = new EnglishTokenizerME();
	return tokeniser;
    }
    
    private static EnglishSentenceDetectorME englishSentenceDetector = null;
    private static EnglishSentenceDetectorME getSentenceDetector()
    {
	if(englishSentenceDetector == null)
	    englishSentenceDetector = new EnglishSentenceDetectorME();
	return englishSentenceDetector;
    }
    
    
}
