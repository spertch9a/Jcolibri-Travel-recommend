/**
 * LinealCaseBase.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 28/11/2006
 */
package com.demo.jcolibri.casebase;



import com.demo.jcolibri.cbrcore.CBRCase;
import com.demo.jcolibri.cbrcore.CBRCaseBase;
import com.demo.jcolibri.cbrcore.CaseBaseFilter;
import com.demo.jcolibri.cbrcore.Connector;

import java.util.Collection;


public abstract class LinealCaseBase implements CBRCaseBase {

	private Connector connector;
	private Collection<CBRCase> cases;

	public void init(Connector connector) {
		this.connector = connector;
		cases = this.connector.retrieveAllCases();	
	}
	
	/* (non-Javadoc)
	 * @see jcolibri.cbrcore.CBRCaseBase#deInit()
	 */
	public void close() {
		this.connector.close();

	}

	/* (non-Javadoc)
	 * @see jcolibri.cbrcore.CBRCaseBase#forgetCases(java.util.Collection)
	 */
	public void forgetCases(Collection<CBRCase> cases) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see jcolibri.cbrcore.CBRCaseBase#getCases()
	 */
	public Collection<CBRCase> getCases() {
		return cases;
	}

	/* (non-Javadoc)
	 * @see jcolibri.cbrcore.CBRCaseBase#getCases(jcolibri.cbrcore.CaseBaseFilter)
	 */
	public Collection<CBRCase> getCases(CaseBaseFilter filter) {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see jcolibri.cbrcore.CBRCaseBase#learnCases(java.util.Collection)
	 */
	public void learnCases(Collection<CBRCase> cases) {
		connector.storeCases(cases);
		this.cases.addAll(cases);

	}


}
