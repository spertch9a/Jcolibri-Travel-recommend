/**
 * TestCarrot.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 17/05/2007
 */
package com.demo.app.jcolibri.extensions.textual.carrot2;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import javax.swing.JFileChooser;

import jcolibri.datatypes.Text;
import jcolibri.extensions.textual.carrot2.CarrotClusteringResult.Cluster;
import jcolibri.extensions.textual.lucene.LuceneDocument;
import jcolibri.extensions.textual.lucene.LuceneIndex;
import jcolibri.extensions.textual.wordnet.WordNetBridge;

/**
 * Class used to test and learn how to use Carrot2. 
 * It parses the documents of a directory and clusters them according to a query.
 * <p>
 * To avoid memory problems use the -Xms -Xmx VM params. For example to use a max of 1Gb of memory use: -Xms256m -Xmx1024m 
 * @author Juan A. Recio Garc�a.
 * @version 1.0
 *
 */
public class TestCarrot {

	// Unique field of the document.
	private static String CONTENT_FIELD = "content";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//Obtain the directory with the documents
		
	    JFileChooser jfc = new JFileChooser();
		jfc.setDialogType(JFileChooser.OPEN_DIALOG);
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		jfc.showOpenDialog(null);
		File docDir = jfc.getSelectedFile();

		if (!docDir.exists() || !docDir.canRead()) 
		{
	      System.out.println("Document directory '" +docDir.getAbsolutePath()+ "' does not exist or is not readable, please check the path");
	      System.exit(1);
	    }
	    
		// Convert the documents to the Lucene format.
		
		Collection<LuceneDocument> docs = null;;
		try {
			docs = indexDocs(docDir);
		} catch (IOException e) {
			org.apache.commons.logging.LogFactory.getLog(WordNetBridge.class).error(e);
		}
		
		
		try {
			CarrotClusteringResult ccr;
			
			// First create the Lucene inverted index
			LuceneIndex index = new LuceneIndex(docs);
			
			// Create the Carrot Clusterer for the unique field of the Lucene documents.
			String[] searchFields = { CONTENT_FIELD };
			CarrotClusterer clusterer = new CarrotClusterer(index, searchFields, 20);
			
			// Ask for the query
			String query = javax.swing.JOptionPane.showInputDialog("Query?");
			
			// Cluster the query
			ccr = clusterer.cluster(query);
		
			// Print the clusters
			int i=0;
			int total = ccr.getClusters().size();
			for(Cluster c: ccr.getClusters())
			{
				System.out.println("Cluster "+i+++"/"+total+": "+ c.getLabels());
				System.out.println(c.getDocs().size()+ " documents in cluster");
				for(LuceneDocument doc: c.getDocs())
					System.out.println("  "+doc.getDocID());
			}
			
		} catch (OutOfMemoryError e) {
			org.apache.commons.logging.LogFactory.getLog(WordNetBridge.class).error("Carrot2 requires more memory. Launch the JVM with these flags: java -Xms256m -Xmx512m ...");
		}
	}
	
	/**
	 * Converts the documents in the directory to the lucene format
	 */
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

		    // Put the content is our unique field	
	    	LuceneDocument doc = new LuceneDocument(f.getCanonicalPath());
	    	doc.addContentField(CONTENT_FIELD, new Text(sb.toString()));
	    	
	    	docs.add(doc);	
	    }
	    
	    return docs;
	       
	  }
}
