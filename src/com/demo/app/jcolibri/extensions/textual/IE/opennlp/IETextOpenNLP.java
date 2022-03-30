/**
 * IETextOpenNLP.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 21/06/2007
 */
package com.demo.app.jcolibri.extensions.textual.IE.opennlp;

import java.util.Hashtable;

import jcolibri.extensions.textual.IE.representation.IEText;
import jcolibri.extensions.textual.IE.representation.Paragraph;
import jcolibri.extensions.textual.IE.representation.Sentence;
import jcolibri.extensions.textual.IE.representation.Token;

import org.jdom.Element;

/**
 * Represents an IEText implemented using the OpenNLP package.
 * <br>This object uses internally an NLPDocument object (from the OpenNLP package)
 * that is an XML DOM document organized in paragraphs, sentences and tokens.
 * The specific OpenNLP methods will decorate this DOM tree with information,
 * so this class is a wrapper that implements the IEText superclass.
 * 
 * @author Juan A. Recio-Garcia
 * @version 1.0
 * @see IEText
 */
public class IETextOpenNLP extends IEText
{

    protected opennlp.common.xml.NLPDocument doc;

    /**
     * Creates an empty IETextOpenNLP object
     */
    public IETextOpenNLP()
    {
	
    }
    
    /**
     * Creates an IETextOpenNLP object with the given text
     */
    public IETextOpenNLP(String content)
    {
	try
	{
	    fromString(content);
	} catch (Exception e)
	{
	    org.apache.commons.logging.LogFactory.getLog(this.getClass()).error(e);
	}
    }
    
    /**
     * Stores the given text in the object
     */
    public void fromString(String content) throws Exception
    {
	super.fromString(content);
	opennlp.common.xml.NLPDocumentBuilder builder = new opennlp.common.xml.NLPDocumentBuilder();
	doc = builder.build(content);
    }

    /**
     * Returns the internal OpenNLP object that stores the text.
     */
    public opennlp.common.xml.NLPDocument getDocument()
    {
	return this.doc;
    }

    //Internal mapping between paragraphs and the paragraphs nodes in the DOM tree
    private Hashtable<Paragraph, Element> parMapping = new Hashtable<Paragraph, Element>();

    //Internal mapping between sentences and the sentences nodes in the DOM tree
    private Hashtable<Sentence, Element> sentMapping = new Hashtable<Sentence, Element>();

    //Internal mapping between tokens and the tokens nodes in the DOM tree
    private Hashtable<Token, Element> tokMapping = new Hashtable<Token, Element>();

    /**
     * Returns a mapping between a paragraph and the paragraph node in the DOM tree
     */
    protected Element getParagraphMapping(Paragraph par)
    {
	return parMapping.get(par);
    }
    
    /**
     * Sets a mapping between a paragraph and the paragraph node in the DOM tree
     */
    protected void setParagraphMapping(Paragraph par, Element annot)
    {
	parMapping.put(par, annot);
    }

    /**
     * Returns a mapping between a sentence and the sentence node in the DOM tree
     */
    protected Element getSentenceMapping(Sentence sent)
    {
	return sentMapping.get(sent);
    }

    /**
     * Sets a mapping between a sentence and the sentence node in the DOM tree
     */
    protected void setSentenceMapping(Sentence sent, Element annot)
    {
	sentMapping.put(sent, annot);
    }

    /**
     * Returns a mapping between a token and the token node in the DOM tree
     */
    protected Element getTokenMapping(Token tok)
    {
	return tokMapping.get(tok);
    }
    
    /**
     * Sets a mapping between a token and the token node in the DOM tree
     */
    protected void setTokenMapping(Token tok, Element annot)
    {
	tokMapping.put(tok, annot);
    }
    
}
