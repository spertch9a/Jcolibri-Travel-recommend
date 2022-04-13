/**
 * ObtainQueryWithFormMethod.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 23/10/2007
 */
package com.demo.jcolibri.method.gui.formFilling;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.cbrcore.CaseComponent;
import jcolibri.exception.AttributeAccessException;
import jcolibri.method.gui.editors.ParameterEditor;
import jcolibri.method.gui.editors.ParameterEditorFactory;
import jcolibri.method.gui.utils.LayoutUtils;
import jcolibri.method.gui.utils.WindowUtils;
import jcolibri.util.AttributeUtils;

/**
 * Shows a from to obtain the query.<br>
 * The methods of this class allow to use default values (read from the query),
 * hide some attributes and specify the label shown with each attribute.
 * 
 * @author Juan A. Recio-Garcia
 * @version 1.0
 *
 */
public class ObtainQueryWithFormMethod
{
    private static Hashtable<Attribute, ParameterEditor> editors;
    private static JDialog dialog;

    /**
     * Obtains a query without showing initila values.
     * It shows every attribute and uses the attribute name as the label.
     * @param query to obtain.
     */
    public static void obtainQueryWithoutInitialValues(CBRQuery query, Collection<Attribute> hiddenAttributes, Map<Attribute, String> labels)
    {
	obtainQuery(query, false, hiddenAttributes, labels);
    }
    
    /**
     * Obtains a query showing the initial values of the received query object.
     * It shows every attribute and uses the attribute name as the label.
     * @param query with the initial values and where the user new values are stored
     */
    public static void obtainQueryWithInitialValues(CBRQuery query, Collection<Attribute> hiddenAttributes, Map<Attribute, String> labels)
    {
	obtainQuery(query, true,  hiddenAttributes, labels);
    }
    
    /**
     * Obtains the query showing a form
     * @param query to fill with the obtained values and containing the initial values
     * @param useQueryvalues indicates if the query values are shown by default
     * @param hiddenAttributes is a list of not shown attributes
     * @param labels for each attribute. If there is no label for an attribute, 
     * the attribute name is used.
     */
    static void obtainQuery(CBRQuery query, boolean useQueryvalues, Collection<Attribute> hiddenAttributes, Map<Attribute, String> labels)
    {
	dialog = new JDialog();
	dialog.setModal(true);
	
	editors = new Hashtable<Attribute, ParameterEditor>();
	
	JPanel panel = new JPanel();
	
	addAttributes(query.getDescription(), panel, hiddenAttributes, labels);
	
	if(useQueryvalues)
	{
	    for(Attribute a: editors.keySet())
	    {
		ParameterEditor editor = editors.get(a);
		editor.setEditorValue(AttributeUtils.findValue(a, query));
	    }
	}
	
	dialog.getContentPane().setLayout(new BorderLayout());
	dialog.getContentPane().add(panel,BorderLayout.CENTER);
	
	JPanel okPanel = new JPanel();
	JButton okButton = new JButton("Ok");
	okButton.addActionListener(new ActionListener(){
	    public void actionPerformed(ActionEvent arg0)
	    {
		dialog.setVisible(false);	
	    }
	});
	okPanel.add(okButton);
	dialog.getContentPane().add(okPanel,BorderLayout.SOUTH);
	
	
	dialog.pack();
	WindowUtils.centerWindow(dialog);
	dialog.setTitle("Query");
	dialog.setVisible(true);
	
	for(Attribute a: editors.keySet())
	{
	    Object value = editors.get(a).getEditorValue();
	    AttributeUtils.setValue(a, query, value);
	}
	System.out.println(query);
    }
    
    /**
     * Adds Parameter editor to the panel for each attribute in the CaseComponent
     * @param cc CaseComponent with the attributes
     * @param panel to add the ParameterEditor
     * @param hiddenAttributes attributes not shown
     * @param labels for the attributes
     */
    private static void addAttributes(CaseComponent cc, JPanel panel, Collection<Attribute> hiddenAttributes, Map<Attribute, String> labels)
    {
	panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
	JPanel simplePanel = new JPanel();
	simplePanel.setLayout(new SpringLayout());
	
	JPanel compoundPanel = new JPanel();
	compoundPanel.setLayout(new BoxLayout(compoundPanel,BoxLayout.Y_AXIS));

	try
	{
	    if(hiddenAttributes == null)
		hiddenAttributes = new ArrayList<Attribute>();
	    if(labels == null)
		labels = new HashMap<Attribute,String>();
	    
	    Attribute[] ats = AttributeUtils.getAttributes(cc.getClass());
	    Attribute id = cc.getIdAttribute();
	    
	    ArrayList<Attribute> compounds = new ArrayList<Attribute>();
	    int sAtts = 0;
	    for(Attribute a: ats)
	    {
		if(a.equals(id))
		    continue;
		else if(a.getType().equals(CaseComponent.class))
	        {
	            compounds.add(a);
	        }
	        else if(hiddenAttributes.contains(a))
	            continue;
	        else
	        {
	            String label = labels.get(a);
	            if(label==null)
	        	label = a.getName();
	            simplePanel.add(new JLabel(label));
	            ParameterEditor pe = ParameterEditorFactory.getEditor(a.getType());
	            simplePanel.add(pe.getJComponent());
	            editors.put(a,pe);
	            sAtts++;
	        }
	    }
	    LayoutUtils.makeCompactGrid(simplePanel, sAtts,2,5,5,5,5);
	    
	    
	    //Now process compounds
	    for(Attribute comp: compounds)
	    {
		JPanel subpanel = new JPanel();
		subpanel.setBorder(javax.swing.BorderFactory.createTitledBorder(comp.getName()));
		addAttributes((CaseComponent)comp.getValue(cc),subpanel,hiddenAttributes,labels);
		compoundPanel.add(subpanel);
	    }
            
	    panel.add(simplePanel);
	    panel.add(compoundPanel);
	} catch (AttributeAccessException e)
	{
	    org.apache.commons.logging.LogFactory.getLog(AttributeUtils.class).error(e);
	}
    }
    
}
