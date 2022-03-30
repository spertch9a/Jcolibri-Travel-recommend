/**
 * CarrotClusteringResult.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 17/05/2007
 */
package com.demo.app.jcolibri.extensions.textual.carrot2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jcolibri.extensions.textual.lucene.LuceneDocument;
import jcolibri.extensions.textual.lucene.LuceneIndex;

import org.carrot2.core.clustering.RawCluster;
import org.carrot2.core.clustering.RawDocument;
import org.carrot2.core.impl.ArrayOutputComponent;

/**
 * Result of a clustering.
 * Uses an internal class "Cluster" that stores the requiered information for each cluster:
 * <ul>
 * <li>The labels assigned to the cluster.
 * <li>The documents that belong to the cluster (LuceneDocuments).
 * </ul>
 * 
 * @author Juan A. Recio-Garc�a
 * @version 1.0
 * @see jcolibri.extensions.textual.lucene.LuceneDocument
 */
public class CarrotClusteringResult {

	private ArrayList<Cluster> clusters;
	
	/**
	 * Internal class that stores the labels and documents for a cluster.
	 * @author Juan A. Recio-Garc�a
	 */
	public class Cluster
	{
		List<String> labels;
		List<LuceneDocument> docs;
		protected Cluster(List<String> labels, List<LuceneDocument> docs)
		{
			this.labels = labels;
			this.docs   = docs;
		}
		/**
		 * @return the documents of the cluster
		 */
		public List<LuceneDocument> getDocs() {
			return docs;
		}
		/**
		 * @return the labels of the cluster
		 */
		public List<String> getLabels() {
			return labels;
		}
		
		
	}
	
	/**
	 * Creates a CarrotClusteringResult object from the Carrot2 output.
	 */
	@SuppressWarnings("unchecked")
	protected CarrotClusteringResult(ArrayOutputComponent.Result result, LuceneIndex index)
	{
		clusters = new ArrayList<Cluster>();
		
        final List carrotClusters = result.clusters;
        for (Iterator i = carrotClusters.iterator(); i.hasNext(); )
        {
            RawCluster rawc = (RawCluster) i.next();
            List<String> labels = rawc.getClusterDescription();
            ArrayList<LuceneDocument> docs = new ArrayList<LuceneDocument>();
            for (Iterator d = rawc.getDocuments().iterator(); d.hasNext(); ) 
            {
                RawDocument document = (RawDocument) d.next();
                LuceneDocument ld = index.getDocument(document.getTitle());
                docs.add(ld);
            }
            
            Cluster c =  new Cluster(labels,docs);
            clusters.add(c);
        }

	}
	
	/**
	 * Returns the list of clusters.
	 */
	public List<Cluster> getClusters()
	{
		return this.clusters;
	}
	
}
