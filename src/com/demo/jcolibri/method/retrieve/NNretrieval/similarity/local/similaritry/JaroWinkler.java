package com.demo.jcolibri.method.retrieve.NNretrieval.similarity.local.similaritry;

import java.util.Arrays;

import jcolibri.exception.NoApplicableSimilarityFunctionException;
import jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction;
import jcolibri.method.retrieve.NNretrieval.similarity.local.interfaces.NormalizedStringDistance;
import jcolibri.method.retrieve.NNretrieval.similarity.local.interfaces.NormalizedStringSimilarity;

/**
 * The Jaro�Winkler distance metric is designed and best suited for short
 * strings such as person names, and to detect typos; it is (roughly) a
 * variation of Damerau-Levenshtein, where the substitution of 2 close
 * characters is considered less important then the substitution of 2 characters
 * that a far from each other.
 * Jaro-Winkler was developed in the area of record linkage (duplicate
 * detection) (Winkler, 1990). It returns a value in the interval [0.0, 1.0].
 * The distance is computed as 1 - Jaro-Winkler similarity.
 * @author Thibault Debatty
 */

public class JaroWinkler  implements NormalizedStringSimilarity, NormalizedStringDistance,LocalSimilarityFunction  {

    private static final double DEFAULT_THRESHOLD = 0.7;
    private static final int THREE = 3;
    private static final double JW_COEF = 0.1;
    private final double threshold;

    /**
     * Instantiate with default threshold (0.7).
     *
     */
    public JaroWinkler() {
        this.threshold = DEFAULT_THRESHOLD;
    }

    /**
     * Instantiate with given threshold to determine when Winkler bonus should
     * be used.
     * Set threshold to a negative value to get the Jaro distance.
     * @param threshold
     */
    public JaroWinkler(final double threshold) {
        this.threshold = threshold;
    }

    /**
     * Returns the current value of the threshold used for adding the Winkler
     * bonus. The default value is 0.7.
     *
     * @return the current value of the threshold
     */
    public final double getThreshold() {
        return threshold;
    }

    /**
     * Compute Jaro-Winkler similarity.
     * @param caseObject The first string to compare.
     * @param queryObject The second string to compare.
     * @return The Jaro-Winkler similarity in the range [0, 1]
     * @throws NullPointerException if s1 or s2 is null.
     */
    public final double similarity(final Object caseObject, final Object queryObject) {
        if (caseObject == null) {
            throw new NullPointerException("s1 must not be null");
        }

        if (queryObject == null) {
            throw new NullPointerException("s2 must not be null");
        }

        if (caseObject.equals(queryObject)) {
            return 1;
        }

        int[] mtp = matches(caseObject, queryObject);
        float m = mtp[0];
        if (m == 0) {
            return 0f;
        }
        double j = ((m / ((String) caseObject).length() + m / ((String) queryObject).length() + (m - mtp[1]) / m))
                / THREE;
        double jw = j;

        if (j > getThreshold()) {
            jw = j + Math.min(JW_COEF, 1.0 / mtp[THREE]) * mtp[2] * (1 - j);
        }
        return jw;
    }


    /**
     * Return 1 - similarity.
     * @param s1 The first string to compare.
     * @param s2 The second string to compare.
     * @return 1 - similarity.
     * @throws NullPointerException if s1 or s2 is null.
     */
    @Override
	public double compute(Object caseObject, Object queryObject) throws NoApplicableSimilarityFunctionException {
		// TODO Auto-generated method stub
		

   
        return 1.0 - similarity(caseObject, queryObject);
    }

    private int[] matches(final Object caseObject, final Object queryObject) {
        String max, min;
        if (((String) caseObject).length() > ((String) queryObject).length()) {
            max = (String) caseObject;
            min = queryObject.toString();
        } else {
            max = queryObject.toString();
            min = caseObject.toString();
        }
        int range = Math.max(max.length() / 2 - 1, 0);
        int[] matchIndexes = new int[min.length()];
        Arrays.fill(matchIndexes, -1);
        boolean[] matchFlags = new boolean[max.length()];
        int matches = 0;
        for (int mi = 0; mi < min.length(); mi++) {
            char c1 = min.charAt(mi);
            for (int xi = Math.max(mi - range, 0),
                    xn = Math.min(mi + range + 1, max.length()); xi < xn; xi++) {
                if (!matchFlags[xi] && c1 == max.charAt(xi)) {
                    matchIndexes[mi] = xi;
                    matchFlags[xi] = true;
                    matches++;
                    break;
                }
            }
        }
        char[] ms1 = new char[matches];
        char[] ms2 = new char[matches];
        for (int i = 0, si = 0; i < min.length(); i++) {
            if (matchIndexes[i] != -1) {
                ms1[si] = min.charAt(i);
                si++;
            }
        }
        for (int i = 0, si = 0; i < max.length(); i++) {
            if (matchFlags[i]) {
                ms2[si] = max.charAt(i);
                si++;
            }
        }
        int transpositions = 0;
        for (int mi = 0; mi < ms1.length; mi++) {
            if (ms1[mi] != ms2[mi]) {
                transpositions++;
            }
        }
        int prefix = 0;
        for (int mi = 0; mi < min.length(); mi++) {
            if (((String) caseObject).charAt(mi) == ((String) queryObject).charAt(mi)) {
                prefix++;
            } else {
                break;
            }
        }
        return new int[]{matches, transpositions / 2, prefix, max.length()};
    }


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JaroWinkler j = new JaroWinkler();
	
		double m;
		try {
			m = j.compute("Temp","emp");
			double n = j.compute("Grippe","Diabete");
			double s = j.compute("Cance","Diabete");
			System.out.println("la distance\n 1-\t"+m+"\n2-\t"+n+"\n3-"+s);
		} catch (NoApplicableSimilarityFunctionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
	@Override
	public boolean isApplicable(Object caseObject, Object queryObject) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public double similarity(String s1, String s2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double distance(String s1, String s2) {
		// TODO Auto-generated method stub
		return 0;
	}

}
