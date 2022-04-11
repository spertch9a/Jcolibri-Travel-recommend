/**
 * Region.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 10/01/2007
 */
package com.demo.app;


import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;

/**
 * Compound attribute that stores the information of the region in the travel description.
 * @author Juan A. Recio-Garcia
 * @version 1.0
 * @see TravelDescription
 */
public class Region implements CaseComponent {
	Integer id;
	String region;
	String city;
	String airport;
	String currency;
	

	public String toString()
	{
		return "("+id+","+region+","+city+","+airport+","+currency+")";
	}
	
	
	/**
	 * @return the airport
	 */
	public String getAirport() {
		return airport;
	}
	/**
	 * @param airport the airport to set
	 */
	public void setAirport(String airport) {
		this.airport = airport;
	}
	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}
	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}
	/**
	 * @param currency the currency to set
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * @return the region
	 */
	public String getRegion() {
		return region;
	}
	/**
	 * @param region the region to set
	 */
	public void setRegion(String region) {
		this.region = region;
	}
	
	
	public Attribute getIdAttribute() {
		return new Attribute("id", this.getClass());
	}
}
