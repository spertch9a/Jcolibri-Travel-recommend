/**
 * StringEditor.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 02/11/2007
 */
package com.demo.jcolibri.method.gui.editors;

import jcolibri.method.gui.editors.ParameterEditor;

import java.awt.Dimension;
import java.util.Collection;

import javax.swing.JComponent;
import javax.swing.JTextField;

/**
 * Parameter Editor for String values.
 * 
 * @author Juan A. Recio-Garcia
 * @version 1.0
 * @see jcolibri.method.gui.editors.ParameterEditor
 */
public class StringEditor extends JTextField implements ParameterEditor {
	private static final long serialVersionUID = 1L;
	
	/**
	 *  Creates a new instance
	 */
	public StringEditor() {
        this.setMaximumSize(new Dimension(50,20));
	}

	/**
	 * Returns a String object
	 */
	public Object getEditorValue() {
	    if(getText().length()==0)
		return null;
		return getText();
	}

	/**
	 * Returns the JComponent
	 */
	public JComponent getJComponent() {
		return (JComponent) this;
	}

	/**
	 * Receives a String value
	 */
	public void setEditorValue(Object defaultValue) {
	    	if(defaultValue == null)
	    	{
	    	    this.setText("");
	    	    return;
	    	}
		String value = defaultValue.toString();
		this.setText(value);
	}

	/**
	 * Does nothing
	 */
	public void setAllowedValues(Collection<Object> allowedValues)
	{
	    // ANY 
	}

}
