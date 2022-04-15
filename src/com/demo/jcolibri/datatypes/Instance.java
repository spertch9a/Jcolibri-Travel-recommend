/**
 * Instance.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 11/01/2007
 */
package com.demo.jcolibri.datatypes;


import com.demo.jcolibri.connector.TypeAdaptor;
import com.demo.jcolibri.exception.OntologyAccessException;
import com.demo.jcolibri.util.OntoBridgeSingleton;

/**
 * Represents an Instance of an ontology.<p>
 * This class uses OntoBridge to connect with the ontology.
 * <p>
 * It can manage both short or long names to localize an instance:<br>
 * For example: In the gaia.fdi.ucm.es/ontologies/Restaurant.owl ontology, the long name of the concept "wine" is 
 * "gaia.fdi.ucm.es/ontologies/Restaurant.owl#wine. 
 * The short name is just "wine" or "restaurant:wine" if "restaurant" is a defined prefix for that ontology.
 * 
 * @author Juan A. Recio-Garc�a.
 * @version 2.0
 * @see TypeAdaptor
 */
public class Instance implements TypeAdaptor {

	private String name;
	private boolean useShortName = true;
	
	public static Instance createInstance(String instanceName, String parentConcept)
	{
		try {
			OntoBridgeSingleton.getOntoBridge().createInstance(parentConcept, instanceName);
			return new Instance(instanceName);
		} catch (OntologyAccessException e) {
			org.apache.commons.logging.LogFactory.getLog(Instance.class).error(e);
		}
		return null;
	}
	
	public Instance()
	{
	}
	
	/**
	 * Creates an Instance looking for the instance in the ontology with the same name.<p>
	 * The useShortName param allows using short or long names. For example:<br>
	 * In the gaia.fdi.ucm.es/ontologies/Restaurant.owl ontology, the long name of the concept "wine" is "gaia.fdi.ucm.es/ontologies/Restaurant.owl#wine. 
	 * The short name is just "wine" or "restaurant:wine" if "restaurant" is a defined prefix for that ontology.
	 * @param instance Name of the concept in the ontology
	 * @param useShortName Use long or short name
	 * @throws OntologyAccessException
	 */
	public Instance(String instance, boolean useShortName) throws OntologyAccessException
	{
		this.useShortName = useShortName; 
		fromString(instance);
	}
	
	/**
	 * Creates an instance connected with the instance in the ontology. It uses a short name format. 
	 * @param instance Name of the concept in the ontology
	 * @throws OntologyAccessException
	 */
	public Instance(String instance) throws OntologyAccessException
	{
		fromString(instance);
	}
	
	public void fromString(String content) throws OntologyAccessException {
		name = getCorrectRepresentation(content);
		if(!OntoBridgeSingleton.getOntoBridge().existsInstance(name))
			throw new OntologyAccessException("Instance: "+ name +" not found in loaded ontologies. Check names or OntoBridge configuration.");
	}
	
	public String toString()
	{
		return getCorrectRepresentation(name);
	}

	public boolean equals(Object o)
	{
		if(!(o instanceof Instance))
			return false;
		
		Instance i = (Instance)o;
		
		String otherName = i.name;
		if(isShort(otherName))
			otherName = OntoBridgeSingleton.getOntoBridge().getURI(otherName);
			
		String myName = name;
		if(isShort(myName))
			myName = OntoBridgeSingleton.getOntoBridge().getURI(myName);
		
		return myName.equals(otherName);
	}
	
	public int hashCode()
	{
	    return OntoBridgeSingleton.getOntoBridge().getURI(name).hashCode();
	}
	
	/**
	 * Indicates if using a long or short name.
	 * @param yesno
	 */
	public void useShortName(boolean yesno)
	{
		useShortName = yesno;
	}
	
	private String getCorrectRepresentation(String n)
	{
		if(useShortName)
		{
			if(!isShort(n))
				return OntoBridgeSingleton.getOntoBridge().getShortName(n);
		}else
		{
			if(isShort(n))
				return OntoBridgeSingleton.getOntoBridge().getURI(n);
		}
		return n;
	}
	
	private boolean isShort(String n)
	{
		return n.indexOf('#')==-1;
	}
}
