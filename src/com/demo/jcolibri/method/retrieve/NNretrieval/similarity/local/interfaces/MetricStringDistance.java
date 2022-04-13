package com.demo.jcolibri.method.retrieve.NNretrieval.similarity.local.interfaces;

import jcolibri.method.retrieve.NNretrieval.similarity.local.interfaces.StringDistance;

/**
 * String distances that implement this interface are metrics, which means:
 * d(x, y) ≥ 0     (non-negativity, or separation axiom)
 * d(x, y) = 0   if and only if   x = y     (identity, or coincidence axiom)
 * d(x, y) = d(y, x)     (symmetry)
 * d(x, z) ≤ d(x, y) + d(y, z)     (triangle inequality).
 * 
 * @author Thibault Debatty
 */
public interface MetricStringDistance extends StringDistance {
    double distance(String s1, String s2);
}

