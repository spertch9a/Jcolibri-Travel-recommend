/**
 * CBRCaseBase.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 05/01/2007
 */
package com.demo.jcolibri.cbrcore;

import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CaseBaseFilter;
import jcolibri.cbrcore.Connector;

import java.util.Collection;

/**
 * This interface defines the methods that at least any Case Base must implement
 * to be transparently used by the framework.
 * <p>
 * A Case Base is the in-memory organization of the cases. 
 * Cases are read from the persistence media an loaded into an implementation of this interface.
 * 
 * Further implementations will provide cache mechanisms, optimized organizations, etc.
 * 
 * @author Juan A. Recio-Garc�a
 */
public interface CBRCaseBase {


    /**
     * Initializes the case base. This methods recibes the connector that manages the persistence media.
     *
     */
    public void init(Connector connector) throws jcolibri.exception.InitializingException;
    
    
    /**
     * DeInitializes the case base.
     *
     */
    public void close();
    


	/**
	 * Returns all the cases available on this case base
	 * 
	 * @return all the cases available on this case base
	 */
	public Collection<jcolibri.cbrcore.CBRCase> getCases();
	
	
    /**
     * Returns some cases depending on the filter
     * @param filter a case base filter
     * @return a collection of cases
     */
    public Collection<jcolibri.cbrcore.CBRCase> getCases(CaseBaseFilter filter);


	/**
	 * Adds a collection of new CBRCase objects to the current case base
	 * 
	 * @param cases
	 *            to be added
	 */
	public void learnCases(Collection<jcolibri.cbrcore.CBRCase> cases);

	/**
	 * Removes a collection of new CBRCase objects to the current case base
	 * 
	 * @param cases
	 *            to be removed
	 */
	public void forgetCases(Collection<CBRCase> cases);



}
