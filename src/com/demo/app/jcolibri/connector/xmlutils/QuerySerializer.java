/**
 * QuerySerializer.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 04/11/2007
 */
package com.demo.app.jcolibri.connector.xmlutils;

import java.io.FileWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import jcolibri.cbrcore.CBRQuery;
import jcolibri.util.FileIO;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Utility class to serialize queries into xml files.
 * @author Juan A. Recio-Garcia
 * @version 1.0
 *
 */
public class QuerySerializer
{
    public static void serializeQuery(CBRQuery query, String filename)
    {
	    try
	    {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.newDocument();

		Element root = doc.createElement("CBRQuery");
		String id = "null";
		Object idObject = query.getID();
		if(idObject != null)
		    id = idObject.toString();
		root.setAttribute("Id", id);
		root.appendChild(CaseComponentSerializer.serializeCaseComponent(query.getDescription(),"Description",doc));
		
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer trans = tf.newTransformer();
		FileWriter fw = new FileWriter(filename);
		trans.transform(new DOMSource(root), new StreamResult(fw));
	    } catch (Exception e)
	    {
		org.apache.commons.logging.LogFactory.getLog(CaseComponentSerializer.class).error(e);
		
	    }
    }
    
    public static CBRQuery deserializeQuery(String filename)
    {
	CBRQuery query = new CBRQuery();
	
	try
	{
	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    DocumentBuilder db = dbf.newDocumentBuilder();
	    Document doc = db.parse(FileIO.openFile(filename));
	    
	    Node node = doc.getElementsByTagName("CaseComponent").item(0);
	    query.setDescription(CaseComponentSerializer.deserializeCaseComponent(node));
	    
	} catch (Exception e)
	{
	    org.apache.commons.logging.LogFactory.getLog(CaseComponentSerializer.class).error(e);
	    
	}
	
	return query;
    }
}
