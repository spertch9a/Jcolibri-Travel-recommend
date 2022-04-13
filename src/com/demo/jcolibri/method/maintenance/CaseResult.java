package com.demo.jcolibri.method.maintenance;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import jcolibri.cbrcore.CBRCase;
import jcolibri.method.maintenance.QueryResult;

/**
 * Stores the case result information. It contains a <case, result> pair.
 * The result is some double value related to the case. 
 *
 * @author Lisa Cummins
 */
public class CaseResult extends QueryResult
	implements Comparable
{
	/**
	 * Sets up a <case, result> pair.
	 * @param _case The case to be stored
	 * @param result The result associated with this case.
	 */
	public CaseResult(CBRCase _case, double result)
	{   super(_case, result);	
	}

	/**
	 * Returns the case.
	 * @return the case.
	 */
	public CBRCase getCase() 
	{	return (CBRCase)_case;
	}

	/**
	 * Sorts the given list of CaseResults in the given order and
	 * returns the sorted list. 
	 * @param ascending The order in which to sort the elements. 
	 * @param toSort The list of CaseResults to sort.
	 * @return the sorted list.
	 */
	@SuppressWarnings("unchecked")
	public static List sortCaseResults(boolean ascending, List<CaseResult> toSort)
	{   
		Collections.sort(toSort);
	    if(ascending)
	    {	return toSort;	    	    
	    }
	    List<CaseResult> sorted = new LinkedList<CaseResult>();
	    for(CaseResult res: toSort)
	    {	sorted.add(0, res);	    	    
	    }
	    return sorted;
	}
}