/**
 * CarrotClusterer.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 17/05/2007
 */
package com.demo.app.jcolibri.extensions.textual.carrot2;


import java.util.HashMap;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Searcher;
import org.carrot2.core.LocalComponent;
import org.carrot2.core.LocalComponentFactory;
import org.carrot2.core.LocalController;
import org.carrot2.core.LocalControllerBase;
import org.carrot2.core.LocalInputComponent;
import org.carrot2.core.LocalProcessBase;
import org.carrot2.core.ProcessingResult;
import org.carrot2.core.impl.ArrayOutputComponent;
import org.carrot2.filter.lingo.local.LingoLocalFilterComponent;
import org.carrot2.input.lucene.LuceneLocalInputComponent;
import org.carrot2.input.lucene.LuceneSearchConfig;

import jcolibri.extensions.textual.lucene.LuceneDocument;
import jcolibri.extensions.textual.lucene.LuceneIndex;
import jcolibri.util.ProgressController;

/**
 * Clusters documents using the Carrot2 framework. 
 * This framework uses Lucene to index and retrieve relevant documents for a query, 
 * and then cluster them assigning a "descriptive label" for each one.
 * 
 * <p>
 * To learn how to use this class see the TestCarrot example.
 * 
 * @author Juan A. Recio-Garc�a
 * @version 1.0
 * @see jcolibri.extensions.textual.lucene.LuceneIndex
 * @see jcolibri.extensions.textual.carrot2.TestCarrot
 */
public class CarrotClusterer {
	
    LocalController controller;
    LuceneIndex index;

    /**
     * Creates a Carrot Clusterer for the given Lucene Index. 
     * @param index Index of documents
     * @param searchFields Fields where search inside the document. Each lucene index is divided in several fields an the search can be performed in some of them.
     */
	public CarrotClusterer(LuceneIndex index, String[] searchFields)
	{
		this(index,searchFields, -1);
	}
    
    /**
     * Creates a Carrot Clusterer for the given Lucene Index that returns a maximum number of documents in each search.
     * @param index Index of documents
     * @param searchFields Fields where search inside the document. Each lucene index is divided in several fields an the search can be performed in some of them.
     * @param maxclusters Max number of clusters to return (approximately).
     */
	public CarrotClusterer(LuceneIndex index, String[] searchFields, int maxclusters)
	{
		this.index = index;
        try{
        	final int _maxclusters = maxclusters;
        	
        	controller = new LocalControllerBase();
        	
			Searcher searcher  = new IndexSearcher(index.getDirectory());
	
	        // Create an Analyzer. This must be the same analyzer as the one
	        // used to create your index. We use a standard analyzer here.
	        final Analyzer analyzer = new StandardAnalyzer();
	
	        // Define your field configuration here. Search fields are the
	        // fields used to retrieve matching documents when you query
	        // Lucene through Carrot<sup>2</sup>. Title, URL and summary
	        // fields are used for retriving data to be clustered (the URL
	        // field is used for document identification, actually).
	        final String urlField = searchFields[0];
	        final String titleField = LuceneDocument.ID_FIELD;
	        final String summaryField = searchFields[0];
	
	        final LuceneSearchConfig luceneConfig = new LuceneSearchConfig(
	                searcher, analyzer, searchFields,
	                titleField, summaryField, urlField); 
	
	        //
	        // Create Lucene input component factory.
	        //
	        final LocalComponentFactory input = new LocalComponentFactory() {
	            public LocalComponent getInstance() {
	                return new LuceneLocalInputComponent(luceneConfig);
	            }
	        };
	        
	        // add lucene input as 'lucene-myindex'
	        controller.addLocalComponentFactory("lucene-myindex", input);
	
	
	        //
	        // Now it's time to create filters. We will use Lingo clustering
	        // component. 
	        //
	        final LocalComponentFactory lingo = new LocalComponentFactory() {
	            public LocalComponent getInstance() {
	                // we will use the defaults here, see {@link Example}
	                // for more verbose configuration.
	                final HashMap<String,String> parameters = new HashMap<String,String>();
	                parameters.put("lsi.threshold.clusterAssignment", "0.01");
	                parameters.put("lsi.threshold.candidateCluster",  "3.5");
	                if(_maxclusters>0)
	                	parameters.put("clusters.num", String.valueOf(_maxclusters));
	
	                return new LingoLocalFilterComponent(null, parameters);
	            }
	        };
	
	        // add the clustering component as "lingo-classic"
	        controller.addLocalComponentFactory("lingo-classic", lingo);
	
	        
	        //
	        // Finally, create a result-catcher component
	        //
	        final LocalComponentFactory output = new LocalComponentFactory() {
	            public LocalComponent getInstance() {
	                return new ArrayOutputComponent();
	            }
	        };
	
	        // add the output component as "buffer"
	        controller.addLocalComponentFactory("buffer", output);
	
	        
	        //
	        // In the final step, assemble a process from the above.
	        //

            controller.addProcess("lucene-lingo", 
                    new LocalProcessBase("lucene-myindex", "buffer", new String [] {"lingo-classic"}));
        } catch (Exception e) {
			org.apache.commons.logging.LogFactory.getLog(this.getClass()).error(e);
        }
	}
	
	/**
	 * Clusters the documents for the given query.
	 */
	public CarrotClusteringResult cluster(String query)
	{
		return cluster(query,-1);
	}

	/**
	 * Clusters the documents for the given query, retrieving a maximum of documents from Lucene.
	 */
	public CarrotClusteringResult cluster(String query, int maxResults)
	{
        try {
			final HashMap<String,String> params = new HashMap<String,String>();
			if(maxResults>-1)
				params.put(LocalInputComponent.PARAM_REQUESTED_RESULTS, Integer.toString(maxResults));
			ProgressController.init(this.getClass(),"Carrot2. Clustering documents", -1);
			ProgressController.step(this.getClass());
			final ProcessingResult pResult = controller.query("lucene-lingo", query, params);
			final ArrayOutputComponent.Result result = (ArrayOutputComponent.Result) pResult.getQueryResult();
			ProgressController.finish(this.getClass());
			return new CarrotClusteringResult(result, index);
		} catch (Exception e) {
			org.apache.commons.logging.LogFactory.getLog(this.getClass()).error(e);
		}
		return null;
        
	}
}
