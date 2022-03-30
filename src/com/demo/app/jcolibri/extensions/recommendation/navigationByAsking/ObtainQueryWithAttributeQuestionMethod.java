package com.demo.app.jcolibri.extensions.recommendation.navigationByAsking;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.method.gui.editors.ParameterEditor;
import jcolibri.method.gui.editors.ParameterEditorFactory;
import jcolibri.method.gui.utils.LayoutUtils;
import jcolibri.method.gui.utils.WindowUtils;
import jcolibri.util.AttributeUtils;

/**
 * Obtains the query asking for the value of an attribute.
 * 
 * @author Juan A. Recio-Garcia
 * @author Developed at University College Cork (Ireland) in collaboration with Derek Bridge.
 * @version 1.0
 *
 */
public class ObtainQueryWithAttributeQuestionMethod
{
    private static JDialog dialog;
    
    /**
     * Modifies the query with value of an attribute. It shows a dialog
     * to obtain that value .<br>
     * If the attribute parameter is null, this method does nothing and returns false.
     * It returns true in a.o.c. This servers to know when there are not more paramethers
     * to be asked and the conversation must finish. The condition of finishing a conversation
     * is calculated by the SelectAttributeMethod executed before. The Attribute
     * returned by these methods is the input "attribute" here. If a SelectAttributMethod
     * returns null, it means that there are not any other attributes to ask.
     *  
     * @param query to modify.
     * @param attribute to ask.
     * @param labels for the attribute.
     * @param cases used to find the available values presented to the user.
     * @return true 
     */
    public static boolean obtainQueryWithAttributeQuestion(CBRQuery query, Attribute attribute, Map<Attribute, String> labels, Collection<CBRCase> cases)
    {
	if(attribute == null)
	    return false;
	
	dialog = new JDialog();
	dialog.setModal(true);
	dialog.setTitle("Enter query value");
	
	String info = "Please specify following property to focus the retrieval: ";
	Object currentValue = AttributeUtils.findValue(attribute, query);
	if(currentValue != null)
	    info+="(current value = "+currentValue+")";
	JLabel infoLabel = new JLabel(info);
	
	JPanel main = new JPanel();
	main.setLayout(new SpringLayout());
	String label = labels.get(attribute);
	if(label==null)
	    label = attribute.getName();
	main.add(new JLabel(label));
	
	HashSet<Object> values = new HashSet<Object>();
	for(CBRCase c: cases)
	    values.add(AttributeUtils.findValue(attribute, c));
	
	ParameterEditor pe = ParameterEditorFactory.getEditor(attribute.getType(), values);
	main.add(pe.getJComponent());
	

	JButton ok = new JButton("OK");
	ok.addActionListener(new ActionListener(){
	    public void actionPerformed(ActionEvent arg0)
	    {
		dialog.setVisible(false);
	    }});
	JPanel southPanel = new JPanel();
	southPanel.add(ok);
	
	dialog.getContentPane().setLayout(new BorderLayout());
	dialog.getContentPane().add(infoLabel,BorderLayout.NORTH);
	dialog.getContentPane().add(main, BorderLayout.CENTER);
	dialog.getContentPane().add(southPanel, BorderLayout.SOUTH);

	
	//dialog.setSize(new Dimension(500,170));
	LayoutUtils.makeCompactGrid(main, 1, 2, 5, 5, 5, 5);
	dialog.pack();
	WindowUtils.centerWindow(dialog);
	dialog.setVisible(true);
	
	AttributeUtils.setValue(attribute, query, pe.getEditorValue());
	return true;
    }
}
