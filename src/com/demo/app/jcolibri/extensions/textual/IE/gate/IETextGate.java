/**
 * IETextGate.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 21/06/2007
 */
package com.demo.app.jcolibri.extensions.textual.IE.gate;

import gate.Annotation;
import gate.DocumentFormat;
import gate.Factory;

import java.util.Hashtable;

import jcolibri.extensions.textual.IE.gate.gateinit.InitGate;
import jcolibri.extensions.textual.IE.representation.IEText;
import jcolibri.extensions.textual.IE.representation.Paragraph;
import jcolibri.extensions.textual.IE.representation.Sentence;
import jcolibri.extensions.textual.IE.representation.Token;

/*
 * Represents an IEText implemented using the GATE package.
 * It stores internally a gate Document object, so this class is a 
 * wrapper that implements the IEText superclass.
 * <br>
 * GATE organizes documents using annotations. Annotations have a label and keep any other information. 
 * They also have their begin and end position of the annotation within the text to obtain the text fragment that refer to.
 * 
 * @author Juan A. Recio-Garcia
 * @version 1.0
 * @see jcolibri.extensions.textual.IE.representation.IEText
 */
public class IETextGate extends IEText
{
    
    protected gate.Document doc;

    /**
     * Creates an empty IETextGate object. 
     * Initializes GATE if required.
     */
    public IETextGate()
    {
	InitGate.initGate();
    }
    
    /**
     * Creates an IETextGate object with the given content.
     * Initializes GATE if required.
     */
    public IETextGate(String content)
    {
	InitGate.initGate();
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
	doc = Factory.newDocument(content);
	DocumentFormat df = DocumentFormat.getDocumentFormat(doc, "text");
	df.unpackMarkup(doc);
    }

    /**
     * Returns the internal gate's document.
     */
    protected gate.Document getDocument()
    {
	return doc;
    }
    
    //Internal mapping between paragraphs and the paragraphs annotations in the gate's document
    private Hashtable<Paragraph,Annotation> parMapping = new Hashtable<Paragraph,Annotation>();
    //Internal mapping between sentences and the sentences annotations in the gate's document
    private Hashtable<Sentence,Annotation> sentMapping = new Hashtable<Sentence,Annotation>();
    //Internal mapping between tokens and the tokens annotations in the gate's document
    private Hashtable<Token,Annotation>     tokMapping = new Hashtable<Token,Annotation>();
    
    /**
     * Returns the annotation object for a given paragraph
     */
    protected Annotation getParagraphMapping(Paragraph par)
    {
	return parMapping.get(par);
    }
    
    /**
     * Sets the annotation object for a given paragraph
     */
    protected void setParagraphMapping(Paragraph par, Annotation annot)
    {
	parMapping.put(par, annot);
    }
    
    /**
     * Returns the annotation object for a given sentence
     */
    protected Annotation getSentenceMapping(Sentence sent)
    {
	return sentMapping.get(sent);
    }
    
    /**
     * Sets the annotation object for a given sentence
     */
    protected void setSentenceMapping(Sentence sent, Annotation annot)
    {
	sentMapping.put(sent, annot);
    }
    
    /**
     * Returns the annotation object for a given token
     */
    protected Annotation getTokenMapping(Token tok)
    {
	return tokMapping.get(tok);
    }
    
    /**
     * Sets the annotation object for a given token
     */
    protected void setTokenMapping(Token tok, Annotation annot)
    {
	tokMapping.put(tok, annot);
    }
    
    

}
