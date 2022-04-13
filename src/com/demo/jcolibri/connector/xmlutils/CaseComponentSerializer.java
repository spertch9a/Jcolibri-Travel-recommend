/**
 * CaseComponentSerializer.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 04/11/2007
 */
package com.demo.jcolibri.connector.xmlutils;

import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CaseComponent;
import jcolibri.connector.plaintextutils.PlainTextTypeConverter;
import jcolibri.util.AttributeUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Utility class to serialize CaseComponents into xml files.
 * This class will be used in future versions to implement the xml connector.
 * @author Juan A. Recio-Garcia
 * @version 1.0
 *
 */
public class CaseComponentSerializer
{

    
    public static CaseComponent deserializeCaseComponent(Node node)
    {
	    try
	    {
		NamedNodeMap nodemap = node.getAttributes();
		String className = nodemap.getNamedItem("Class").getTextContent();
		CaseComponent cc = (CaseComponent)Class.forName(className).newInstance();
		
		NodeList nl = node.getChildNodes();
		for(int i=0; i<nl.getLength(); i++)
		{
		    Node child = nl.item(i);
		    String nodeName = child.getNodeName();

		    
		    if(nodeName.equals("CaseComponent"))
		    {
			Attribute at = new Attribute(child.getAttributes().getNamedItem("Name").getTextContent(), Class.forName(child.getAttributes().getNamedItem("Class").getTextContent()));
			AttributeUtils.setValue(at, cc, deserializeCaseComponent(child));
		    }else
		    {
			Attribute at = new Attribute(nodeName, cc.getClass());
			if(child.getFirstChild() != null)
			{
			    String value = child.getFirstChild().getTextContent();
			    Object oValue = PlainTextTypeConverter.convert(value, at.getType());
			    AttributeUtils.setValue(at, cc, oValue);
			}
		    } 
		}
		return cc;
	    } catch (Exception e)
	    {
		org.apache.commons.logging.LogFactory.getLog(CaseComponentSerializer.class).error(e);
	    }
	    return null;
    }
    
    public static String serializeCaseComponent(CaseComponent casecomponent, String name)
    {
	try
	{
	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.newDocument();

	    Element root = serializeCaseComponent(casecomponent,name,doc);
	    
	    TransformerFactory tf = TransformerFactory.newInstance();
	    Transformer trans = tf.newTransformer();
	    StringWriter sw = new StringWriter();
	    trans.transform(new DOMSource(root), new StreamResult(sw));
	    
	    
	    return sw.toString();
	} catch (Exception e)
	{
	    org.apache.commons.logging.LogFactory.getLog(CaseComponentSerializer.class).error(e);
	    
	}
	return null;
    }
    
    public static Element serializeCaseComponent(CaseComponent casecomponent, String name, Document doc)
    {
	try
	{	    
	    Element root = doc.createElement("CaseComponent");
	    root.setAttribute("Name", name);
	    root.setAttribute("Class", casecomponent.getClass().getCanonicalName());
	    root.setAttribute("IdAttribute", casecomponent.getIdAttribute().getName());

	    for(Attribute at: AttributeUtils.getAttributes(casecomponent))
	    {
		if(CaseComponent.class.isAssignableFrom(at.getType()))
		    root.appendChild(serializeCaseComponent((CaseComponent)at.getValue(casecomponent),at.getName(),doc));
		else
		{
		    Element child = doc.createElement(at.getName());
		    Object value = at.getValue(casecomponent);
		    if(value!=null)
			child.appendChild(doc.createTextNode(value.toString()));
		    root.appendChild(child);
		}
	    }

	    return root;
	} catch (Exception e)
	{
	    org.apache.commons.logging.LogFactory.getLog(CaseComponentSerializer.class).error(e);
	    
	}
	return null;
    }
}
