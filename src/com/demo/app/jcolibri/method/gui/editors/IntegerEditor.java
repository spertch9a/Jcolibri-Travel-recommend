/**
 * IntegerEditor.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 02/11/2007
 */
package com.demo.app.jcolibri.method.gui.editors;

import java.util.Collection;

import javax.swing.JComponent;
import javax.swing.JTextField;

/**
 * Parameter Editor for Integer values.
 * 
 * @author Juan A. Recio-Garcia
 * @version 1.0
 * @see jcolibri.method.gui.editors.ParameterEditor
 */
public class IntegerEditor extends JTextField implements ParameterEditor {
	private static final long serialVersionUID = 1L;

	
	/**
	 *  Creates a new instance
	 */
	public IntegerEditor() {
	    super();
	}
	

	/**
	 * Returns an Integer object
	 */
	public Object getEditorValue() {
	    try{
		return new Integer(getText());
	    }catch(Exception e){}
	    return null;
	}

	/**
	 * Returns the JComponent
	 */
	public JComponent getJComponent() {
		return (JComponent) this;
	}

	/**
	 * Receives an Integer value
	 */
	public void setEditorValue(Object value) {
	    	if(value == null)
	    	{
	    	    this.setText("");
	    	    return;
	    	}
		this.setText(value.toString());
	}

	/**
	 * Does nothing
	 */
	public void setAllowedValues(Collection<Object> allowedValues)
	{
	    // ANY
	}

}
