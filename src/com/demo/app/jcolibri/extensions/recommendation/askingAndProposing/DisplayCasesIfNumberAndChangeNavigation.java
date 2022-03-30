/**
 * DisplayCasesIfNumberAndChangeNavigation.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 05/11/2007
 */
package com.demo.app.jcolibri.extensions.recommendation.askingAndProposing;

import java.util.Collection;

import jcolibri.cbrcore.CBRCase;

/**
 * Method that implements the display condition for the Expert Clerk system.<br>
 * See recommender 8 for details.<br>
 * If the number of cases is less than a threshold it returns true and sets the navigation mode to NbP.<br>
 * If the number of cases is more than a threshold it returns false and sets the navigation mode to NbA.
 * 
 * @author Juan A. Recio-Garcia
 * @author Developed at University College Cork (Ireland) in collaboration with Derek Bridge.
 * @version 1.0
 * @see jcolibri.test.recommenders.rec8.Houses8
 */
public class DisplayCasesIfNumberAndChangeNavigation
{
    /**
     * Navigation mode enum.<br>
     * NBA: Navigation by Asking.<br>
     * NBP: Navigation by Proposing.<br>
     * @author Juan A. Recio-Garcia
     * @version 1.0
     *
     */
    public enum NavigationMode {NBA, NBP}; 
    
    /**
     * If the number of cases is less than max it returns true and sets the navigation mode to NbP.<br>
     * If the number of cases is more than max it returns false and sets the navigation mode to NbA.
     * @param max is the threshold
     * @param cases is the working cases set
     */
    public static boolean displayCasesIfNumberAndChangeNavigation(int max, Collection<CBRCase> cases)
    {
	if(cases.size()<max)
	{
	    AskingAndProposingPreferenceElicitation.changeTo(NavigationMode.NBP);
	    return true;
	}
	else
	{
	    AskingAndProposingPreferenceElicitation.changeTo(NavigationMode.NBA);
	    return false;
	}
	
	

	    
    }
}
