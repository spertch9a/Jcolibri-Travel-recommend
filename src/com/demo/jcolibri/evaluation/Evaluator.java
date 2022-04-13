/**
 * Evaluator.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 07/05/2007
 */
package com.demo.jcolibri.evaluation;

import jcolibri.cbraplications.StandardCBRApplication;
import jcolibri.evaluation.EvaluationReport;

/**
 * This abstract class defines the common behaviour of an evaluator.
 * @author Juanan
 */
public abstract class Evaluator {
	
	/** Initializes the evaluator with the CBR application to evaluate 
	 * @see StandardCBRApplication
	 * */
	public abstract void init(StandardCBRApplication cbrApp);
	
	/** Object that stores the evaluation results */
	protected static EvaluationReport report;
	
	/** Returns the evaluation report */
	public static EvaluationReport getEvaluationReport(){
		return report;
	}
}
