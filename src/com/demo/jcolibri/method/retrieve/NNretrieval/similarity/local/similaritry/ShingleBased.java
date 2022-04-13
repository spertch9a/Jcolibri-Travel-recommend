package com.demo.jcolibri.method.retrieve.NNretrieval.similarity.local.similaritry;

import java.util.*;
import java.util.regex.Pattern;

public class ShingleBased {
	  private static final int DEFAULT_K = 3;

	    private final int k;

	    /**
	     * Pattern for finding multiple following spaces.
	     */
	    private static final Pattern SPACE_REG = Pattern.compile("\\s+");

	    /**
	     *
	     * @param k
	     * @throws IllegalArgumentException if k is <= 0
	     */
	    ShingleBased(final int k) {
	        if (k <= 0) {
	            throw new IllegalArgumentException("k should be positive!");
	        }
	        this.k = k;
	    }

	    /**
	     *
	     */
	    ShingleBased() {
	        this(DEFAULT_K);
	    }

	    /**
	     * Return k, the length of k-shingles (aka n-grams).
	     *
	     * @return The length of k-shingles.
	     */
	    public int getK() {
	        return k;
	    }

	    /**
	     * Compute and return the profile of s, as defined by Ukkonen "Approximate
	     * string-matching with q-grams and maximal matches".
	     * https://www.cs.helsinki.fi/u/ukkonen/TCS92.pdf
	     * The profile is the number of occurrences of k-shingles, and is used to
	     * compute q-gram similarity, Jaccard index, etc.
	     * Pay attention: the memory requirement of the profile can be up to
	     * k * size of the string
	     *
	     * @param string
	     * @return the profile of this string, as an unmodifiable Map
	     */
	    public final Map<String, Integer> getProfile(final String string) {
	        HashMap<String, Integer> shingles = new HashMap<String, Integer>();

	        String string_no_space = SPACE_REG.matcher(string).replaceAll(" ");
	        for (int i = 0; i < (string_no_space.length() - k + 1); i++) {
	            String shingle = string_no_space.substring(i, i + k);
	            Integer old = shingles.get(shingle);
	            if (old!=null) {
	                shingles.put(shingle, old + 1);
	            } else {
	                shingles.put(shingle, 1);
	            }
	        }

	        return Collections.unmodifiableMap(shingles);
	    }

	}
