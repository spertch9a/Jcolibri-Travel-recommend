/**
 * CasesVisualization.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 23/05/2007
 */
package com.demo.app.jcolibri.extensions.visualization;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;

import jcolibri.cbrcore.CBRCase;
import jcolibri.extensions.classification.ClassificationSolution;
import jcolibri.method.retrieve.RetrievalResult;
import jcolibri.method.retrieve.NNretrieval.NNConfig;
import jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import es.csic.iiia.visualgraph.CBGraph;
import es.csic.iiia.visualgraph.CaseBaseVis;
import es.csic.iiia.visualgraph.InfoVisual;

/**
 * Wrapper to the InfoVisual library develped by Josep Lluis Arcos (IIIA-CSIC) that visualizes cases.
 * @author Juan A. Recio-Garcia
 * @version 2.0
 */
public class CasesVisualization {

	/**
	 * Visualizes a collection of cases using a given NN similarity configuration.
	 */
	public static void visualize(Collection<CBRCase> cases, NNConfig knnConfig)
	{
		CBGraph graph = new CBGraph();
		
		// First add all the nodes saving IDs in a table
		Hashtable<CBRCase,Integer> case2id = new Hashtable<CBRCase,Integer>();
		for(CBRCase c : cases)
		{
		    	ClassificationSolution sol = (ClassificationSolution)c.getSolution();
			int id = graph.addCase(c.getID().toString(), sol.getClassification().toString());
			case2id.put(c, id);
		}
		
		// Now calculate edges distances
		Collection<CBRCase> copy = new ArrayList<CBRCase>(cases);
		
		//Compute total cycles
		int total = cases.size() * (cases.size()-1) / 2;
		jcolibri.util.ProgressController.init(CasesVisualization.class, "Computing distances", total);

		for(CBRCase c : cases)
		{
			copy.remove(c);
			Collection<RetrievalResult> result = NNScoringMethod.evaluateSimilarity(copy, c, knnConfig);
			int currentcaseId = case2id.get(c);
			for(RetrievalResult sim : result)
			{
				CBRCase otherCase = sim.get_case();
				int othercaseId = case2id.get(otherCase);
				float distance = (float)(1-sim.getEval());
				graph.addDistance(currentcaseId, othercaseId, distance);
				System.out.println("Distance: "+c.getID()+" <--> "+otherCase.getID()+": "+ distance);
				jcolibri.util.ProgressController.step(CasesVisualization.class);
			}
		}
		jcolibri.util.ProgressController.finish(CasesVisualization.class);
		
		// Finally, visualize the data
		new InfoVisual( new CaseBaseVis(graph), true, false);
	}
}
