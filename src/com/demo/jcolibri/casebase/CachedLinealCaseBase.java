/**
 * CachedLinealCaseBase.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 03/05/2007
 */

package com.demo.jcolibri.casebase;



import com.demo.jcolibri.cbrcore.CBRCase;
import com.demo.jcolibri.cbrcore.CBRCaseBase;
import com.demo.jcolibri.cbrcore.CaseBaseFilter;
import com.demo.jcolibri.cbrcore.Connector;
import es.ucm.fdi.gaia.jcolibri.exception.InitializingException;

import java.util.ArrayList;
import java.util.Collection;


public abstract class CachedLinealCaseBase implements CBRCaseBase {

	private Connector connector;
	private Collection<CBRCase> originalCases;
	private Collection<CBRCase> workingCases;
	
	/**
	 * Closes the case base saving or deleting the cases of the persistence media
	 */
	public void close() {
		java.util.ArrayList<CBRCase> casestoRemove = new java.util.ArrayList<CBRCase>((Collection<? extends CBRCase>) originalCases);
		casestoRemove.removeAll(workingCases);
		org.apache.commons.logging.LogFactory.getLog(this.getClass()).info("Deleting "+casestoRemove.size()+" cases from storage media");
		connector.deleteCases(casestoRemove);
		
		Collection<CBRCase> casestoStore = new ArrayList<CBRCase>(workingCases);
		casestoStore.removeAll(originalCases);
		org.apache.commons.logging.LogFactory.getLog(this.getClass()).info("Storing "+casestoStore.size()+" cases into storage media");
		
		connector.storeCases(casestoStore);
		
		connector.close();

	}

	/**
	 * Forgets cases. It only removes the cases from the storage media when closing.
	 */
	public void forgetCases(Collection<CBRCase> cases) {
		workingCases.removeAll(cases);

	}

	/**
	 * Returns working cases.
	 * @return
	 */
	public Collection<CBRCase> getCases() {
		return workingCases;
	}

	/**
	 * TODO.
	 * @return
	 */
	public Collection<CBRCase> getCases(CaseBaseFilter filter) {
		// TODO
		return null;
	}

//	public void learnCases(Collection<CBRCase> cases) {
//
//	}

	/**
	 * Initializes the Case Base with the cases read from the given connector.
	 */
	public void init(Connector connector) throws InitializingException {
		this.connector = connector;
		originalCases = this.connector.retrieveAllCases();	
		workingCases = new java.util.ArrayList<CBRCase>((Collection<? extends CBRCase>) originalCases);
	}

	/**
	 * Learns cases that are only saved when closing the Case Base.
	 */
//	public void learnCases(Collection<CBRCase> cases) {
	//	workingCases.addAll(cases);

	}


