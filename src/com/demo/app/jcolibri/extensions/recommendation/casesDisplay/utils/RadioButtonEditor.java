/**
 * RadioButtonEditor.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 02/11/2007
 */
package com.demo.app.jcolibri.extensions.recommendation.casesDisplay.utils;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.JTable;

/**
 * Utility class for managing radio buttons in a table
 * @author Juan A. Recio-Garcia
 * @author Developed at University College Cork (Ireland) in collaboration with Derek Bridge.
 * @version 1.0
 *
 */
public class RadioButtonEditor extends DefaultCellEditor implements ItemListener
{

    private static final long serialVersionUID = 1L;

    private JRadioButton button;

    public RadioButtonEditor(JCheckBox checkBox)
    {
	super(checkBox);
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
    {
	if (value == null)
	    return null;
	button = (JRadioButton) value;
	button.addItemListener(this);
	return (Component) value;
    }

    public Object getCellEditorValue()
    {
	button.removeItemListener(this);
	return button;
    }

    public void itemStateChanged(ItemEvent e)
    {
	super.fireEditingStopped();
    }
}
