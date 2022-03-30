/**
 * CreateProfile.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 02/11/2007
 */
package com.demo.app.jcolibri.method.gui.editors;

import java.io.File;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import jcolibri.datatypes.Instance;
import jcolibri.datatypes.Text;

/**
 * Factory to obtain the ParameterEditor of a data type.
 * 
 * @author Juan A. Recio-Garcia
 * @version 1.0
 *
 */
public class ParameterEditorFactory {

    private static HashMap<Class, Class> table = new HashMap<Class, Class>();

    //Code to register editors.
    //TODO search in classpath.
    static{
	ParameterEditorFactory.registerEditor(Boolean.class, BooleanEditor.class);
	ParameterEditorFactory.registerEditor(Date.class, DateEditor.class);
	ParameterEditorFactory.registerEditor(Double.class, DoubleEditor.class);
	ParameterEditorFactory.registerEditor(Enum.class, EnumEditor.class);
	ParameterEditorFactory.registerEditor(File.class, FileEditor.class);
	ParameterEditorFactory.registerEditor(Instance.class, InstanceEditor.class);
	ParameterEditorFactory.registerEditor(String.class, StringEditor.class);
	ParameterEditorFactory.registerEditor(Text.class, TextEditor.class);
	ParameterEditorFactory.registerEditor(Integer.class, IntegerEditor.class);
    }
    
    /**
     * Creates the editor and configures it with its data-type name
     */
    public static ParameterEditor getEditor(Class<?> type) {
	try
	{
	    
	    Class editor = table.get(type);
	    if(editor != null)
		return (ParameterEditor)table.get(type).newInstance();
	    
	    for(Class<?> c : table.keySet())
		if(c.isAssignableFrom(type))
		    editor = table.get(c);
	    
	    if(editor.equals(EnumEditor.class))
		return new EnumEditor(type);
	    if(editor != null)
		return (ParameterEditor)editor.newInstance();
	    
	    throw new Exception("No editor found for type: "+type.getName());
	    
	} catch (Exception e)
	{
	    org.apache.commons.logging.LogFactory.getLog(ParameterEditorFactory.class).error(e); 
	} 
	return null;
    }
    
    /**
     * Creates the editor and configures it with its data-type name
     */
    public static ParameterEditor getEditor(Class<?> type, Collection<Object> allowedValues) 
    {
	ParameterEditor pe = getEditor(type);
	if(pe==null)
	    return null;
	pe.setAllowedValues(allowedValues);
	return pe;
    }

    /**
     * Registers and editor
     * @param type that the editor manages
     * @param editor of the type
     */
    public static void registerEditor(Class type, Class editor) {
	table.put(type, editor);
    }

    /**
     * Unregisters the editor of a type.
     * @param type of the editor to remove
     */
    public static void unregisterEditor(Class type) {
	table.remove(type);
    }

    /**
     * Clears all the registered editors
     */
    public static void clear() {
	table.clear();
    }
}
