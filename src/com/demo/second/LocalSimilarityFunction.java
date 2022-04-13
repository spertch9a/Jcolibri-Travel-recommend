/**
 * LocalSimilarityFunction.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 03/01/2007
 */
package com.demo.second;


import es.ucm.fdi.gaia.jcolibri.exception.NoApplicableSimilarityFunctionException;

/**
 * Defines the methods of a local similarity function.
 * A local similarity function is applied to simple attributes by the NN algorithm.
 * @author Juan A. Recio-Garcia
 * @version 1.0
 */
public interface LocalSimilarityFunction {

	/**
	 * Computes the similarity between two objects
	 *
	 * @param caseObject  object of the case
	 * @param queryObject object of the query
	 * @return a value between [0..1]
	 */
	public default double compute(Object caseObject, Object queryObject) throws NoApplicableSimilarityFunctionException {
		return 0;
	}

	/**
	 * Indicates if the function is applicable to two objects
	 */
	public boolean isApplicable(Object caseObject, Object queryObject);
}
