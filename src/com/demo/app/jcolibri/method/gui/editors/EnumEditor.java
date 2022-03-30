/**
 * EnumEditor.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 02/11/2007
 */
package com.demo.app.jcolibri.method.gui.editors;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JComponent;


/**
 * Parameter Editor for Enum values.
 * 
 * @author Juan A. Recio-Garcia
 * @version 1.0
 * @see jcolibri.method.gui.editors.ParameterEditor
 */
public class EnumEditor extends JComboBox implements
		ParameterEditor {
	private static final long serialVersionUID = 1L;

	
	private final String EMPTY = "<empty>";

	/**
	 *  Creates a new instance
	 */
	public EnumEditor(Class enumeration)
	{
	    super();
	    Object[] constants = enumeration.getEnumConstants();
	    this.addItem(EMPTY);
	    for(int i=0; i<constants.length; i++)
		this.addItem(constants[i]);
	}
	

	/**
	 * Returns an Enum value object
	 */
	public Object getEditorValue() {
		Object value = this.getSelectedItem();
		if (value.equals(EMPTY))
			return null;
		return value;
	}

	/**
	 * Returns the JComponent
	 */
	public JComponent getJComponent() {
		return this;
	}

	/**
	 * Receives a Boolean value
	 */
	public void setEditorValue(Object value) {
	    if(value==null)
		this.setSelectedItem(EMPTY);
	    else
		this.setSelectedItem(value);
	}


	/**
	 * Receives a list of Enum values
	 */
	@SuppressWarnings("unchecked")
	public void setAllowedValues(Collection<Object> allowedValues)
	{
	    this.removeAllItems();
	    List<Enum> list = new ArrayList<Enum>();
	    for(Object o: allowedValues)
		list.add((Enum)o);
	    java.util.Collections.sort(list);
	    for(Enum e: list)
		this.addItem(e);
	}

}
