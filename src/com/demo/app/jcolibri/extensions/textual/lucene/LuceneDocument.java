/**
 * LuceneDocument.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 10/04/2007
 */
package com.demo.app.jcolibri.extensions.textual.lucene;

import jcolibri.datatypes.Text;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

/**
 * Wraps a Lucene document object. 
 * This wrapper defines an unique ID for each document and allows to add different fields.<br>
 * A Lucene document is divided into several fields. This allows to search only in some of them.
 * 
 * @author Juan A. Recio-Garc�a
 * @version 2.0
 */
public class LuceneDocument {

    Document doc;
    public static String ID_FIELD = "ID";
    
    public LuceneDocument(String docID)
    {
    	doc = new Document();
    	setDocID(docID);
    }
    
    protected Document getInternalDocument()
    {
    	return doc;
    }
    
    
    public void setDocID(String id)
    {
    	doc.add(new Field(ID_FIELD, id, Field.Store.YES, Field.Index.NO));    	
    }
    public String getDocID()
    {
    	return doc.get(ID_FIELD);
    }
    
    public void addContentField(String fieldname, Text content)
    {
    	doc.add(new Field(fieldname, content.toString(), Field.Store.YES, Field.Index.TOKENIZED));
    }
    public String getContentField(String fieldname)
    {
    	return doc.get(fieldname);
    }
}
