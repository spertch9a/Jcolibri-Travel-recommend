package com.demo.jcolibri.method.retrieve.NNretrieval.similarity.local.interfaces;

import java.io.Serializable;

public interface StringDistance extends Serializable {
	 double distance(String s1, String s2);
}
