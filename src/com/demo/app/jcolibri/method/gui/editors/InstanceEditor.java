/**
 * InstanceEditor.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 02/11/2007
 */
package com.demo.app.jcolibri.method.gui.editors;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;

import jcolibri.datatypes.Instance;
import jcolibri.exception.OntologyAccessException;
import es.ucm.fdi.gaia.ontobridge.OntoBridge;
import es.ucm.fdi.gaia.ontobridge.test.gui.PnlSelectInstance;

/**
 * Parameter Editor for Instance values.
 * 
 * @author Juan A. Recio-Garcia
 * @version 1.0
 * @see jcolibri.method.gui.editors.ParameterEditor
 */
public class InstanceEditor extends JButton implements ParameterEditor {
	private static final long serialVersionUID = 1L;

	
	private static Icon INSTANCE = new javax.swing.ImageIcon(jcolibri.util.FileIO.findFile("/es/ucm/fdi/gaia/ontobridge/test/gui/instance.gif"));
	
	JDialog ontoDialog;
	PnlSelectInstance ontoPanel;
	String selected;
	
	/**
	 *  Creates a new instance
	 */
	public InstanceEditor()
	{
		this.setText("...");
		
		ontoDialog = new JDialog((Frame)null, true);
		OntoBridge ob = jcolibri.util.OntoBridgeSingleton.getOntoBridge();
		ontoPanel = new PnlSelectInstance(ob);
		Container main = ontoDialog.getContentPane();
		main.setLayout(new BorderLayout());
		main.add(ontoPanel, BorderLayout.CENTER);
		
		JButton select = new JButton("Select Instance");
		select.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				ontoDialog.setVisible(false);	
			}
		});
		main.add(select,BorderLayout.SOUTH);
		
		ontoDialog.pack();
		
		Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		ontoDialog.setBounds((screenSize.width - ontoDialog.getWidth()) / 2,
			(screenSize.height - ontoDialog.getHeight()) / 2, 
			ontoDialog.getWidth(),
			ontoDialog.getHeight());
		
		this.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				selectInstance();	
			}
		});
	}
	
	
	void selectInstance()
	{
		ontoDialog.setVisible(true);
		selected = ontoPanel.getSelectedInstance();
		if(selected==null)
		{
			this.setText("...");
			this.setIcon(null);
		}
		else
		{
			this.setText(selected);
			this.setIcon(INSTANCE);
		}
	}
	

	/**
	 * Returns an Instance object
	 */
	public Object getEditorValue() {
	    try {
		if(selected==null)
			return null;
		return new Instance(selected);
	    } catch (OntologyAccessException e) {
		org.apache.commons.logging.LogFactory.getLog(this.getClass()).error(e);
	    }
	    return null;
	}

	/**
	 * Returns the JComponent
	 */
	public JComponent getJComponent() {
		return (JComponent) this;
	}

	/**
	 * Receives an Instance (or String) value
	 */
	public void setEditorValue(Object value) {
	    	if(value==null)
	    	{
	    	    this.setText("");
	    	    this.setIcon(null);
	    	    return;
	    	} 
		selected = value.toString();
		this.setText(selected);
		this.setIcon(INSTANCE);
	}

	/**
	 * Does nothing
	 */
	public void setAllowedValues(Collection<Object> allowedValues)
	{  
	}
}
