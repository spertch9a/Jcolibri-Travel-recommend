/**
 * LuceneIndexCreator.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 12/04/2007
 */
package com.demo.app.jcolibri.method.precycle;

import java.util.ArrayList;
import java.util.Collection;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRCaseBase;
import jcolibri.datatypes.Text;
import jcolibri.extensions.textual.lucene.LuceneDocument;
import jcolibri.extensions.textual.lucene.LuceneIndex;

/**
 * Creates a Lucene index with the text contained in some attributes of a case
 * @author Juan A. Recio-Garcia
 * @version 1.0
 * @see LuceneIndex
 */
public class LuceneIndexCreator {

	/**
	 * Creates a Lucene Index with the text contained in some attributes. The type of that attributes must be "Text".
	 * This method creates a LuceneDocument for each case, and adds a new field for each attribute (recived as parameter). 
	 * The name and content of the Lucene document field is the name and content of the attribute.
	 */
	public static LuceneIndex createLuceneIndex(CBRCaseBase casebase, Collection<Attribute> fields)
	{
		for(Attribute field: fields)
		{	
			Class c = field.getType();
			if(!Text.class.isAssignableFrom(c))
			{
				org.apache.commons.logging.LogFactory.getLog(LuceneIndexCreator.class).error("Field "+field+" is not an jcolibri.datatyps.Text. Aborting Lucene index creation");
				return null;
			}
		}
		
		ArrayList<LuceneDocument> docs = new ArrayList<LuceneDocument>();
		for(CBRCase c: casebase.getCases())
		{
			LuceneDocument ld = new LuceneDocument((String)c.getID());
			for(Attribute field: fields)
				ld.addContentField(field.getName(), (Text)jcolibri.util.AttributeUtils.findValue(field, c));
			docs.add(ld);
		}
		return new LuceneIndex(docs);

	}
	

	/**
	 * Creates a Lucene Index with the text contained in some attributes. The type of that attributes must be "Text".
	 * This method creates a LuceneDocument for each case, and adds a new field for each attribute (recived as parameter). 
	 * The name and content of the Lucene document field is the name and content of the attribute.
	 */
	public static LuceneIndex createLuceneIndex(CBRCaseBase casebase)
	{
	    	CBRCase _case = casebase.getCases().iterator().next();
	    	Collection<Attribute> attributes = new ArrayList<Attribute>();
	    	if(_case.getDescription() != null)
	    	    attributes.addAll(jcolibri.util.AttributeUtils.getAttributes(_case.getDescription(), Text.class));
	    	if(_case.getSolution() != null)
	    	    attributes.addAll(jcolibri.util.AttributeUtils.getAttributes(_case.getSolution(), Text.class));
	    	if(_case.getResult() != null)
	    	    attributes.addAll(jcolibri.util.AttributeUtils.getAttributes(_case.getResult(), Text.class));
	    	if(_case.getJustificationOfSolution() != null)
	    	    attributes.addAll(jcolibri.util.AttributeUtils.getAttributes(_case.getJustificationOfSolution(), Text.class));
	    	

		return createLuceneIndex(casebase, attributes);

	}	
	
}
