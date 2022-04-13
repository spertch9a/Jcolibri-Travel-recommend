package com.demo.app;



import es.ucm.fdi.gaia.jcolibri.casebase.LinealCaseBase;
import es.ucm.fdi.gaia.jcolibri.cbraplications.StandardCBRApplication;
import es.ucm.fdi.gaia.jcolibri.cbrcore.*;
import es.ucm.fdi.gaia.jcolibri.connector.DataBaseConnector;
import es.ucm.fdi.gaia.jcolibri.exception.ExecutionException;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.NNConfig;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.global.Average;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.local.Equal;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.local.Interval;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.RetrievalResult;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.selection.SelectCases;



import java.util.Collection;


public abstract class Test4 implements StandardCBRApplication {

	Connector _connector;
	CBRCaseBase _caseBase;
	public CBRCaseBase mycases;

	public void configure() throws ExecutionException {
		try {
			// Create a data base connector
			_connector = (Connector) new DataBaseConnector();

			// Init the ddbb connector with the config file
			_connector.initFromXMLfile(FileIO.findFile("com/demo/app/databaseconfig.xml"));


			// Create a Lineal case base for in-memory organization
			_caseBase = (CBRCaseBase) new LinealCaseBase();

		} catch (Exception e) {
			throw new ExecutionException(e);
		}
//		try{
//		_connector = new DataBaseConnector();
//		_connector.initFromXMLfile(FileIO.findFile("databaseconfig.xml"));
////			_connector.initFromXMLfile(FileIO.findFile("jcolibri/test/test4/databaseconfig.xml"));
//
//			_caseBase  = (CBRCaseBase) new LinealCaseBase();
//		mycases = _caseBase;
//		} catch (Exception e){
//			throw new ExecutionException(e);
//		}
	}

	
	/* (non-Javadoc)
	 * @see jcolibri.cbraplications.BasicCBRApplication#preCycle()
	 */
	public CBRCaseBase preCycle() throws ExecutionException {
		_caseBase.init(_connector);	
		for(CBRCase c: _caseBase.getCases())
			System.out.println(c);
		return _caseBase;
	}
	
	/* (non-Javadoc)
	 * @see jcolibri.cbraplications.BasicCBRApplication#cycle()
	 */
	public void cycle(CBRQuery query) throws ExecutionException
	{		
		/********* NumericSim Retrieval **********/
		
		NNConfig simConfig = new NNConfig();
		simConfig.setDescriptionSimFunction(new Average());
		simConfig.addMapping(new Attribute("Accommodation", TravelDescription.class),
				new Equal());
		Attribute duration = new Attribute("Duration", TravelDescription.class);
		simConfig.addMapping(duration, new Interval(31));
		simConfig.setWeight(duration, 0.5);
		simConfig.addMapping(new Attribute("HolidayType", TravelDescription.class), new Equal());
		simConfig.addMapping(new Attribute("NumberOfPersons", TravelDescription.class), new Equal());
		
		simConfig.addMapping(new Attribute("Region",   TravelDescription.class), new Average());
		simConfig.addMapping(new Attribute("region",   Region.class), new Equal());
		simConfig.addMapping(new Attribute("city",     Region.class), new Equal());
		simConfig.addMapping(new Attribute("airport",  Region.class), new Equal());
		simConfig.addMapping(new Attribute("currency", Region.class), new Equal());

		
		System.out.println("Query:");
		System.out.println(query);
		System.out.println();
		//Blank description
		query.setDescription((CaseComponent) new TravelDescription());
		/********* Execute NN ************/
		Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(_caseBase.getCases(), query, simConfig);
//		Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(_caseBase.getCases(),
//				query,
//				simConfig);
		
		/********* Select cases **********/
		Collection<CBRCase> selectedcases = SelectCases.selectTopK(eval, 3);
		
//		/********* Reuse **********/
//		// Compute a direct proportion between the "NumberOfPersons" and "Price" attributes.
//		NumericDirectProportionMethod.directProportion(	new Attribute("NumberOfPersons",TravelDescription.class),
//													 	new Attribute("price",TravelSolution.class),
//													 	query, selectedcases);
//		// Compute a direct proportion between the "Duration" and "Price" attributes.
//		NumericDirectProportionMethod.directProportion(	new Attribute("Duration",TravelDescription.class),
//			 											new Attribute("price",TravelSolution.class),
//			 											query, selectedcases);
//
//
//		Collection<CBRCase> newcases = CombineQueryAndCasesMethod.combine(query, selectedcases);
//		System.out.println("Combined cases");
//		for(jcolibri.cbrcore.CBRCase c: newcases)
//			System.out.println(c);
//
//		/********* Revise **********/
//		CBRCase bestCase = newcases.iterator().next();
//
//		HashMap<Attribute, Object> componentsKeys = new HashMap<Attribute,Object>();
//		componentsKeys.put(new Attribute("caseId",TravelDescription.class), "test3id");
//		componentsKeys.put(new Attribute("id",TravelSolution.class), "test3id");
//		//componentsKeys.put(new Attribute("id",Region.class), 7);
//		jcolibri.method.revise.DefineNewIdsMethod.defineNewIdsMethod(bestCase, componentsKeys);
//
//		System.out.println("Case with new Id");
//		System.out.println(bestCase);
		
		/********* Retain **********/
		
//		 Uncomment next line to store cases into persistence
		//jcolibri.method.retain.StoreCasesMethod.storeCase(_caseBase, bestCase);
	}

	/* (non-Javadoc)
	 * @see jcolibri.cbraplications.BasicCBRApplication#postCycle()
	 */
	public void postCycle() throws ExecutionException {
		this._caseBase.close();

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {}


}


//Query definition