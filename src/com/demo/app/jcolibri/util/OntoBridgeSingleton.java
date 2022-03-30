/**
 * OntoBridgeSingleton.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 04/06/2007
 */
package com.demo.app.jcolibri.util;

import es.ucm.fdi.gaia.ontobridge.OntoBridge;

/**
 * Singleton for the OntoBridge library
 * @author Juan A. Recio-Garcia
 *
 */
public class OntoBridgeSingleton {

	private static OntoBridge _instance=null;
	public static OntoBridge  getOntoBridge()
	{
		if(_instance == null)
			_instance = new OntoBridge();
		return _instance;
	}
}
