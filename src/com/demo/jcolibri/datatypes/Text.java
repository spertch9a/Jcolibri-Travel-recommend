/**
 * 
 */
package com.demo.jcolibri.datatypes;

import jcolibri.connector.TypeAdaptor;

/**
 * @author Juanan
 *
 */
public class Text implements TypeAdaptor {

	protected String rawContent;
	
	public Text()
	{
	    
	}
	
	public Text(String content)
	{
	    rawContent = content;
	}
	
	/* (non-Javadoc)
	 * @see jcolibri.connector.TypeAdaptor#fromString(java.lang.String)
	 */
	public void fromString(String content) throws Exception {
		rawContent = new String(content);
	}
	
	public String toString(){
		return rawContent;
	}

}
