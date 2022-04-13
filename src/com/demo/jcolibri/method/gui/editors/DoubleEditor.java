/**
 * DoubleEditor.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 02/11/2007
 */
package com.demo.jcolibri.method.gui.editors;

import jcolibri.method.gui.editors.ParameterEditor;

import java.util.Collection;

import javax.swing.JComponent;
import javax.swing.JFormattedTextField;

/**
 * Parameter Editor for Double values.
 * 
 * @author Juan A. Recio-Garcia
 * @version 1.0
 * @see jcolibri.method.gui.editors.ParameterEditor
 */
public class DoubleEditor extends JFormattedTextField implements
        ParameterEditor {
	private static final long serialVersionUID = 1L;

	
	/**
	 *  Creates a new instance
	 */
	public DoubleEditor() {
		setValue(new Double(0));
	}

	/**
	 * Returns a Double object
	 */
	public Object getEditorValue() {
	    try{
		return new Double(getText());
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
	 * Receives a Double value
	 */
	public void setEditorValue(Object defaultValue) {
	    	if(defaultValue==null)
	    	{
	    	    this.setText("");
	    	    return;
	    	} 
		if (!(defaultValue instanceof Double))
			return;
		Double value = (Double) defaultValue;
		this.setValue(value);
	}

	/**
	 * Does nothing
	 */
	public void setAllowedValues(Collection<Object> allowedValues)
	{
	    //any
	}
}
