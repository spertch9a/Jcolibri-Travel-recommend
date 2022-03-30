/**
 * DisplayCasesTableMethod.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 25/10/2007
 */
package com.demo.app.jcolibri.extensions.recommendation.navigationByProposing;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
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
import jcolibri.extensions.recommendation.casesDisplay.UserChoice;
import jcolibri.extensions.recommendation.casesDisplay.utils.RadioButtonEditor;
import jcolibri.extensions.recommendation.casesDisplay.utils.RadioButtonTableRenderer;
import jcolibri.extensions.recommendation.navigationByAsking.ObtainQueryWithAttributeQuestionMethod;
import jcolibri.method.retrieve.FilterBasedRetrieval.predicates.Equal;
import jcolibri.method.retrieve.FilterBasedRetrieval.predicates.FilterPredicate;
import jcolibri.util.AttributeUtils;

/**
 * This method shows the cases in a table and also allows to show buttons with 
 * critiques.
 * <br>
 * It is an extension of jcolibri.extensions.recommendation.casesDisplay.DisplayCasesTableMethod used
 * in navigationByProposing recommenders.
 * <br>
 * This method enables and disables the critiques buttons depending on the values of the 
 * available cases. (For example, it has no sense to show a "creaper" button if there are
 * not cheaper cases).
 * Usually, displayed cases are the same than working cases, but when using diversity 
 * algorithms only three of the working cases are displayed.
 * 
 * @author Juan A. Recio-Garcia
 * @author Developed at University College Cork (Ireland) in collaboration with Derek Bridge.
 * @version 1.0
 *
 * @see jcolibri.extensions.recommendation.navigationByProposing.CriticalUserChoice
 * @see jcolibri.extensions.recommendation.casesDisplay.DisplayCasesTableMethod
 */
public class DisplayCasesTableWithCritiquesMethod
{
    private static JDialog dialog;
    
    private static ButtonGroup group;
    private static int returnCode = UserChoice.QUIT;

    private static Collection<CBRCase> displayedCases;
    private static Collection<CritiqueOption> displayedCritiques;
    private static Collection<CBRCase> _availableCases;
    
    private static Collection<CritiqueOption> userCritiques;
    private static JTable table;
    
    private static HashMap<CritiqueOption,JButton> critiquesMap;
    
    private static CBRCase critiquedQuery;
    
    
    /**
     * This method shows the cases in a table and also allows to show buttons with 
     * critiques.
     * @param cases to be shown
     * @param critiques to the cases (buttons are automatically generated from these critiques).
     * @param availableCases are the current working cases. Critiques are enabled depending on these cases.
     * @return a CriticalUserChoice object.
     */
    public static CriticalUserChoice displayCasesInTableWithCritiques(Collection<CBRCase> cases, Collection<CritiqueOption> critiques, Collection<CBRCase> availableCases )
    {
	displayedCases = cases;
	displayedCritiques = critiques;
	_availableCases = availableCases;
	critiquesMap = new HashMap<CritiqueOption, JButton>();
	
	dialog = new JDialog();
	dialog.setTitle(cases.size()+" Retrieved cases");
	dialog.setModal(true);
	

	userCritiques = new ArrayList<CritiqueOption>();
	
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
	{
	    JRadioButton rb = (JRadioButton) tm.getValueAt(i, 0);
	    group.add(rb);
	    DisplayCasesTableWithCritiquesMethod any = new DisplayCasesTableWithCritiquesMethod();
	    rb.addActionListener(any.new ItemRadioButtonListener(i));
	}
	
	
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
		    Iterator<CBRCase> iter = displayedCases.iterator();
		    CBRCase _case = iter.next();
		    for(int i=0; i<table.getSelectedRow(); i++)
			 _case = iter.next();
		    critiquedQuery = _case;
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

	actionsPanel.add(Box.createHorizontalGlue());
	actionsPanel.add(ok);
	actionsPanel.add(quit);
	actionsPanel.add(Box.createHorizontalGlue());
	
	JPanel critiquesPanel = new JPanel();
	critiquesPanel.setLayout(new BoxLayout(critiquesPanel,BoxLayout.X_AXIS));
	critiquesPanel.add(Box.createHorizontalGlue());
	for(CritiqueOption critique: critiques)
	{
	    JButton b = new JButton(critique.getLabel());
	    DisplayCasesTableWithCritiquesMethod any = new DisplayCasesTableWithCritiquesMethod();
	    b.addActionListener(any.new CritiqueButtonAction(critique));
	    critiquesMap.put(critique, b);
	    critiquesPanel.add(b);
	    critiquesPanel.add(Box.createHorizontalGlue());
	}
	critiquesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Critiques"));
	
	
	JPanel south = new JPanel();
	south.setLayout(new BoxLayout(south, BoxLayout.Y_AXIS));
	south.add(actionsPanel);
	south.add(critiquesPanel);
	mainPanel.add(south, BorderLayout.SOUTH);
	
	dialog.getContentPane().add(mainPanel);
	dialog.setSize(800, 600);
	jcolibri.method.gui.utils.WindowUtils.centerWindow(dialog);
	
	System.out.println("Available cases:");
	for(CBRCase c: _availableCases)
	    System.out.println(c);
	
	dialog.setVisible(true);

	
	return new CriticalUserChoice(returnCode, userCritiques, critiquedQuery);
    }
    
    /**
     * Disable critiques buttons depending on the selected case (row)
     * @param row of the table
     */
    private static void disableCritiques(int row)
    {
	Iterator<CBRCase> iter = displayedCases.iterator();
	CBRCase _case = iter.next();
	for(int i=0; i<row; i++)
	   _case = iter.next();
	
	for(CritiqueOption co : displayedCritiques)
	{
	    FilterPredicate fp = co.getPredicate();
	    Attribute a = co.getAttribute();
	    Object valueSelected = AttributeUtils.findValue(a, _case.getDescription());
	    boolean therearemore = false;
	    for(CBRCase cbCase : _availableCases)
	    {
		Object valueOther    = AttributeUtils.findValue(a, cbCase);
		try
		{
		    boolean res = fp.compute(valueOther,valueSelected);
		    if(fp instanceof Equal)
			res = !res;
		    if(res)
		    {
		        therearemore = true;
		        break;
		    }
		} catch (Exception e)
		{
		    org.apache.commons.logging.LogFactory.getLog(DisplayCasesTableWithCritiquesMethod.class).error(e);
		    
		}
	    }
	    critiquesMap.get(co).setEnabled(therearemore);
	}
	
    }
    
    /**
     * Returns the attributes of a case
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
     * Returns the attributes of a CaseComponent
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
     * Gets the column names from the names of the attributes of a case
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
     * Returns the names of the attributes of a CaseComponent.
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
    
    /**
     * Listener for the RadioButtons
     * @author Juan A. Recio-Garcia
     * @version 1.0
     */
    private class ItemRadioButtonListener implements ActionListener
    {
	int row = 0;
	public ItemRadioButtonListener(int row)
	{
	    this.row = row;
	}
	public void actionPerformed(ActionEvent arg0)
	{
	   JRadioButton rb = (JRadioButton)arg0.getSource();
	   if(rb.isSelected())
	       disableCritiques(row);
	}
	
    }
    
    /**
     * Listener for the critiques buttons.
     * @author Juan A. Recio-Garcia
     * @version 1.0
     *
     */
    private class CritiqueButtonAction implements ActionListener
    {
	CritiqueOption critique;
	public CritiqueButtonAction(CritiqueOption co)
	{
	    critique = co;
	}
	public void actionPerformed(ActionEvent arg0)
	{
	    if(table.getSelectedRowCount()<=0)
	    {
		JOptionPane.showMessageDialog(dialog, "You should choose one item", "Error", JOptionPane.ERROR_MESSAGE);
		return;
	    }

	    
	    Iterator<CBRCase> iter = displayedCases.iterator();
	    CBRCase _case = iter.next();
	    for(int i=0; i<table.getSelectedRow(); i++)
		 _case = iter.next();
	    
	    critiquedQuery = _case;
	    returnCode = UserChoice.REFINE_QUERY;
	
	    if(critique.getPredicate().getClass().equals(Equal.class))
	    {
		ObtainQueryWithAttributeQuestionMethod.obtainQueryWithAttributeQuestion(critiquedQuery, critique.getAttribute(), new HashMap<Attribute,String>(), _availableCases);
	    }
	    userCritiques.add(critique);
	    dialog.setVisible(false);

	}
	
    }
    
}
