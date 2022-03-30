/**
 * LuceneIndex.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 10/04/2007
 */
package com.demo.app.jcolibri.extensions.textual.lucene;


import java.io.File;
import java.io.IOException;
import java.util.Collection;

import jcolibri.util.ProgressController;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;

/**
 * This class wraps the Lucene inverted terms index. 
 * This structure stores in which documents appears a word.
 * <br>
 * It also mantains a hash table that allows to retrieve a document form the index given its ID.
 * <p>
 * There are two ways to store the index:
 * <ul>
 * <li>In the file system. It saves the index in a directory. It is slower but does not consume memory.
 * <li>In memory. It stores the index in memory. You will need very much RAM memory but it will work quickly. 
 * If you obtain an outOfMemoryException try the -Xms -Xmx VM params.
 * </ul>
 * @author Juan A. Recio-Garc�a
 * @version 2.0
 */
public class LuceneIndex{
	
	private Directory directory;
	private java.util.HashMap<String, LuceneDocument> docsMapping;

	/**
	 * Creates a LuceneIndex stored in the File System.
	 * @param directory to store the index once generated
	 * @param documents to index
	 */
	public LuceneIndex(File directory,  Collection<LuceneDocument> documents)
	{
		this.docsMapping = new java.util.HashMap<String, LuceneDocument>();

	    org.apache.commons.logging.LogFactory.getLog(LuceneIndex.class).info("Creating File System Index in: "+directory.getPath());
		
		try {
			this.directory = FSDirectory.getDirectory(directory);
		} catch (IOException e) {
			org.apache.commons.logging.LogFactory.getLog(LuceneIndex.class).error(e);
		}

		createIndex(documents);

	}
	
	/**
	 * Creates an index stored into memory.
	 * @param documents to index.
	 */
	public LuceneIndex(Collection<LuceneDocument> documents)
	{
		this.docsMapping = new java.util.HashMap<String, LuceneDocument>();
		org.apache.commons.logging.LogFactory.getLog(LuceneIndex.class).info("Creating In-Memory index");
		
	    this.directory = new RAMDirectory();
		createIndex(documents);
	}

	private void createIndex(Collection<LuceneDocument> documents)
	{
		try {
			
			IndexWriter writer = new IndexWriter(directory,  new StandardAnalyzer(), true);
		    
			org.apache.commons.logging.LogFactory.getLog(LuceneIndex.class).info("Indexing "+documents.size()+" documents.");
			ProgressController.init(this.getClass(),"Lucene. Indexing documents", documents.size());
			
			for(LuceneDocument doc: documents)
			{
				writer.addDocument(doc.getInternalDocument());
				docsMapping.put(doc.getDocID(), doc);
				ProgressController.step(this.getClass());
			}		    
			org.apache.commons.logging.LogFactory.getLog(LuceneIndex.class).info("Optimizing index.");
			
			writer.optimize();
		    writer.close();
		    ProgressController.finish(this.getClass());
		} catch (Exception e) {
			org.apache.commons.logging.LogFactory.getLog(LuceneIndex.class).error(e);		
		}
	}

	
	
	
	/**
	 * @return the directory
	 */
	public Directory getDirectory() {
		return directory;
	}
	
	
	
		
	public int getNumberOfDocuments()
	{
		return docsMapping.size();
	}
	
	public LuceneDocument getDocument(String docId)
	{
		return docsMapping.get(docId);
	}
}
