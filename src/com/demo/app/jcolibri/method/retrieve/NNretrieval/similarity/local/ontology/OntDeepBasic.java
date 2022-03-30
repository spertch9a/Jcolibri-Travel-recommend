/**
 * OntDeepBasic.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 13/06/2007
 */
package com.demo.app.jcolibri.method.retrieve.NNretrieval.similarity.local.ontology;

import java.util.ArrayList;

import jcolibri.datatypes.Instance;
import jcolibri.exception.NoApplicableSimilarityFunctionException;
import jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction;
import jcolibri.util.FileIO;
import es.ucm.fdi.gaia.ontobridge.OntoBridge;
import es.ucm.fdi.gaia.ontobridge.OntologyDocument;

/**
 * This function computes the fdeep_basic similarity. See package documentation
 * for details.
 * 
 * @author Juan A. Recio-Garcia
 * @version 1.0
 * @see jcolibri.method.retrieve.NNretrieval.similarity.local
 */
public class OntDeepBasic implements LocalSimilarityFunction
{

    	/*
         * (non-Javadoc)
         * 
         * @see jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction#compute(java.lang.Object,
         *      java.lang.Object, jcolibri.cbrcore.CaseComponent,
         *      jcolibri.cbrcore.CaseComponent, jcolibri.cbrcore.CBRCase,
         *      jcolibri.cbrcore.CBRQuery, java.lang.String)
         */
    public double compute(Object caseObject, Object queryObject) throws NoApplicableSimilarityFunctionException
    {
	if ((caseObject == null) || (queryObject == null))
	    return 0;
	if (!(caseObject instanceof Instance))
	    throw new NoApplicableSimilarityFunctionException(this.getClass(), caseObject.getClass());
	if (!(queryObject instanceof Instance))
	    throw new NoApplicableSimilarityFunctionException(this.getClass(), queryObject
		    .getClass());

	Instance i1 = (Instance) caseObject;
	Instance i2 = (Instance) queryObject;

	if (i1.equals(i2))
	    return 1;

	OntoBridge ob = jcolibri.util.OntoBridgeSingleton.getOntoBridge();

	double up = ob.maxProfLCS(i1.toString(), i2.toString());
	double down = ob.getMaxProf();

	return up / down;
    }

    /** Applicable to Instance */
    public boolean isApplicable(Object o1, Object o2)
    {
	if ((o1 == null) && (o2 == null))
	    return true;
	else if (o1 == null)
	    return o2 instanceof Instance;
	else if (o2 == null)
	    return o1 instanceof Instance;
	else
	    return (o1 instanceof Instance) && (o2 instanceof Instance);
    }

    /**
         * Testing method using test5 ontology
         */
    public static void main(String[] args)
    {
	try
	{
	    // Obtain a reference to OntoBridge
	    OntoBridge ob = jcolibri.util.OntoBridgeSingleton.getOntoBridge();
	    // Configure it to work with the Pellet reasoner
	    ob.initWithPelletReasoner();
	    // Setup the main ontology
	    OntologyDocument mainOnto = new OntologyDocument("http://gaia.fdi.ucm.es/ontologies/vacation.owl", FileIO
		    .findFile("jcolibri/test/test5/vacation.owl").toExternalForm());
	    // There are not subontologies
	    ArrayList<OntologyDocument> subOntologies = new ArrayList<OntologyDocument>();
	    // Load the ontology
	    ob.loadOntology(mainOnto, subOntologies, false);

	    OntDeepBasic sim = new OntDeepBasic();

	    System.out.println("deep_basic(CAR,TRAIN)=" + sim.compute(new Instance("CAR"), new Instance("TRAIN")));
	    System.out.println("deep_basic(CAR,IBIZA)=" + sim.compute(new Instance("CAR"), new Instance("IBIZA")));
	    System.out.println("deep_basic(CAR,I101)=" + sim.compute(new Instance("CAR"), new Instance("I101")));

	} catch (Exception e)
	{
	    org.apache.commons.logging.LogFactory.getLog(OntDeepBasic.class).error(e);
	}
    }
}
