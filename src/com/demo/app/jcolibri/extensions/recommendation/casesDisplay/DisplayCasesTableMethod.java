/**
 * DisplayCasesTableMethod.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 25/10/2007
 */
package com.demo.app.jcolibri.extensions.recommendation.casesDisplay;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableModel;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CaseComponent;
import jcolibri.extensions.recommendation.casesDisplay.utils.RadioButtonEditor;
import jcolibri.extensions.recommendation.casesDisplay.utils.RadioButtonTableRenderer;
import jcolibri.util.AttributeUtils;

/**
 * Shows cases in a table allowing to select one.<br>
 * This method is not very suitable for composed cases
 * (cases with nested CaseComponents) because it doesn't
 * reflects that structure.<br>
 * It allways shows an "Add to Basket" and a "Quit" buttons. Optionally it
 * shows an "Edit Query" button for conversational recommenders (not useful 
 * in one-shot recommenders).<br>
 * The methods of this class return an UserChoice object.
 * 
 * @author Juan A. Recio-Garcia
 * @author Developed at University College Cork (Ireland) in collaboration with Derek Bridge.
 * @version 1.0
 * @see jcolibri.extensions.recommendation.casesDisplay.UserChoice
 */
public class DisplayCasesTableMethod
{
    private static JDialog dialog;
    
    private static ButtonGroup group;
    private static int returnCode = UserChoice.QUIT;
    private static CBRCase selectedCase = null;
    private static JTable table;
    
    /** Shown cases */
    private static CBRCase[] _cases;
    
    /**
     * Display options for the methods.
     * In BASIC mode the dialog only shows the buy and quit buttons.
     * The EDIT_QUERY option returns a UserChoice.EDIT_QUERY value without any case selected.
     * The SELECT_CASE option returns a UserChoice.EDIT_QUERY value but with a case selected from the list.
     * @author Juan A. Recio-Garcia
     * @version 1.0
     *
     */
    public enum DisplayOption {BASIC, EDIT_QUERY, SELECT_CASE};
    
    /**
     * Shows the dialog without the "Edit Query" option
     * @param cases to display
     * @return UserChoice object
     */
    public static UserChoice displayCasesInTableBasic(Collection<CBRCase> cases)
    {
	return displayCasesInTable(cases, DisplayOption.BASIC, null);
    }

    /**
     * Shows the dialog without the "Edit Query" option
     * @param cases to display
     * @return UserChoice object
     */
    public static UserChoice displayCasesInTableEditQuery(Collection<CBRCase> cases)
    {
	return displayCasesInTable(cases, DisplayOption.EDIT_QUERY, "Refine Query");
    }
    
    /**
     * Shows the dialog without the "Edit Query" option
     * @param cases to display
     * @return UserChoice object
     */
    public static UserChoice displayCasesInTableSelectCase(Collection<CBRCase> cases)
    {
	return displayCasesInTable(cases, DisplayOption.SELECT_CASE, "Something like this");
    }
    
    
    /**
     * Shows the dialog and allows to choose if show the "Edit Query" option.
     * @param cases to display.
     * @param editQueryEnabled decides if show the "Edit Query" option.
     * @param editQueryLabel is the label for the edit query button
     * @return UserChoice object.
     */
    static UserChoice displayCasesInTable(Collection<CBRCase> cases, DisplayOption displayOption, String optionLabel)
    {
	_cases = new CBRCase[cases.size()];
	cases.toArray(_cases);
	
	dialog = new JDialog();
	dialog.setTitle(cases.size()+" Retrieved cases");
	dialog.setModal(true);
	
	if(cases.size()==0)
	    return new UserChoice(UserChoice.REFINE_QUERY, selectedCase);

	Vector<Object> columnNames = extractColumnNames(cases.iterator().next());
	

	Vector<Object> rows = new Vector<Object>();
	for(CBRCase c: cases)
	    rows.add(getAttributes(c));
	
	table = new JTable(rows, columnNames){

	    private static final long serialVersionUID = 1L;

	    public void tableChanged(TableModelEvent e) {
		        super.tableChanged(e);
		        repaint();
	      }

	};
	
	table.getColumn("Select").setCellRenderer(
	        new RadioButtonTableRenderer());
	table.getColumn("Select").setCellEditor(
	        new RadioButtonEditor(new JCheckBox()));
	
	group = new ButtonGroup();
	TableModel tm = table.getModel();
	for(int i=0; i<tm.getRowCount();i++)
	    group.add((JRadioButton) tm.getValueAt(i, 0));
	
	
	JScrollPane scrollPane = new JScrollPane(table);
	table.setFillsViewportHeight(true);
	
	JPanel mainPanel = new JPanel();
	mainPanel.setLayout(new BorderLayout());
	mainPanel.add(scrollPane,BorderLayout.CENTER);
	
	JPanel actionsPanel = new JPanel();
	actionsPanel.setLayout(new BoxLayout(actionsPanel,BoxLayout.X_AXIS));
	
	JButton ok = new JButton("Add to Basket");
	ok.addActionListener(new ActionListener(){
	    public void actionPerformed(ActionEvent arg0)
	    {
		
		if(table.getSelectedRow() == -1)
		    JOptionPane.showMessageDialog(dialog, "You should choose one item", "Error", JOptionPane.ERROR_MESSAGE);
		else
		{
		    returnCode = UserChoice.BUY;
		    selectedCase = _cases[table.getSelectedRow()];
		    dialog.setVisible(false);
		}
	    } 
	});
	JButton quit = new JButton("Quit");
	quit.addActionListener(new ActionListener(){
	    public void actionPerformed(ActionEvent arg0)
	    {
		returnCode = UserChoice.QUIT;
		dialog.setVisible(false);
	    } 
	});
	JButton refine = new JButton(optionLabel);
	refine.addActionListener(new ActionListener(){
	    public void actionPerformed(ActionEvent arg0)
	    {
		
		returnCode = UserChoice.REFINE_QUERY;
		dialog.setVisible(false);
	    } 
	});
	JButton select = new JButton(optionLabel);
	select.addActionListener(new ActionListener(){
	    public void actionPerformed(ActionEvent arg0)
	    {
		if(table.getSelectedRow() == -1)
		    JOptionPane.showMessageDialog(dialog, "You should choose one item", "Error", JOptionPane.ERROR_MESSAGE);
		else
		{
		    returnCode = UserChoice.REFINE_QUERY;
		    selectedCase = _cases[table.getSelectedRow()];
		    dialog.setVisible(false);
		}
	    } 
	});	
	actionsPanel.add(Box.createHorizontalGlue());
	actionsPanel.add(ok);
	actionsPanel.add(quit);
	if(displayOption == DisplayOption.EDIT_QUERY)
	    actionsPanel.add(refine);
	if(displayOption == DisplayOption.SELECT_CASE)
	    actionsPanel.add(select);
	actionsPanel.add(Box.createHorizontalGlue());

	
	mainPanel.add(actionsPanel, BorderLayout.SOUTH);
	
	dialog.getContentPane().add(mainPanel);
	dialog.setSize(800, 200);
	jcolibri.method.gui.utils.WindowUtils.centerWindow(dialog);
	dialog.setVisible(true);

	
	return new UserChoice(returnCode, selectedCase);
    }
    
    /**
     * Returns a list with the values of the attributes of a case.
     * @param c the case
     * @return Vector of Objects
     */
    private static Vector getAttributes(CBRCase c)
    {
	Vector<Object> res = new Vector<Object>();
	
	JRadioButton rb = new JRadioButton(c.getID().toString());
	res.add(rb);
	
	getAttributes(c.getDescription(), res);
	getAttributes(c.getSolution(), res);
	getAttributes(c.getJustificationOfSolution(), res);
	getAttributes(c.getResult(), res);
	
	return res;
    }
    
    /**
     * Fills the list with the values of the attributes of the CaseComponent.
     * @param cc CaseComponent
     * @param res List to fill
     */
    private static void getAttributes(CaseComponent cc, Vector<Object> res)
    {
	Collection<Attribute> atts = AttributeUtils.getAttributes(cc);
	if(atts == null)
	    return;

	Attribute id = cc.getIdAttribute();
	for(Attribute a: atts)
	{
	    if(!a.equals(id))
		res.add(AttributeUtils.findValue(a, cc));
	}
    }
    
    /**
     * Obtains the column names of the tables from a case.
     * (It obtains the names of the attributes)
     * @param c is any case.
     * @return a list of objects
     */
    private static Vector<Object> extractColumnNames(CBRCase c)
    {
	Vector<Object> res = new Vector<Object>();
	res.add("Select");
	extractColumnNames(c.getDescription(),res);
	extractColumnNames(c.getSolution(),res);
	extractColumnNames(c.getJustificationOfSolution(),res);
	extractColumnNames(c.getResult(),res);
	return res;
    }
    
    /**
     * Extracts the column names (names of the attributes) from
     * a CaseComponent.
     * @param cc is the CaseComponent.
     * @param res List to fill.
     */
    private static void extractColumnNames(CaseComponent cc, Vector<Object> res)
    {
	Collection<Attribute> atts = AttributeUtils.getAttributes(cc);
	if(atts == null)
	    return;
	Attribute id = cc.getIdAttribute();
	for(Attribute a: atts)
	{
	    if(!a.equals(id))
		res.add(a.getName());
	}
    }
    
}
