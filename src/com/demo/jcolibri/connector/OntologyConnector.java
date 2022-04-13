/**
 * OntologyConnector.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 07/06/2007
 */
package com.demo.jcolibri.connector;

import java.io.FileWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CaseBaseFilter;
import jcolibri.cbrcore.CaseComponent;
import jcolibri.cbrcore.Connector;
import jcolibri.connector.ontologyutils.OntologyInfo;
import jcolibri.connector.ontologyutils.OntologyMapping;
import jcolibri.datatypes.Instance;
import jcolibri.exception.InitializingException;
import jcolibri.util.FileIO;
import jcolibri.util.ProgressController;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import es.ucm.fdi.gaia.ontobridge.OntoBridge;
import es.ucm.fdi.gaia.ontobridge.OntologyDocument;

/**
 * Implements a generic Ontology connector.
 * It uses OntoBridge to manage the ontologies and the reasoner.
 * To configure this connector create a configuration xml file following this schema:
 * <a href="OntologyConnector.xsd">/doc/configfilesSchemas/OntologyConnector.xsd</a>:<p>
 * <img src="OntologyConnectorSchema.jpg">
 * <p>
 * This connector only maps case structures without compound attributes.
 * All attributes must be Instance typed.
 * 
 * For a complete example see Test 10.
 * 
 * @author Juan A. Recio-Garcia
 * @version 2.0
 * @see jcolibri.test.test10.Test10
 */
public class OntologyConnector implements Connector {
	
	private Class descriptionClass;
	private Class solutionClass;
	private Class justOfSolutionClass;
	private Class resultClass;
	
	private OntologyInfo mainOntologyInfo;
	private ArrayList<OntologyInfo> subOntologiesInfo;
	
	private String CaseMainConcept;
	
	private ArrayList<OntologyMapping> descriptionMappings;
	private ArrayList<OntologyMapping> solutionMappings;
	private ArrayList<OntologyMapping> justOfSolutionMappings;
	private ArrayList<OntologyMapping> resultMappings;
	
	private boolean modified;
	
	private OntologyInfo getOntologyInfo(Node node)
	{
		OntologyInfo oi = new OntologyInfo();
		NodeList ontologyNodes = node.getChildNodes();
		for(int i=0; i<ontologyNodes.getLength(); i++)
		{
			Node n = ontologyNodes.item(i);
			if(n.getNodeName().equals("URL"))
				oi.setUrl(n.getTextContent());
			else if(n.getNodeName().equals("LocalCopy"))
				oi.setLocalCopy(n.getTextContent());	
		}
		return oi;
	}
	
	private void getOntologyMappings(Node mappings, ArrayList<OntologyMapping> descriptionMappings) {
		NodeList mappingNodes = mappings.getChildNodes();
		for(int i=0; i<mappingNodes.getLength(); i++)
		{
			Node n = mappingNodes.item(i);
			if(!n.getNodeName().equals("Map"))
				continue;
			OntologyMapping om = new OntologyMapping();
			NodeList contents = n.getChildNodes();
			for(int j=0; j<contents.getLength(); j++)
			{
				Node c = contents.item(j);
				if(c.getNodeName().equals("Property"))
					om.setProperty(c.getTextContent());
				else if(c.getNodeName().equals("Concept"))
					om.setConcept(c.getTextContent());
				else if(c.getNodeName().equals("Attribute"))
					om.setAttribute(c.getTextContent());
			}
			descriptionMappings.add(om);
		}
	}
	
	/** 
	 * Initializes the connector from an XML config file.
	 * This method reads the configuration and launches OntoBridge with the Pellet reasoner.
	 * Then the ontologies are loaded into memory.
	 * 
	 * @see Connector#initFromXMLfile(URL)
	 */
	public void initFromXMLfile(URL file) throws InitializingException {
		
		
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(file.openStream());
			
			/* Main Ontology Info */
			mainOntologyInfo = getOntologyInfo(doc.getElementsByTagName("MainOntology").item(0));

			/* SubOntologies Info */
			subOntologiesInfo = new ArrayList<OntologyInfo>();
			NodeList subOntologiesNodes = doc.getElementsByTagName("SubOntology");
			for(int i=0; i<subOntologiesNodes.getLength(); i++)
				subOntologiesInfo.add(getOntologyInfo(subOntologiesNodes.item(i)));
			
			/* Case Main Concept */
			this.CaseMainConcept = doc.getElementsByTagName("CaseMainConcept").item(0).getTextContent();
			
			
			/* Description mapping */
			this.descriptionClass = Class.forName(doc.getElementsByTagName("DescriptionClassName").item(0).getTextContent());
			Node mappings = doc.getElementsByTagName("DescriptionMappings").item(0);
			this.descriptionMappings = new ArrayList<OntologyMapping>();
			getOntologyMappings(mappings, descriptionMappings);
			
			/* Solution mapping */
			try{
				this.solutionClass =  Class.forName(doc.getElementsByTagName("SolutionClassName").item(0).getTextContent());
				mappings = doc.getElementsByTagName("SolutionMappings").item(0);
				this.solutionMappings = new ArrayList<OntologyMapping>();
				getOntologyMappings(mappings, solutionMappings);
			}catch(Exception e) {}
			
			/* JustOfSolution mapping */
			try{
				this.justOfSolutionClass =  Class.forName(doc.getElementsByTagName("JustificationOfSolutionClassName").item(0).getTextContent());
				mappings = doc.getElementsByTagName("JustificationOfSolutionMappings").item(0);
				this.justOfSolutionMappings = new ArrayList<OntologyMapping>();
				getOntologyMappings(mappings, justOfSolutionMappings);
			}catch(Exception e) {}
			
			/* result mapping */
			try{
				this.resultClass =  Class.forName(doc.getElementsByTagName("ResultClassName").item(0).getTextContent());
				mappings = doc.getElementsByTagName("ResultMappings").item(0);
				this.resultMappings = new ArrayList<OntologyMapping>();
				getOntologyMappings(mappings, resultMappings);
			}catch(Exception e) {}
			
			
			// Now let's initialize Ontobridge
			
			// Obtain a reference to OntoBridge
			OntoBridge ob = jcolibri.util.OntoBridgeSingleton.getOntoBridge();
			// Configure it to work with the Pellet reasoner
			ob.initWithPelletReasoner();
			// Setup the main ontology
			OntologyDocument mainOnto = new OntologyDocument(this.mainOntologyInfo.getUrl(), 
			FileIO.findFile(this.mainOntologyInfo.getLocalCopy()).toExternalForm());
			// Setup subontologies
			ArrayList<OntologyDocument> subOntologies = new ArrayList<OntologyDocument>();
			for(OntologyInfo oi : this.subOntologiesInfo)
			{
				OntologyDocument subOnto = new OntologyDocument(oi.getUrl(), 
				FileIO.findFile(oi.getLocalCopy()).toURI().toString());
				subOntologies.add(subOnto);
			}
			
			// Load the ontology
			ob.loadOntology(mainOnto, subOntologies, false);
	
			// Set modified to false
			this.modified = false;
			
		} catch (Exception e) {
			throw new InitializingException(e);
		}
		
		
	}


	/* (non-Javadoc)
	 * @see jcolibri.cbrcore.Connector#retrieveAllCases()
	 */
	public Collection<CBRCase> retrieveAllCases() {

		//Result list
		ArrayList<CBRCase> cases = new ArrayList<CBRCase>();
		
		//Obtain OntoBridge
		OntoBridge ob = jcolibri.util.OntoBridgeSingleton.getOntoBridge();
		
		ProgressController.init(this.getClass(), "Loading concepts", ProgressController.UNKNOWN_STEPS);
		
		//Obtain instances
		Iterator<String> caseInstances =  ob.listInstances(this.CaseMainConcept);
		while(caseInstances.hasNext())
		{
			String caseInstance = caseInstances.next();
			CBRCase _case = new CBRCase();
			
			try {
				//Map description
				CaseComponent description = (CaseComponent)this.descriptionClass.newInstance();
				retrieveCaseComponent(ob, description, caseInstance, this.descriptionMappings);
				_case.setDescription(description);
				
				//Map solution
				if(this.solutionClass != null)
				{
					CaseComponent cc = (CaseComponent)this.solutionClass.newInstance();
					retrieveCaseComponent(ob, cc, caseInstance, this.solutionMappings);	
					_case.setSolution(cc);
				}
				
				//Map justification of solution
				if(this.justOfSolutionClass != null)
				{
					CaseComponent cc = (CaseComponent)this.justOfSolutionClass.newInstance();
					retrieveCaseComponent(ob, cc, caseInstance, this.justOfSolutionMappings);			
					_case.setJustificationOfSolution(cc);
				}
				
				//Map result solution
				if(this.resultClass != null)
				{
					CaseComponent cc = (CaseComponent)this.resultClass.newInstance();
					retrieveCaseComponent(ob, cc, caseInstance, this.resultMappings);
					_case.setResult(cc);
				}
				
				// If everything ok add the case to the list
				cases.add(_case);
				
			} catch (Exception e) {
				org.apache.commons.logging.LogFactory.getLog(this.getClass()).error(e);
			}
			
			ProgressController.step(this.getClass());
		}
		ProgressController.finish(this.getClass());
		return cases;
	}
	
	private void retrieveCaseComponent(OntoBridge ob, CaseComponent cc, String mainInstanceName, ArrayList<OntologyMapping> mappings) throws Exception
	{
		//Id
		Instance id = new Instance(mainInstanceName);
		cc.getIdAttribute().setValue(cc, id);
		
		//Other attributes
		for(OntologyMapping om: mappings)
		{
			// Obtain CaseComponent attribute
			Attribute at = new Attribute(om.getAttribute(), cc.getClass());
			
			// Find values of the property. It could have several values.
			Iterator<String> values = ob.listPropertyValue(mainInstanceName, om.getProperty());
			// Find which value is instance of the concept
			boolean found = false;
			while(values.hasNext() && !found)
			{
				String valueInstance = values.next();
				if(ob.isInstanceOf(valueInstance, om.getConcept()))
				{
					found = true;
					Instance concept = new Instance(valueInstance);
					at.setValue(cc, concept);
				}
			}
		}

	}

	/**
	 * UnImplemented.
	 * @see Connector#retrieveSomeCases(CaseBaseFilter)
	 */
	public Collection<CBRCase> retrieveSomeCases(CaseBaseFilter filter) {
		org.apache.commons.logging.LogFactory.getLog(this.getClass()).error("retrieveSomeCases(CaseBaseFilter) method is not yet implemented");
		return null;
	}

	/** 
	 * Stores cases into the ontology.
	 * @see Connector#storeCases(Collection)
	 */
	public void storeCases(Collection<CBRCase> cases) {
		
		if(cases.isEmpty())
			return;
		else
			modified = true;
		
		
		//Obtain OntoBridge
		OntoBridge ob = jcolibri.util.OntoBridgeSingleton.getOntoBridge();
		
		ProgressController.init(this.getClass(), "Storing concepts/cases", cases.size());
		for(CBRCase _case: cases)
		{
			try {
				if(!ob.existsInstance(_case.getID().toString(),this.CaseMainConcept))
					ob.createInstance(this.CaseMainConcept, _case.getID().toString());
				createCaseComponent(_case.getDescription(), this.descriptionMappings);
				createCaseComponent(_case.getSolution(), this.solutionMappings);
				createCaseComponent(_case.getJustificationOfSolution(), this.justOfSolutionMappings);
				createCaseComponent(_case.getResult(), this.resultMappings);
			} catch (Exception e) {
				org.apache.commons.logging.LogFactory.getLog(this.getClass()).error("Error storing case: "+_case+". Cause: "+ e.getMessage());
			}
			ProgressController.step(this.getClass());
		}
		ProgressController.finish(this.getClass());
	}
	
	private void createCaseComponent(CaseComponent cc, ArrayList<OntologyMapping> maps) throws Exception
	{
		if((cc == null)||(maps==null))
			return;
		
		OntoBridge ob = jcolibri.util.OntoBridgeSingleton.getOntoBridge();
		
		String mainInstance = cc.getIdAttribute().getValue(cc).toString();

		for(OntologyMapping om: maps)
		{
			
			Attribute at = new Attribute(om.getAttribute(), cc.getClass());
			String instance = at.getValue(cc).toString();
			if(!ob.existsInstance(instance,om.getConcept()))
				ob.createInstance(om.getConcept(), instance);
			ob.createOntProperty(mainInstance, om.getProperty(), instance);
		}
		
		
	}

	/**
	 * If there was any modification to the ontology, the owl file is replaced with a new one that contains the changes.
	 * The new owl file is completely regenerated from scrach with the current content of the reasoner (not including the inferred model).
	 * OntoBridge uses the RDF/XML-ABBREV syntax for the owl files.
	 * 
	 * @see Connector#close()
	 */
	public void close() {
		if(!modified)
			return;
		OntoBridge ob = jcolibri.util.OntoBridgeSingleton.getOntoBridge();
		try {
			ob.save(new FileWriter(FileIO.findFile(this.mainOntologyInfo.getLocalCopy()).getFile()));
		} catch (Exception e) {
			org.apache.commons.logging.LogFactory.getLog(this.getClass()).error(e);
		}

	}

	/**
	 * Deletes cases in the ontology. Only the main instance (case id mapped instance) is removed, so the instances mapped to attributes are keep. 
	 * @see Connector#deleteCases(Collection)
	 */
	public void deleteCases(Collection<CBRCase> cases) {

		if(cases.isEmpty())
			return;
		else
			modified = true;
		
		OntoBridge ob = jcolibri.util.OntoBridgeSingleton.getOntoBridge();
		
		ProgressController.init(this.getClass(), "Deleting concepts/cases", cases.size());
		for(CBRCase _case: cases)
		{
			ob.delete(_case.getID().toString());
			ProgressController.step(this.getClass());
		}
		ProgressController.finish(this.getClass());

	}

	
	
	/**************************************************************/
	/*********** Public API for the connector configuration       */
	/**************************************************************/

	
	/**
	 * @return Returns the caseMainConcept.
	 */
	public String getCaseMainConcept() {
		return CaseMainConcept;
	}

	/**
	 * @return Returns the descriptionMappings.
	 */
	public ArrayList<OntologyMapping> getDescriptionMappings() {
		return descriptionMappings;
	}

	/**
	 * @return Returns the justOfSolutionMappings.
	 */
	public ArrayList<OntologyMapping> getJustOfSolutionMappings() {
		return justOfSolutionMappings;
	}

	/**
	 * @return Returns the mainOntologyInfo.
	 */
	public OntologyInfo getMainOntologyInfo() {
		return mainOntologyInfo;
	}

	/**
	 * @return Returns the resultMappings.
	 */
	public ArrayList<OntologyMapping> getResultMappings() {
		return resultMappings;
	}

	/**
	 * @return Returns the solutionMappings.
	 */
	public ArrayList<OntologyMapping> getSolutionMappings() {
		return solutionMappings;
	}

	/**
	 * @return Returns the subOntologiesInfo.
	 */
	public ArrayList<OntologyInfo> getSubOntologiesInfo() {
		return subOntologiesInfo;
	}
	
	
	
}
