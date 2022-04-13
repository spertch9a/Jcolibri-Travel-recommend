/**
 * BooleanEditor.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 02/11/2007
 */
package com.demo.jcolibri.method.gui.editors;

import jcolibri.method.gui.editors.ParameterEditor;

import java.util.Collection;

import javax.swing.JComboBox;
import javax.swing.JComponent;

/**
 * Parameter Editor for Boolean values.
 * 
 * @author Juan A. Recio-Garcia
 * @version 1.0
 * @see jcolibri.method.gui.editors.ParameterEditor
 */
public class BooleanEditor extends JComboBox implements ParameterEditor {
	private static final long serialVersionUID = 1L;
	

	private final String ANY   = "<any>";
	private final String TRUE  = "true";
	private final String FALSE = "false";
	
	/**
	 *  Creates a new instance
	 */
	public BooleanEditor() {
		super();
		addItem(ANY);
		addItem(TRUE);
		addItem(FALSE);
	}

	/**
	 * Returns a Boolean object
	 */
	public Object getEditorValue() {
		if(getSelectedItem().equals(ANY))
		    return null;
		if(getSelectedItem().equals(TRUE))
		    return true;
		else
		    return false;
	}

	/**
	 * Returns the JComponent
	 */
	public JComponent getJComponent() {
		return (JComponent) this;
	}

	/**
	 * Receives a Boolean value
	 */
	public void setEditorValue(Object value) {
	    	if(value == null)
	    	    setSelectedItem(ANY);
		if (!(value instanceof Boolean))
			return;
		Boolean bvalue = (Boolean) value;
		if(bvalue.booleanValue())
		    setSelectedItem(TRUE);
		else
		    setSelectedItem(FALSE);

	}

	/**
	 * Receives a Collection of Boolean objects.
	 */
	public void setAllowedValues(Collection<Object> allowedValues)
	{
	    boolean containTrue = false;
	    boolean containFalse = false;
	    for(Object o: allowedValues)
	    {
		Boolean bool = (Boolean)o;
		if(!containTrue)
		    containTrue = bool.booleanValue();
		if(!containFalse)
		    containFalse = !bool.booleanValue();
	    }
	    if(!containTrue)
		removeItem(TRUE);
	    if(!containFalse)
		removeItem(FALSE);
	}
}
