package com.demo.jcolibri.method.retrieve.NNretrieval.similarity.local;


import com.demo.jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction;

/**
 * This function returns a similarity value depending of the biggest substring
 * that belong to both strings.
 */
public class MaxString implements LocalSimilarityFunction {


	/**
	 * Applies the similarity function.
	 * 
	 * @param s
	 *            String.
	 * @param t
	 *            String.
	 * @return result of apply the similarity funciton.
	 */
	public double compute(Object s, Object t) throws com.demo.jcolibri.exception.NoApplicableSimilarityFunctionException
{
		if ((s == null) || (t == null))
			return 0;
		if (!(s instanceof String))
			throw new com.demo.jcolibri.exception.NoApplicableSimilarityFunctionException(this.getClass(), s.getClass());
		if (!(t instanceof String))
			throw new com.demo.jcolibri.exception.NoApplicableSimilarityFunctionException(this.getClass(), s.getClass());


		String news = (String) s;
		String newt = (String) t;
		if (news.equals(newt))
			return 1.0;
		else
			return ((double) MaxSubString(news, newt) / (double) Math.max(news
					.length(), newt.length()));
	}

	/**
	 * Returns the length of the biggest substring that belong to both strings.
	 * 
	 * @param s
	 * @param t
	 * @return
	 */
	private int MaxSubString(String s, String t) {
		String shorter = (s.length() > t.length()) ? t : s;
		String longer = (shorter.equals(s)) ? t : s;
		int best = 0;
		for (int i = 0; i < shorter.length(); i++) {
			for (int j = shorter.length(); j > i; j--) {
				if (longer.indexOf(shorter.substring(i, j)) != -1)
					best = Math.max(best, j - i);
			}
		}
		return best;
	}
	
	/** Applicable to String */
	public boolean isApplicable(Object o1, Object o2)
	{
		if((o1==null)&&(o2==null))
			return true;
		else if(o1==null)
			return o2 instanceof String;
		else if(o2==null)
			return o1 instanceof String;
		else
			return (o1 instanceof String)&&(o2 instanceof String);
	}
}