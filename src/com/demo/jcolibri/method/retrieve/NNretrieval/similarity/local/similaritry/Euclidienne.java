package com.demo.jcolibri.method.retrieve.NNretrieval.similarity.local.similaritry;

import jcolibri.exception.NoApplicableSimilarityFunctionException;
import jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction;

public class Euclidienne implements  LocalSimilarityFunction{
	double g;
	
	
	@Override
	public double compute(Object caseObject, Object queryObject) throws NoApplicableSimilarityFunctionException {
		g=Math.sqrt(Math.abs((Math.pow((double) caseObject, 2))-(Math.pow((double) queryObject, 2))));
		double m = (double)caseObject+(double)queryObject;
		System.out.println(g/m);
		return g/m;
		
	}
	@Override
	public boolean isApplicable(Object caseObject, Object queryObject) {
		// TODO Auto-generated method stub
		return false;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Euclidienne j = new Euclidienne();
		double m;
		try {
			m = j.compute(20.0,2.0);
		
		double n = j.compute(1.0,1.0);
		double s = j.compute(30.0,35.0);
		System.out.println("la distance\n 1-\t"+m+"\n2-\t"+n+"\n3-"+s);
		} catch (NoApplicableSimilarityFunctionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
