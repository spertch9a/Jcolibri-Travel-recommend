/**
 * AskingAndProposingPreferenceElicitation.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 05/11/2007
 */
package com.demo.app.jcolibri.extensions.recommendation.askingAndProposing;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.exception.ExecutionException;
import jcolibri.extensions.recommendation.askingAndProposing.DisplayCasesIfNumberAndChangeNavigation.NavigationMode;
import jcolibri.extensions.recommendation.navigationByAsking.ObtainQueryWithAttributeQuestionMethod;
import jcolibri.extensions.recommendation.navigationByAsking.SelectAttributeMethod;
import jcolibri.extensions.recommendation.navigationByProposing.CriticalUserChoice;
import jcolibri.extensions.recommendation.navigationByProposing.queryElicitation.MoreLikeThis;

/**
 * Method that implements the Preference elicitation task for the Expert Clerk system.<br>
 * See recommender 8 for details.<br>
 * In NbA it elicits the query asking for the value of an attribute.<br>
 * In NbP it uses the MoreLikeThis method.
 * 
 * @author Juan A. Recio-Garcia
 * @author Developed at University College Cork (Ireland) in collaboration with Derek Bridge.
 * @version 1.0
 * @see jcolibri.test.recommenders.rec8.Houses8
 */
public class AskingAndProposingPreferenceElicitation
{
    
    private static NavigationMode mode = NavigationMode.NBA;
    
    /** 
     * Changes the navigation type.
     */
    public static void changeTo(NavigationMode _mode)
    {
	mode = _mode;
    }
    
    /**
     * Executes the preference elicitation. <br>
     * If NbA mode it obtains a new query using the ObtainQueryWithAttributeQuestionMethod.<br>
     * If NBP mode it revises the query using MoreLikeThis.<br> 
     * @param query to elicit
     * @param cases is the working cases set
     * @param sam is the method used to obtain the next attribute to ask (only in NbA mode).
     * @param cuc is the user critique in NbP mode.
     * @throws ExecutionException if any error.
     */
    public static void doPreferenceElicitation(CBRQuery query, Collection<CBRCase> cases, SelectAttributeMethod sam, CriticalUserChoice cuc) throws ExecutionException
    {
	if(mode == NavigationMode.NBA)
	{
	    Attribute att = sam.getAttribute(cases, query);
	    Map<Attribute,String> labels = new HashMap<Attribute,String>();
	    ObtainQueryWithAttributeQuestionMethod.obtainQueryWithAttributeQuestion(query, att, labels,cases);
	} 
	else if(mode == NavigationMode.NBP)
	{
	    new MoreLikeThis().reviseQuery(query, cuc.getSelectedCase(), cases);
	}

    }
}
