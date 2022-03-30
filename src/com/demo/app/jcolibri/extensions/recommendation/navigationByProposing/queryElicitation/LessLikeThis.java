/**
 * WeightedMoreLikeThis.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 02/11/2007
 */
package com.demo.app.jcolibri.extensions.recommendation.navigationByProposing.queryElicitation;

import java.util.Collection;
import java.util.HashSet;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.method.retrieve.FilterBasedRetrieval.FilterConfig;
import jcolibri.method.retrieve.FilterBasedRetrieval.predicates.NotEqualTo;
import jcolibri.util.AttributeUtils;

/**
 * The LessLikeThis strategy is a simple one: if the rejected cases all have 
 * the same feature-value combination, which is different from the preferred
 * case then this combination can be added as a negative condition. This 
 * negative condition is coded as a NotEqualTo(value) predicate in a 
 * FilterConfig object. The query is not modified.
 * That way, this method should be used together with FilterBasedRetrieval.
 * <p>See:
 * <p>L. McGinty and B. Smyth. Comparison-based recommendation. In ECCBR'02: 
 * Proceedings of the 6th European Conference on Advances in Case-Based
 * Reasoning, pages 575-589, London, UK, 2002. Springer-Verlag.
 * 
 * @see jcolibri.method.retrieve.FilterBasedRetrieval.FilterConfig
 * @author Juan A. Recio-Garcia
 * @author Developed at University College Cork (Ireland) in collaboration with Derek Bridge.
 * @version 1.0
 *
 */
public class LessLikeThis implements ComparisonQueryElicitation
{ 
    /******************************************************************************/
    /**                           STATIC METHODS                                 **/
    /******************************************************************************/
    
    /**
     * If the rejected cases all have 
     * the same feature-value combination, which is different from the preferred
     * case then this combination can be added as a negative condition. This 
     * negative condition is coded as a NotEqualTo(value) predicate in a 
     * FilterConfig object. The query is not modified.
     */
    public static void lessLikeThis(CBRQuery query, CBRCase selectedCase,Collection<CBRCase> proposedCases, FilterConfig filterConfig)
    {
	for(Attribute at: AttributeUtils.getAttributes(selectedCase.getDescription()))
	{
	    Object selectedValue = AttributeUtils.findValue(at, selectedCase);
	    HashSet<Object> alternatives = new HashSet<Object>();
	    for(CBRCase c: proposedCases)
	    {
		Object value = AttributeUtils.findValue(at, c);
		alternatives.add(value);
	    }
	    if(alternatives.size()!=1)
		return;
	    Object value = alternatives.iterator().next();
	    if(selectedValue==null)
	    {
		if(value == null)
		    return;
		else
		    filterConfig.addPredicate(at, new NotEqualTo(value));
	    }else if(!selectedValue.equals(value))
		filterConfig.addPredicate(at, new NotEqualTo(value));    
	}
	    
	    
    }
    
    
    /******************************************************************************/
    /**                           OBJECT METHODS                                 **/
    /******************************************************************************/

    private FilterConfig _filterConfig;
    
    public LessLikeThis(FilterConfig filterConfig)
    {
	_filterConfig = filterConfig;
    }
    
    /**
     * If the rejected cases all have 
     * the same feature-value combination, which is different from the preferred
     * case then this combination can be added as a negative condition. This 
     * negative condition is coded as a NotEqualTo(value) predicate in a 
     * FilterConfig object.
     */
    public void reviseQuery(CBRQuery query, CBRCase selectedCase, Collection<CBRCase> proposedCases)
    {
	lessLikeThis(query, selectedCase, proposedCases,_filterConfig);
    }
}
