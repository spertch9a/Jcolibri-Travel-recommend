/**
 * DisplayCasesMethod.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 24/10/2007
 */
package com.demo.app.jcolibri.extensions.recommendation.casesDisplay;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CaseComponent;
import jcolibri.exception.AttributeAccessException;
import jcolibri.method.gui.utils.LayoutUtils;
import jcolibri.method.gui.utils.WindowUtils;
import jcolibri.util.AttributeUtils;

/**
 * Displays the cases in a panel with a "previous" and "next" buttons to 
 * move through the cases.<br> 
 * This method is useful when showing composed cases.<br> 
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
public class DisplayCasesMethod
{
    /** Shown cases */
    private static CBRCase[] _cases;
    /** ith case currently shown */
    private static int i = 0;
    
    /** Main panel */
    private static JPanel casesPanel;
    /** Dialog object */
    private static JDialog dialog;
    /** Option to return */
    private static int returnCode = UserChoice.QUIT;
    /** Selected case to return */
    private static CBRCase selectedCase = null;
    
    /**
     * Shows the dialog without the "Edit Query" option
     * @param cases to display
     * @return UserChoice object
     */
    public static UserChoice displayCases(Collection<CBRCase> cases)
    {
	return displayCases(cases, false);
    }
    
    /**
     * Shows the dialog without the "Edit Query" option
     * @param cases to display
     * @return UserChoice object
     */
    public static UserChoice displayCasesWithEditOption(Collection<CBRCase> cases)
    {
	return displayCases(cases, true);
    }
    
    /**
     * Shows the dialog and allows to choose if show the "Edit Query" option.
     * @param cases to display.
     * @param editQueryEnabled decides if show the "Edit Query" option.
     * @return UserChoice object.
     */
    public static UserChoice displayCases(Collection<CBRCase> cases, boolean editQueryEnabled)
    {
	_cases = new CBRCase[cases.size()];
	cases.toArray(_cases);
	dialog = new JDialog();
	dialog.setModal(true);
	
	JPanel main = new JPanel();
	main.setLayout(new BorderLayout());
	
	casesPanel = new JPanel();
	casesPanel.setLayout(new BoxLayout(casesPanel, BoxLayout.Y_AXIS));
	displayCase();
	main.add(casesPanel, BorderLayout.CENTER);
	
	JPanel actionsPanel = new JPanel();
	actionsPanel.setLayout(new BoxLayout(actionsPanel,BoxLayout.X_AXIS));
	
	JButton next = new JButton("Next >>");
	next.addActionListener(new ActionListener(){
	    public void actionPerformed(ActionEvent arg0)
	    {
		i = (i+1)%_cases.length;
		displayCase();
	    } 
	});
	JButton prev = new JButton("<< Previous");
	prev.addActionListener(new ActionListener(){
	    public void actionPerformed(ActionEvent arg0)
	    {
		i = (i-1)%_cases.length;
		displayCase();
	    } 
	}); 
	JButton ok = new JButton("Add to Basket");
	ok.addActionListener(new ActionListener(){
	    public void actionPerformed(ActionEvent arg0)
	    {
		returnCode = UserChoice.BUY;
		selectedCase = _cases[i];
		dialog.setVisible(false);
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
	JButton refine = new JButton("Edit Query");
	refine.addActionListener(new ActionListener(){
	    public void actionPerformed(ActionEvent arg0)
	    {
		returnCode = UserChoice.REFINE_QUERY;
		dialog.setVisible(false);
	    } 
	});
	refine.setEnabled(editQueryEnabled);
	
	actionsPanel.add(ok);
	actionsPanel.add(quit);
	if(editQueryEnabled)
	    actionsPanel.add(refine);
	actionsPanel.add(Box.createHorizontalGlue());
	actionsPanel.add(prev);
	actionsPanel.add(next);
	
	main.add(actionsPanel, BorderLayout.SOUTH);
	
	dialog.getContentPane().add(main);
	dialog.pack();
	dialog.setSize(new Dimension((int)dialog.getSize().getWidth()+50, (int)dialog.getSize().getHeight()));
	WindowUtils.centerWindow(dialog);
	dialog.setTitle(cases.size()+" Retrieved cases");
	dialog.setVisible(true);
	
	return new UserChoice(returnCode, selectedCase);
    }

    /**
     * Displays a case
     */
    private static void displayCase()
    {
	casesPanel.removeAll();
	CBRCase c = _cases[i];
	
	displayCaseComponent(c.getDescription(),"Description",casesPanel);
	displayCaseComponent(c.getSolution(),"Solution",casesPanel);
	displayCaseComponent(c.getJustificationOfSolution(),"Justification of Solution",casesPanel);
	displayCaseComponent(c.getResult(),"Result",casesPanel);
	
	casesPanel.validate();
	casesPanel.repaint();

	dialog.setTitle("Case "+(i+1)+"/"+_cases.length);
    }
    
    /**
     * Displays a case component
     * @param cc is the case component to display
     * @param title of the panel
     * @param parentPanel is the parent panel. 
     */
    private static void displayCaseComponent(CaseComponent cc, String title, JPanel parentPanel)
    {
	if(cc==null)
	    return;
	
	JPanel panel = new JPanel();
	panel.setBorder(javax.swing.BorderFactory.createTitledBorder(title));
	
	panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
	JPanel simplePanel = new JPanel();
	simplePanel.setLayout(new SpringLayout());
	
	JPanel compoundPanel = new JPanel();
	compoundPanel.setLayout(new BoxLayout(compoundPanel,BoxLayout.Y_AXIS));

	try
	{
	    Attribute[] ats = jcolibri.util.AttributeUtils.getAttributes(cc.getClass());
	    
	    ArrayList<Attribute> compounds = new ArrayList<Attribute>();
	    int sAtts = 0;
	    for(Attribute a: ats)
	    {
	        if(a.getType().equals(CaseComponent.class))
	        {
	            compounds.add(a);
	        }
	        else
	        {
	            simplePanel.add(new JLabel(a.getName()));
	            simplePanel.add(new JLabel(a.getValue(cc).toString()));
	            sAtts++;
	        }
	    }
	    LayoutUtils.makeCompactGrid(simplePanel, sAtts,2,5,5,15,5);
	    
	    
	    //Now process compounds
	    for(Attribute comp: compounds)
		displayCaseComponent((CaseComponent)comp.getValue(cc), comp.getName(),compoundPanel);
            
	    
	    panel.add(simplePanel);
	    panel.add(compoundPanel);
	    
	    casesPanel.add(panel);
	} catch (AttributeAccessException e)
	{
	    org.apache.commons.logging.LogFactory.getLog(AttributeUtils.class).error(e);
	}
    }

}
