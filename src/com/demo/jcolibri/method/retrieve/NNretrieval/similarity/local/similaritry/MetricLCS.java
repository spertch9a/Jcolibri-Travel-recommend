package com.demo.jcolibri.method.retrieve.NNretrieval.similarity.local.similaritry;

import jcolibri.exception.NoApplicableSimilarityFunctionException;
import jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction;
import jcolibri.method.retrieve.NNretrieval.similarity.local.interfaces.*;
import jcolibri.method.retrieve.NNretrieval.similarity.local.similaritry.LongestCommonSubsequence;

public class MetricLCS  implements MetricStringDistance, NormalizedStringDistance,LocalSimilarityFunction  {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final LongestCommonSubsequence lcs = new LongestCommonSubsequence();

    /**
     * Distance metric based on Longest Common Subsequence, computed as
     * 1 - |LCS(s1, s2)| / max(|s1|, |s2|).
     *
     * @param s1 The first string to compare.
     * @param s2 The second string to compare.
     * @return The computed distance metric value.
     * @throws NullPointerException if s1 or s2 is null.
     */
    @Override
	public double compute(Object caseObject, Object queryObject) throws NoApplicableSimilarityFunctionException {
		
		 
        if (caseObject == null) {
            throw new NullPointerException("s1 must not be null");
        }

        if (queryObject == null) {
            throw new NullPointerException("s2 must not be null");
        }

        if (caseObject.equals(queryObject)) {
            return 0;
        }

        int mLen = Math.max(((String)caseObject).length(), ((String) queryObject).length());
        if (mLen == 0) {
            return 0;
        }
        return 1.0 - (1.0 * lcs.length(((String)caseObject),((String) queryObject)))/ mLen;
    }


	public static void main(String[] args) {
	MetricLCS j = new MetricLCS();
	double m = 0.0,n=0.0,s=0.0;
	try {
		m = j.compute("ras Coeur","ras Coeu");
		 n = j.compute("Grippe","Diabete");
		 s = j.compute("Cancer","Diabete");
	} catch (NoApplicableSimilarityFunctionException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		
		System.out.println("la distance\n 1-\t"+m+"\n2-\t"+n+"\n3-"+s);
	}


	


	@Override
	public boolean isApplicable(Object caseObject, Object queryObject) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public double distance(String s1, String s2) {
		// TODO Auto-generated method stub
		return 0;
	}

}
