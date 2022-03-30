/**
 * LuceneTextSimilarity.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 25/06/2007
 */
package com.demo.app.jcolibri.method.retrieve.NNretrieval.similarity.local.textual;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.datatypes.Text;
import jcolibri.exception.NoApplicableSimilarityFunctionException;
import jcolibri.extensions.textual.lucene.LuceneIndex;
import jcolibri.extensions.textual.lucene.LuceneSearchResult;
import jcolibri.extensions.textual.lucene.LuceneSearcher;
import jcolibri.method.retrieve.LuceneRetrieval.LuceneRetrieval;
import jcolibri.method.retrieve.NNretrieval.similarity.InContextLocalSimilarityFunction;

/**
 * Computes the similarity between two texts using Lucene.
 * <br>
 * It is applicable to any Text object.
 * <br>
 * Requires the previous execution of the method jcolibri.method.precycle.LuceneIndexCreator to create
 * a LuceneIndex.
 * <br>
 * Test 13 shows how to use this similarity measure.
 * 
 * @author Juan A. Recio-Garcia
 * @version 1.0
 * @see Text
 * @see jcolibri.method.precycle.LuceneIndexCreator
 * @see jcolibri.test.test13.Test13b
 */
public class LuceneTextSimilarity extends InContextLocalSimilarityFunction
{
    LuceneSearchResult lsr = null;
    boolean normalized = false;
    
    /**
     * Creates a LuceneTextSimilarity object. This constructor pre-computes the similarity of the query with
     * the textaul attributes of the case (as these attributes are in the index). 
     * @param index Index that contains the attributes of the case
     * @param query query that will be compared
     * @param at textual attribute of the case or query object that is being compared
     * @param normalized if the Lucene result must be normalized to [0..1]
     */
    public LuceneTextSimilarity(LuceneIndex index, CBRQuery query, Attribute at, boolean normalized)
    {
	this.normalized = normalized;
	Object queryString = jcolibri.util.AttributeUtils.findValue(at, query);
	if(!(queryString instanceof Text))
	{
		org.apache.commons.logging.LogFactory.getLog(LuceneRetrieval.class).error("Search field has not a Text value. Returning empty RetrievalResult list.");
		return;
	}
	Text qs = (Text)queryString;
	String sf = at.getName();
	lsr = LuceneSearcher.search(index, qs.toString(), sf);
	
    }

    /* (non-Javadoc)
     * @see jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction#compute(java.lang.Object, java.lang.Object)
     */
    public double compute(Object caseObject, Object queryObject) throws NoApplicableSimilarityFunctionException
    {
	if ((caseObject == null) || (queryObject == null))
		return 0;
	if (!(caseObject instanceof Text))
		throw new NoApplicableSimilarityFunctionException(this.getClass(), caseObject.getClass());
	if (!(queryObject instanceof Text))
		throw new NoApplicableSimilarityFunctionException(this.getClass(), queryObject.getClass());
	
	return lsr.getDocScore(_case.getID().toString(), normalized);
    }

    /* (non-Javadoc)
     * @see jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction#isApplicable(java.lang.Object, java.lang.Object)
     */
    public boolean isApplicable(Object o1, Object o2)
    {
	if((o1==null)&&(o2==null))
		return true;
	else if(o1==null)
		return o2 instanceof Text;
	else if(o2==null)
		return o1 instanceof Text;
	else
		return (o1 instanceof Text)&&(o2 instanceof Text);
    }

}
