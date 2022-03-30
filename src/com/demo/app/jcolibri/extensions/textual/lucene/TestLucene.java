/**
 * TestLucene.java
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
import javax.swing.JFileChooser;

import jcolibri.datatypes.Text;
import jcolibri.extensions.textual.wordnet.WordNetBridge;


/**
 * Class used to test and learn how to use Lucene.
 * It reads documents from a directory and indexes them. Then allows to ask for documents.
 * <p>
 * To avoid memory problems use the -Xms -Xmx VM params. For example to use a max of 1Gb of memory use: -Xms256m -Xmx1024m 
 * @author Juan A. Recio-Garc�a
 * @version 1.0
 */
public class TestLucene {

	//Documents only have a field
	private static String CONTENT_FIELD = "content";
	
	/** Transforms files to Lucene documents. */
	private static Collection<LuceneDocument> indexDocs(File directory) throws IOException {
	    
		java.util.ArrayList<LuceneDocument> docs = new java.util.ArrayList<LuceneDocument>();
		
	    if (!directory.canRead())
	    	return docs;
	    
	    if (!directory.isDirectory())
	    	return docs;
	    
	    File[] files = directory.listFiles();
	    
	    for(File f: files)
	    {
	    	if(f.isDirectory())
	    		continue;
	    	java.io.BufferedReader fr = new java.io.BufferedReader(new java.io.FileReader(f));
	    	StringBuffer sb = new StringBuffer();
	    	while (fr.ready())
	    		sb.append(fr.readLine());
		    fr.close();

		    	
	    	LuceneDocument doc = new LuceneDocument(f.getCanonicalPath());
	    	doc.addContentField(CONTENT_FIELD, new Text(sb.toString()));
	    	
	    	docs.add(doc);	
	    }
	    
	    return docs;
	       
	  }

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//Obtain the files
		
		JFileChooser jfc = new JFileChooser();
		jfc.setDialogType(JFileChooser.OPEN_DIALOG);
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		jfc.showOpenDialog(null);
		File docDir = jfc.getSelectedFile();

		if (!docDir.exists() || !docDir.canRead()) {
	      System.out.println("Document directory '" +docDir.getAbsolutePath()+ "' does not exist or is not readable, please check the path");
	      System.exit(1);
	    }
	    
		//Transform the files to Lucene documents
		
		Collection<LuceneDocument> docs = null;;
		try {
			docs = indexDocs(docDir);
		} catch (IOException e) {
			org.apache.commons.logging.LogFactory.getLog(WordNetBridge.class).error(e);
		}
		
		try {
			
			// Create the Lucene Index
			LuceneIndex index = new LuceneIndex(docs);
			
			// Ask for a query
			String query = javax.swing.JOptionPane.showInputDialog("Query?");
			
			// Search
			LuceneSearchResult lsr = LuceneSearcher.search(index, query, CONTENT_FIELD);
			
			// Print results
			System.out.println("Results: "+lsr.getResultLength());
			for(int i=0; i<lsr.getResultLength(); i++)
			{
				System.out.println(lsr.getDocScore(i,true)+" -> "+lsr.getDocAt(i).getDocID());
			}
		} catch (OutOfMemoryError e) {
			org.apache.commons.logging.LogFactory.getLog(WordNetBridge.class).error("Lucene requires more memory. Launch the JVM with these flags: java -Xms256m -Xmx512m ...");
		}
	}

}
