/**
 * Test4.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-García.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 11/01/2007
 */
package jcolibri.test.test4;


import java.util.Collection;
import java.util.HashMap;

import jcolibri.casebase.LinealCaseBase;
import jcolibri.cbraplications.StandardCBRApplication;
import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRCaseBase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.cbrcore.Connector;
import jcolibri.connector.DataBaseConnector;
import jcolibri.exception.ExecutionException;
import jcolibri.method.retrieve.RetrievalResult;
import jcolibri.method.retrieve.NNretrieval.NNConfig;
import jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import jcolibri.method.retrieve.NNretrieval.similarity.global.Average;
import jcolibri.method.retrieve.NNretrieval.similarity.local.Equal;
import jcolibri.method.retrieve.NNretrieval.similarity.local.Interval;
import jcolibri.method.retrieve.selection.SelectCases;
import jcolibri.method.reuse.CombineQueryAndCasesMethod;
import jcolibri.method.reuse.NumericDirectProportionMethod;

/**
 * This example shows how to manage a complete case with solution and execute some adaptation methods.
 * Now the case has also a solution bean with a few attributes. That way, the structure of the case is:
 * <pre>
 * Case
 *  |
 *  +- Description
 *  |       |
 *  |       +- caseId
 *  |       +- HollidayType
 *  |       +- Price
 *  |       +- NumberOfPersons
 *  |       +- Region
 *  |       |     |
 *  |       |     +- regionId
 *  |       |     +- regionName
 *  |       |     +- NearestCity
 *  |       |     +- Airport
 *  |       |     +- Currency
 *  |       +- Transportation
 *  |       +- Duration
 *  |       +- Season
 *  |       +- Accomodation
 *  |       +- Hotel
 *  |
 *  +- Solution
 *          |
 *          +- id
 *          +- price
 *          +- hotel
 * </pre>
 * Solution is stored in the TravelSolution bean (CaseComponent). 
 * This bean could be saved into a separate table, but here were are going to show how to use the same table than the description.
 * This way, the mapping is:
 * <ul>
 * <li>Description is saved into the travel table.
 * <li>Solution is also saved into the travel table (using different columns, of course).
 * <li>Region (the compound attribute) of the description is saved into its own table region.
 * </ul>
 * Following picture shows how attibutes are mapped into the database:<p>
 * <img src="mappingTest4.jpg"/>
 * <p>
 * To configure these mapping we must modify or create the following files:
 * <ul>
 * <li><b>databaseconfig.xml</b><br>
 * In this file we include the solution class namea and its mapping file:
 * <pre>
 * &lt;SolutionMappingFile&gt;jcolibri/test/test4/TravelSolution.hbm.xml&lt;/SolutionMappingFile&gt;
 * &lt;SolutionClassName&gt;jcolibri.test.test4.TravelSolution&lt;/SolutionClassName&gt;
 * </pre>
 * <li><b>TravelSolution.hbm.xml</b><br>
 * This is a simple mapping file for the solution bean:
 * <pre>
 * &lt;hibernate-mapping default-lazy="false"&gt;
 *   &lt;class name="jcolibri.test.test4.TravelSolution" table="travel"&gt;
 *     &lt;id name="id" column="caseId"&gt;&lt/id&gt;
 *     &lt;property name="price" column="Price"/&gt;
 *     &lt;property name="hotel" column="Hotel"/&gt;	
 *   &lt;/class&gt;
 * &lt;/hibernate-mapping&gt;
 * </pre>
 * </ul>
 * Doing these changes the connector will manage the new case structure without problems.
 * <p>
 * This method also shows how to perform a simple adaptation based in the DirectProportion method that
 * modifies the value of an attribute of the solution depending on the value in the query and retrieved case of other attribute of the description.
 * 
 *
 * 
 * @author Juan A. Recio-Garcia
 * @version 1.0
 * 
 * @see jcolibri.test.test4.TravelDescription
 * @see jcolibri.test.test4.Region
 * @see jcolibri.test.test5.TravelSolution
 * @see jcolibri.method.reuse.NumericDirectProportionMethod
 *
 */
public class Test4 implements StandardCBRApplication {

	Connector _connector;
	CBRCaseBase _caseBase;
	
	/* (non-Javadoc)
	 * @see jcolibri.cbraplications.BasicCBRApplication#configure()
	 */
	public void configure() throws ExecutionException{
		try{
		_connector = new DataBaseConnector();
		_connector.initFromXMLfile(jcolibri.util.FileIO.findFile("jcolibri/test/test4/databaseconfig.xml"));
		_caseBase  = new LinealCaseBase();
		} catch (Exception e){
			throw new ExecutionException(e);
		}
	}

	
	/* (non-Javadoc)
	 * @see jcolibri.cbraplications.BasicCBRApplication#preCycle()
	 */
	public CBRCaseBase preCycle() throws ExecutionException {
		_caseBase.init(_connector);	
		for(jcolibri.cbrcore.CBRCase c: _caseBase.getCases())
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
		simConfig.addMapping(new Attribute("Accommodation", TravelDescription.class), new Equal());
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
		
		/********* Execute NN ************/
		Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(_caseBase.getCases(), query, simConfig);
		
		/********* Select cases **********/
		Collection<CBRCase> selectedcases = SelectCases.selectTopK(eval, 1);
		
		/********* Reuse **********/
		// Compute a direct proportion between the "NumberOfPersons" and "Price" attributes.
		NumericDirectProportionMethod.directProportion(	new Attribute("NumberOfPersons",TravelDescription.class), 
													 	new Attribute("price",TravelSolution.class), 
													 	query, selectedcases);
		// Compute a direct proportion between the "Duration" and "Price" attributes.
		NumericDirectProportionMethod.directProportion(	new Attribute("Duration",TravelDescription.class), 
			 											new Attribute("price",TravelSolution.class), 
			 											query, selectedcases);
		
		
		Collection<CBRCase> newcases = CombineQueryAndCasesMethod.combine(query, selectedcases);
		System.out.println("Combined cases");
		for(jcolibri.cbrcore.CBRCase c: newcases)
			System.out.println(c);
		
		/********* Revise **********/
		CBRCase bestCase = newcases.iterator().next();
		
		HashMap<Attribute, Object> componentsKeys = new HashMap<Attribute,Object>();
		componentsKeys.put(new Attribute("caseId",TravelDescription.class), "test3id");	
		componentsKeys.put(new Attribute("id",TravelSolution.class), "test3id");	
		//componentsKeys.put(new Attribute("id",Region.class), 7);	
		jcolibri.method.revise.DefineNewIdsMethod.defineNewIdsMethod(bestCase, componentsKeys);
		
		System.out.println("Case with new Id");
		System.out.println(bestCase);
		
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
	public static void main(String[] args) {
	    	// Launch DDBB manager
	    	jcolibri.test.database.HSQLDBserver.init();

		Test4 test4 = new Test4();
		try {
			test4.configure();
			test4.preCycle();
			
			//BufferedReader reader  = new BufferedReader(new InputStreamReader(System.in));			
			//do
			//{		
			/********* Query Definition **********/
			TravelDescription queryDesc = new TravelDescription();
			queryDesc.setAccommodation(TravelDescription.AccommodationTypes.ThreeStars);
			queryDesc.setDuration(10);
			queryDesc.setHolidayType("Recreation");
			queryDesc.setNumberOfPersons(4);
			
			Region region = new Region();
			region.setRegion("Bulgaria");
			region.setCity("Sofia");
			region.setCurrency("Euro");
			region.setAirport("airport");
			queryDesc.setRegion(region);
			
			CBRQuery query = new CBRQuery();
			query.setDescription(queryDesc);
			
			test4.cycle(query);
			
			
			//	System.out.println("Cycle finished. Type exit to idem");
			//}while(!reader.readLine().equals("exit"));
			
			test4.postCycle();
			
			//Shutdown DDBB manager
		    	jcolibri.test.database.HSQLDBserver.shutDown();

		} catch (ExecutionException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
