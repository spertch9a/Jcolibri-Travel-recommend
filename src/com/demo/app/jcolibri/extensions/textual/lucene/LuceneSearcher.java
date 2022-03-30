/**
 * LuceneSearcher.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 10/04/2007
 */
package com.demo.app.jcolibri.extensions.textual.lucene;



import jcolibri.extensions.textual.lucene.spanish.SpanishAnalyzer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;

/**
 * Searchs for documents given a query and an index.
 * @author Juan A. Recio-Garc�a
 * @version 1.0
 */
public class LuceneSearcher {
	
	/**
	 * Performs a search using Lucene
	 * @param index with the documents to search
	 * @param query to search
	 * @param fieldName field where search inside the documents
	 * @return the search result
	 */
	public static LuceneSearchResult search(LuceneIndex index, String query, String fieldName)
	{	    
		try {

		    Searcher searcher = new IndexSearcher(index.getDirectory());
		    Analyzer analyzer = new StandardAnalyzer();
		    QueryParser parser = new QueryParser(fieldName, analyzer);
		    Query q = parser.parse(query);
		    Hits hits = searcher.search(q);
		    LuceneSearchResult lsr = new LuceneSearchResult(hits, index);
		    searcher.close();
		    return lsr;
			
		} catch (Exception e) {
			org.apache.commons.logging.LogFactory.getLog(LuceneSearcher.class).error(e);
		}
	    return null;
	}
	
	/**
	 * Performs a search over spanish texts using Lucene
	 * @param index with the documents to search
	 * @param query to search
	 * @param fieldName field where search inside the documents
	 * @return the search result
	 */
	public static LuceneSearchResult searchSpanish(LuceneIndex index, String query, String fieldName)
	{	    
		try {

		    Searcher searcher = new IndexSearcher(index.getDirectory());
		    Analyzer analyzer = new SpanishAnalyzer();
		    QueryParser parser = new QueryParser(fieldName, analyzer);
		    Query q = parser.parse(query);
		    Hits hits = searcher.search(q);
		    LuceneSearchResult lsr = new LuceneSearchResult(hits, index);
		    searcher.close();
		    return lsr;
			
		} catch (Exception e) {
			org.apache.commons.logging.LogFactory.getLog(LuceneSearcher.class).error(e);
		}
	    return null;
	}
}
