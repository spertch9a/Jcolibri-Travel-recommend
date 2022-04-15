/**
 * IDIndexedCaseBase.java
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

/**
 * This is a modification of LinealCaseBase that also keeps an index of cases using their IDs. 
 * Internally it uses a hash table that relates each ID with its corresponding case.
 * It adds the method: getCase(Object ID)
 * 
 * @author Juan A. Recio-Garc�a
 *
 */
public abstract class IDIndexedLinealCaseBase implements CBRCaseBase {

	private Connector connector;
	private Collection<CBRCase> cases;
	private java.util.HashMap<Object, CBRCase> index;


//	private void indexCases(Collection<CBRCase> cases)
//	{
//		index = new HashMap<Object, CBRCase>();
//		for(CBRCase c: cases)
//		{
//			try {
//				Object o = c.getDescription().getIdAttribute().getValue(c.getDescription());
//				index.put(o, c);
//			} catch (AttributeAccessException e) { }
//		}
//	}
	
	/* (non-Javadoc)
	 * @see jcolibri.cbrcore.CBRCaseBase#init()
	 */
	public void init(Connector connector) {
		this.connector = connector;
		cases = this.connector.retrieveAllCases();	
		//indexCases(cases);
			
	}
	
	/* (non-Javadoc)
	 * @see jcolibri.cbrcore.CBRCaseBase#close()
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

	public void learnCases() {

	}


	/* (non-Javadoc)
	 * @see jcolibri.cbrcore.CBRCaseBase#learnCases(java.util.Collection)
	 */
//	public void learnCases(Collection<CBRCase> cases) {
//		//connector.storeCases(cases);
//		indexCases(cases);
//		this.cases.addAll(cases);
//
//	}

	/**
	 * Returns the case that corresponds with the id parameter.
	 */
	public CBRCase getCase(Object id)
	{
		return index.get(id);
	}

}
