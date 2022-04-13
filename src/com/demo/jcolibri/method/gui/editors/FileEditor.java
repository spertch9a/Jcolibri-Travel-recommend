/**
 * FileEditor.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 02/11/2007
 */
package com.demo.jcolibri.method.gui.editors;

import jcolibri.method.gui.editors.ParameterEditor;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;
import java.util.Collection;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;


/**
 * Parameter Editor for File values.
 * 
 * @author Juan A. Recio-Garcia
 * @version 1.0
 * @see jcolibri.method.gui.editors.ParameterEditor
 */
public class FileEditor extends JPanel implements ParameterEditor {
	private static final long serialVersionUID = 1L;

	
	JTextField text;
	JButton button;
	
	
	/**
	 *  Creates a new instance
	 */
	public FileEditor() {
		text = new JTextField("");
		button = new JButton("...");
		this.add(text);
		this.add(button);
		button.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				buttonPressed();
			}
		});
		text.setMinimumSize(new java.awt.Dimension(150, 25));
		text.setPreferredSize(new java.awt.Dimension(150, 25));

	}

	private void buttonPressed() {
		FileDialog fd = new FileDialog((Frame)null,
				"Load file...", FileDialog.LOAD);
		fd.setVisible(true);
		if (fd.getFile() != null) {
			text.setText(fd.getDirectory() + fd.getFile());
		}
	}

	/**
	 * Returns a File object
	 */
	public Object getEditorValue() {
	    try{
		return new File(text.getText());
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
	 * Receives a File value
	 */
	public void setEditorValue(Object defaultValue) {
	    	if(defaultValue==null)
	    	{
	    	    text.setText("");
	    	    return;
	    	} 
		if (!(defaultValue instanceof String))
			return;
		String value = (String) defaultValue;
		text.setText(value);
	}

	/**
	 * Does nothing
	 */
	public void setAllowedValues(Collection<Object> allowedValues)
	{
	}

}
