/**
 * DateEditor.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 02/11/2007
 */
package com.demo.app.jcolibri.method.gui.editors;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;

import javax.swing.JComponent;
import javax.swing.JFormattedTextField;

/**
 * Parameter Editor for Date values.
 * 
 * @author Juan A. Recio-Garcia
 * @version 1.0
 * @see jcolibri.method.gui.editors.ParameterEditor
 */
public class DateEditor extends JFormattedTextField implements ParameterEditor {
	private static final long serialVersionUID = 1L;

	
	/**
	 *  Creates a new instance
	 */
	public DateEditor() {
		setValue(new Date());
	}
	
	/**
	 * Returns a Date object
	 */
	public Object getEditorValue() {
		try {
			return DateFormat.getDateInstance().parse(getText());
		} catch (ParseException pe) {
			return null; // To do, check what to do
		}
	}

	/**
	 * Returns the JComponent
	 */
	public JComponent getJComponent() {
		return (JComponent) this;
	}

	/**
	 * Receives a Date value
	 */
	public void setEditorValue(Object defaultValue) {
	    	if(defaultValue==null)
	    	{
	    	    this.setText("");
	    	    return;
	    	} 
		if (!(defaultValue instanceof Date))
			return;
		Date value = (Date) defaultValue;
		this.setValue(value);
	}

	/**
	 * Does nothing
	 */
	public void setAllowedValues(Collection<Object> allowedValues)
	{  
	}

}
