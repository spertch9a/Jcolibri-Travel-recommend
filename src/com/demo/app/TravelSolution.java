/**
 * TravelSolution.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 11/01/2007
 */
package com.demo.app;

import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;

/**
 * Bean that stores the solution of the case.
 * @author Juan A. Recio-Garcia
 * @version 1.0
 */ 
public class TravelSolution implements CaseComponent {

	String id;
	Integer price;
	String hotel;
	
	public String toString()
	{
		return "("+id+";"+price+";"+hotel+")";
	}
	
	public Attribute getIdAttribute() {
		
		return new Attribute("id", this.getClass());
	}

	/**
	 * @return Returns the hotel.
	 */
	public String getHotel() {
		return hotel;
	}

	/**
	 * @param hotel The hotel to set.
	 */
	public void setHotel(String hotel) {
		this.hotel = hotel;
	}

	/**
	 * @return Returns the id.
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return Returns the price.
	 */
	public Integer getPrice() {
		return price;
	}

	/**
	 * @param price The price to set.
	 */
	public void setPrice(Integer price) {
		this.price = price;
	}
	
	

}
