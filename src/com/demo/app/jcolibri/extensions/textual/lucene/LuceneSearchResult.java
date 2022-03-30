/**
 * LuceneSearchResult.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 10/04/2007
 */
package com.demo.app.jcolibri.extensions.textual.lucene;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.Hit;
import org.apache.lucene.search.Hits;

/**
 * Stores the result of a search.
 * It is a list of relevant documents with an assigned score.
 * @author Juan A. Recio-Garc�a
 *
 */
public class LuceneSearchResult {
	
	//Table that maps between the position in the result and the ID of the document.
	private String[] _pos2id;
	//Table that maps between the ID of a document and its position in the result
	private HashMap<String, Integer> _id2pos;
	//Table that maps between the position and the score
	private float[]  _pos2score; 
	//Number of returned documents
	private int resultLength;
	//Index used to search
	private LuceneIndex index;
	//Max score obtained
	private float maxscore;
	
	
	/**
	 * Transforms from Lucene result format.
	 */
	protected LuceneSearchResult(Hits hits, LuceneIndex index) throws IOException
	{
		this.index = index;
		resultLength = hits.length();
		_pos2id = new String[resultLength];
		_pos2score = new float[resultLength];
		_id2pos = new HashMap<String,Integer>(resultLength);
		
		maxscore = 0;
		int pos=0;
		for(Iterator hi = hits.iterator(); hi.hasNext();pos++)
		{
			Hit hit = (Hit)hi.next();
			Document doc = hit.getDocument();
			String id = doc.get(LuceneDocument.ID_FIELD);
			
			_pos2id[pos] = id;
			_pos2score[pos] = hit.getScore();
			_id2pos.put(id, new Integer(pos));
			
			if(hit.getScore()>maxscore)
				maxscore = hit.getScore();
		}
	}
	
	/**
	 * Gets the position of a document in the result given its ID.
	 * If that ID is not in the results set this methods returns -1
	 */
	public int getDocPosition(String docID)
	{
	    	Integer pos = _id2pos.get(docID);
	    	if(pos == null)
	    	    return -1;
	    	return pos;
	}
	
	/**
	 * Gets the document in a position inside the results list.
	 */
	public LuceneDocument getDocAt(int position)
	{
		if(position<resultLength)
			return index.getDocument(_pos2id[position]);
		return null;
	}
	
	/**
	 * Gets the score obtained by a document. 
	 * It can be normalized to [0..1], that way, the document with max score will have a 1 and the document with min score a 0.
	 */
	public float getDocScore(String docID, boolean normalized)
	{
		int pos = getDocPosition(docID);
		if(pos == -1)
		    return 0;
		return getDocScore(pos, normalized);
	}
		
	/**
	 * Gets the score obtained by a document located in a position of the result list.
	 * It can be normalized to [0..1], that way, the document with max score will have a 1 and the document with min score a 0.
	 */
	public float getDocScore(int position, boolean normalized)
	{
		if(position>=resultLength)
			return 0;
		if(normalized)
			return _pos2score[position]/maxscore;
		else
			return _pos2score[position];
	}
	
	
	/**
	 * Returns the content of a field of the document located in the given position.
	 * @param position of the document in the result list
	 * @param fieldName that stores the text to return
	 */
	public String getContent(int position, String fieldName)
	{
		if(position>=resultLength)
			return null;
		String id = _pos2id[position];
		return index.getDocument(id).getContentField(fieldName);
	}
	
	/**
	 * Returns the number of results
	 */
	public int getResultLength()
	{
		return this.resultLength;
	}
	
}
