/**
 * OntologyInfo.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 12/06/2007
 */
package com.demo.app.jcolibri.connector.ontologyutils;

/**
 * Stores the ontology configuration of the connector.
 * @author Juan A. Recio-Garcia
 * @version 1.0
 *
 */
public class OntologyInfo {
	private String url;
	private String localCopy;
	/**
	 * @return Returns the localCopy.
	 */
	public String getLocalCopy() {
		return localCopy;
	}
	/**
	 * @param localCopy The localCopy to set.
	 */
	public void setLocalCopy(String localCopy) {
		this.localCopy = localCopy;
	}
	/**
	 * @return Returns the url.
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url The url to set.
	 */
	public void setUrl(String url) {
		this.url = url;
	}
}
