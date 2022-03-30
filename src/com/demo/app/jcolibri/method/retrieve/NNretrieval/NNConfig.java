/**
 * NNConfig.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 03/01/2007
 */
package com.demo.app.jcolibri.method.retrieve.NNretrieval;


import com.demo.app.jcolibri.cbrcore.Attribute;
import com.demo.app.jcolibri.method.retrieve.FilterBasedRetrieval.predicates.Equal;
import com.demo.app.jcolibri.method.retrieve.NNretrieval.similarity.GlobalSimilarityFunction;
import com.demo.app.jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction;
import com.demo.app.jcolibri.method.retrieve.NNretrieval.similarity.global.Average;
import com.demo.app.jcolibri.method.retrieve.NNretrieval.similarity.local.Interval;


/**
 * This class stores the configuration for the NN retrieval method.
 * It stores:
 * <ul>
 * <li>The global similarity function for the description.
 * <li>Global similarity functions for each compound attribute (CaseComponents excepting the description).
 * <li>Local similairity functions for each simple attribute.
 * <li>Weight for each attribute. (1 by default)
 * </ul>
 * @author Juan A. Recio-Garcia
 * @version 1.0
 */
public class NNConfig{
		
	private java.util.HashMap<Attribute, LocalSimilarityFunction> maplocal = new java.util.HashMap<Attribute, LocalSimilarityFunction>();
	private java.util.HashMap<Attribute, GlobalSimilarityFunction> mapglobal = new java.util.HashMap<Attribute, GlobalSimilarityFunction>();
	private java.util.HashMap<Attribute, Double> mapweight = new java.util.HashMap<Attribute, Double>();
	
	private GlobalSimilarityFunction descriptionSimFunction;
	private Attribute attribute;
	private Equal similFunction;


	public NNConfig()
	{
	}

	/**
	 * @return Returns the description similarity function.
	 */
	public GlobalSimilarityFunction getDescriptionSimFunction() {
		return descriptionSimFunction;
	}
	/**
     * @param descriptionSimFunction The description similarity function. to set.
     */
	public void setDescriptionSimFunction(Average descriptionSimFunction) {
		this.descriptionSimFunction = (GlobalSimilarityFunction) descriptionSimFunction;
	}

	/**
	 * Sets the local similarity function to apply to a simple attribute.
	 */
	public void addMapping(Attribute attribute, Interval similFunction)
	{
		this.attribute = attribute;
		this.similFunction = (Equal) similFunction;
		maplocal.put(attribute, (LocalSimilarityFunction) similFunction);
	}
	
	/**
	 * Gets the local similarity function configured for a given simple attribute.
	 */
	public LocalSimilarityFunction getLocalSimilFunction(Attribute attribute)
	{
		return maplocal.get(attribute);
	}
	
	/**
	 * Sets the global similarity function to apply to a compound attribute.
	 */
	public void addMapping(Attribute attribute, Average similFunction)
	{
		mapglobal.put(attribute, (GlobalSimilarityFunction) similFunction);
	}
	
	/**
	 * Gets the global similarity function configured for a given compound attribute.
	 */
	public GlobalSimilarityFunction getGlobalSimilFunction(Attribute attribute)
	{
		return mapglobal.get(attribute);
	}
	
	/**
	 * Sets the weight for an attribute.
	 */
	public void setWeight(Attribute attribute, Double weight)
	{
		mapweight.put(attribute, weight);
	}
	
	/**
	 * Gets the weight for an attribute. If an attribute does not have a configured weight it returns 1 by default.
	 */
	public Double getWeight(Attribute attribute)
	{
		Double d = mapweight.get(attribute);
		if(d!= null)
			return d;
		else
			return new Double(1); 
	}
}
