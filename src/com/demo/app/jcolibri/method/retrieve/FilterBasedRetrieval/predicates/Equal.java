/**
 * Equal.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 28/10/2007
 */
package com.demo.app.jcolibri.method.retrieve.FilterBasedRetrieval.predicates;

import com.demo.app.jcolibri.exception.NoApplicableFilterPredicateException;
import com.demo.app.jcolibri.method.retrieve.NNretrieval.similarity.local.Interval;


/**
 * Predicate that compares if two objects are equal.
 * @author Juan A. Recio-Garcia
 * @author Developed at University College Cork (Ireland) in collaboration with Derek Bridge.
 * @version 1.0
 * @see jcolibri.method.retrieve.FilterBasedRetrieval.FilterBasedRetrievalMethod
 * @see jcolibri.method.retrieve.FilterBasedRetrieval.FilterConfig
 */
public class Equal extends Interval implements FilterPredicate
{
	/**
	 * Constructor.
	 *
	 */
	public Equal() {
		super(interval);
	}

	public boolean compute(Object caseObject, Object queryObject) throws NoApplicableFilterPredicateException
    {
	if((caseObject == null)&&(queryObject==null))
	    return true;
	else if(caseObject == null)
	    return false;
	else if(queryObject == null)
	    return true;
	else 
	    return caseObject.equals(queryObject);
    }

}
