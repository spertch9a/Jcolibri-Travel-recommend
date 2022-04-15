/**
 * StoreCasesMethod.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 05/01/2007
 */
package com.demo.jcolibri.method.retain;

import java.util.ArrayList;
import java.util.Collection;

import com.demo.jcolibri.cbrcore.CBRCase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCaseBase;


/**
 * Stores cases in the case base.
 * @author Juan A. Recio-Garcia
 *
 */
public class StoreCasesMethod {

	
	/**
	 * Simple method that adds some cases to the case base invoking caseBase->learnCases().
	 */
	//public static void storeCases(CBRCaseBase caseBase, Collection<CBRCase> cases)
//	{
//		caseBase.learnCases(caseBase);
//	}
	
	/**
	 * Simple method that add a case to the case base invoking caseBase->learnCases().
	 */
	public static void storeCase(CBRCaseBase caseBase, CBRCase _case)
	{
		Collection<CBRCase> cases = new ArrayList<CBRCase>();
		cases.add(_case);
		//caseBase.learnCases(cases);
	}

}
