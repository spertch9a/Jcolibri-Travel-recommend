package com.demo.jcolibri.method.reuse.classification;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import jcolibri.extensions.classification.ClassificationSolution;
import jcolibri.method.retrieve.RetrievalResult;
import jcolibri.method.reuse.classification.AbstractKNNClassificationMethod;

/**
 * Provides the ability to classify a query by predicting its
 * solution from supplied cases. Classification is done by 
 * similarity weighted voting, where each vote is based on 
 * the similarity of the case to the query. The class with the
 * highest overall value is the predicted class.
 * 
 * @author Derek Bridge
 * @author Lisa Cummins
 * 16/05/07
 */
public class SimilarityWeightedVotingMethod extends AbstractKNNClassificationMethod
{
    /**
     * Predicts the class that has the highest value vote
     * among the k most similar cases, where votes are based on
     * similarity to the query.
     * If several classes receive the same highest vote, the class that
     * has the lowest hash code is taken as the prediction. 
     * @param cases
     *            an ordered list of cases along with similarity scores.
     * @return Returns the predicted solution.
     */
    public ClassificationSolution getPredictedSolution(Collection<RetrievalResult> cases)
    {
        Map<Object, Double> votes = new HashMap<Object, Double>();
        Map<Object, ClassificationSolution> values = new HashMap<Object, ClassificationSolution>();
        
        for(RetrievalResult result: cases)
        {   
            ClassificationSolution solution = (ClassificationSolution)result.get_case().getSolution();
           
            Object solnAttVal = solution.getClassification();
             
            double eval = result.getEval();
            if (votes.containsKey(solnAttVal))
            {   votes.put(solnAttVal, votes.get(solnAttVal) + eval);
            }
            else
            {   votes.put(solnAttVal, eval);
            	values.put(solnAttVal, solution);
            }
        }
        double highestVoteSoFar = 0.0;
        Object predictedClassVal = null;
        for (Map.Entry<Object, Double> e : votes.entrySet())
        {   if (e.getValue() >= highestVoteSoFar)
            {  	highestVoteSoFar = e.getValue();
                predictedClassVal = e.getKey();
            }
        }
        return values.get(predictedClassVal);
    }
}
