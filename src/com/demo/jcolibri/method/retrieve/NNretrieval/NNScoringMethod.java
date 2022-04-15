/**
 * NNretrievalMethod.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 03/01/2007
 */
package com.demo.jcolibri.method.retrieve.NNretrieval;

import com.demo.jcolibri.cbrcore.CBRCase;
import com.demo.jcolibri.cbrcore.CBRQuery;
import com.demo.jcolibri.method.retrieve.NNretrieval.NNConfig;
import com.demo.jcolibri.method.retrieve.NNretrieval.similarity.GlobalSimilarityFunction;
import com.demo.jcolibri.method.retrieve.RetrievalResult;
import com.demo.jcolibri.util.ProgressController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NNScoringMethod {

	
	@SuppressWarnings("unchecked")
      /**
       * Performs the NN scoring over a collection of cases comparing them with a query. 
       * This method is configured through the NNConfig object.
       */
	public static Collection<RetrievalResult> evaluateSimilarity(Collection<CBRCase> cases, CBRQuery query, NNConfig simConfig)
	{
		List<RetrievalResult> res = new ArrayList<RetrievalResult>();
		ProgressController.init(NNScoringMethod.class,"Numeric Similarity Computation", cases.size());
		GlobalSimilarityFunction gsf = simConfig.getDescriptionSimFunction();
		for(CBRCase _case: cases)
		{
			res.add(new RetrievalResult(_case, gsf.compute(_case.getDescription(), query.getDescription(), _case, query, simConfig)));
			ProgressController.step(NNScoringMethod.class);
		}
		java.util.Collections.sort(res);
		ProgressController.finish(NNScoringMethod.class);
		
		return res;
	}
}
