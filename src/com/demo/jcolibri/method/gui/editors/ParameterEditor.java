/**
 * CreateProfile.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 02/11/2007
 */
package com.demo.jcolibri.method.gui.editors;

import java.util.Collection;

import javax.swing.JComponent;

/**
 * Interface for the parameter Editors used by the methods that obtain
 * the query graphically.
 * 
 * @author Juan A. Recio-Garcia
 * @version 1.0
 *
 */
public interface ParameterEditor {

    /**
     * Gets the Editor value
     */
    public Object getEditorValue();

    /**
     * Gets the Editor as a JComponent to be added to the panel
     */
    public JComponent getJComponent();

    /**
     * Sets the default value of the editor
     */
    public void setEditorValue(Object value);
    
    /**
     * Sets the allowed values for the editor
     */
    public void setAllowedValues(Collection<Object> allowedValues);
}
