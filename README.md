# Jcolibri Travel recommend
 
 
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
 * @see TravelDescription
 * @see Region
 * @see jcolibri.test.test5.TravelSolution
 * @see jcolibri.method.reuse.NumericDirectProportionMethod
 *
 */
