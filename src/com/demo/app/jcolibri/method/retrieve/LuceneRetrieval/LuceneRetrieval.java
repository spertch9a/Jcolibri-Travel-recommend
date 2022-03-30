/**
 * LuceneRetrieval.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 09/04/2007
 */
package com.demo.app.jcolibri.method.retrieve.LuceneRetrieval;

import java.util.ArrayList;
import java.util.Collection;

import jcolibri.casebase.IDIndexedLinealCaseBase;
import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRCaseBase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.datatypes.Text;
import jcolibri.extensions.textual.lucene.LuceneIndex;
import jcolibri.extensions.textual.lucene.LuceneSearchResult;
import jcolibri.extensions.textual.lucene.LuceneSearcher;
import jcolibri.method.retrieve.RetrievalResult;

/**
 * Method to retrieve cases using Lucene to compute the similarity with the query.
 * @author Juanan
 *
 */
public class LuceneRetrieval {

	/**
	 * This method retrieves cases using Lucene to compute the similarity with the query.
	 * It requires a LuceneIndex created with the LuceneIndexCreator method.
	 * @param casebase containing the cases
	 * @param query to compute the similarity with
	 * @param index precalculated lucene index
	 * @param searchField to invoke lucene (this attribute must be Text typed)
	 * @param normalized indicates if the results must be normalized to [0..1]
	 * @param k max number of retrieved cases
	 * @see jcolibri.method.precycle.LuceneIndexCreator
	 */
	public static Collection<RetrievalResult> LuceneRetrieve(CBRCaseBase casebase, CBRQuery query, LuceneIndex index, Attribute searchField, boolean normalized, int k)
	{
		ArrayList<RetrievalResult> res = new ArrayList<RetrievalResult>();
		Object queryString = jcolibri.util.AttributeUtils.findValue(searchField, query);
		if(!(queryString instanceof Text))
		{
			org.apache.commons.logging.LogFactory.getLog(LuceneRetrieval.class).error("Search field has not a Text value. Returning empty RetrievalResult list.");
			return res;
		}
		Text qs = (Text)queryString;
		String sf = searchField.getName();
		LuceneSearchResult lsr = LuceneSearcher.search(index, qs.toString(), sf);
		
		int max = lsr.getResultLength();
		if(k < max)
			max = k;
		
		for(int i=0; i<max; i++)
			res.add(new RetrievalResult(findCase(casebase, lsr.getDocAt(i).getDocID()), new Double(lsr.getDocScore(i, normalized))));
		return res;
	}
	
	private static CBRCase findCase(CBRCaseBase casebase, String descriptionID)
	{
		if(casebase instanceof IDIndexedLinealCaseBase) // O(1)
		{
			IDIndexedLinealCaseBase cb = (IDIndexedLinealCaseBase)casebase;
			return cb.getCase(descriptionID);
		}
		else // O(n)
		{
			for(CBRCase c: casebase.getCases())
			{
				try {
					Object descIDObj = c.getDescription().getIdAttribute().getValue(c.getDescription());
					String descID = (String)descIDObj;
					if(descID.equals(descriptionID))
						return c;
				} catch (Exception e) {
					org.apache.commons.logging.LogFactory.getLog(LuceneRetrieval.class).error(e);
				}
				
			}
		}
		return null;
	}
}
