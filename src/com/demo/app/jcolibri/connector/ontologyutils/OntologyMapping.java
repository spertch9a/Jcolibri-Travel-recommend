/**
 * OntologyMapping.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 12/06/2007
 */
package com.demo.app.jcolibri.connector.ontologyutils;

/**
 * Stores the mapping configuration of the connector
 * @author Juan A. Recio-Garcia
 * @version 1.0
 *
 */
public class OntologyMapping {
	private String property;
	private String concept;
	private String attribute;
	/**
	 * @return Returns the attribute.
	 */
	public String getAttribute() {
		return attribute;
	}
	/**
	 * @param attribute The attribute to set.
	 */
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	/**
	 * @return Returns the concept.
	 */
	public String getConcept() {
		return concept;
	}
	/**
	 * @param concept The concept to set.
	 */
	public void setConcept(String concept) {
		this.concept = concept;
	}
	/**
	 * @return Returns the property.
	 */
	public String getProperty() {
		return property;
	}
	/**
	 * @param property The property to set.
	 */
	public void setProperty(String property) {
		this.property = property;
	}
	
}
