package com.demo.jcolibri.method.maintenance;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRQuery;

/**
 * Stores the query result information. It contains a <query, result> pair.
 * The result is some double value related to the query. 
 *
 * @author Lisa Cummins
 */
public class QueryResult implements Comparable
{
	protected CBRQuery _case;
	protected double result;
	
	/**
	 * Sets up a <query, result> pair.
	 * @param _case The query case to be stored
	 * @param result The result associated with this case.
	 */
	public QueryResult(CBRQuery _case, double result)
	{	this._case = _case;
		this.result = result;
	}

	/**
	 * Returns the case.
	 * @return the case.
	 */
	public CBRQuery getCase() 
	{	return _case;
	}

	/**
	 * Set the given case to be the query case
	 * associated with this <query, result> pair.
	 * @param _case The case to set.
	 */
	public void setCase(CBRCase _case) 
	{	this._case = _case;
	}

	/**
	 * Returns the result.
	 * @return the result.
	 */
	public double getResult() 
	{	return result;
	}

	/**
	 * Set the given result to be the result
	 * associated with this <query, result> pair.
	 * @param result The result to set.
	 */
	public void setEval(double result) {
		this.result = result;
	}
	
	/**
	 * Returns a String representation of this object. 
	 * @return a String representation of this object. 
	 */
	public String toString()
	{	return _case + " -> "+ result;
	}

	/**
	 * Returns the result of comparing this object to 
	 * the given object.
	 * This returns 0 if the object is not of the same class
	 * as this object, -1 if the result of this object
	 * is less than the result of the given object, 0 if
	 * the result of this object is equal to the result of the 
	 * given object and 1 if the result of this object is 
	 * greater than the result of the given object.
	 * @return the result of comparing this object to 
	 * the given object.
	 */
	public int compareTo(Object o) 
	{	if(!(o instanceof QueryResult))
			return 0;
		
		QueryResult other = (QueryResult)o;
		if(other.getResult() > result)
			return -1;
		else if(other.getResult() < result)
			return 1;
		else
			return 0;
	}
	
	/**
	 * Sorts the given list of CaseResults in the given order and
	 * returns the sorted list. 
	 * @param ascending The order in which to sort the elements. 
	 * @param toSort The list of CaseResults to sort.
	 * @return the sorted list.
	 */
	@SuppressWarnings("unchecked")
	public static List sortResults(boolean ascending, List<QueryResult> toSort)
	{   Collections.sort(toSort);
	    if(ascending)
	    {	return toSort;	    	    
	    }
	    List<QueryResult> sorted = new LinkedList<QueryResult>();
	    for(QueryResult res: toSort)
	    {	sorted.add(0, res);	    	    
	    }
	    return sorted;
	}
}