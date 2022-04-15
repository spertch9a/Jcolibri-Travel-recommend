/**
 * StandardCBRApplication.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 03-mar-2006
 */
package com.demo.jcolibri.cbraplications;


import com.demo.jcolibri.cbrcore.CBRCase;
import com.demo.jcolibri.cbrcore.CBRCaseBase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRQuery;
import es.ucm.fdi.gaia.jcolibri.exception.ExecutionException;

public interface StandardCBRApplication
{
	/**
	 * Configures the application: case base, connectors, etc.
	 * @throws ExecutionException
	 */
    public void configure() throws ExecutionException;

    /**
     * Runs the precyle where typically cases are read and organized into a case base. 
     * @return The created case base with the cases in the storage.
     * @throws ExecutionException
     */
    public CBRCaseBase preCycle() throws ExecutionException;

    /**
     * Executes a CBR cycle with the given query.
     * @throws ExecutionException
     * @param query
     */
    public void cycle(CBRCase query) throws ExecutionException;

    /**
     * Runs the code to shutdown the application. Typically it closes the connector.
     * @throws ExecutionException
     */
    public void postCycle() throws ExecutionException;
}
