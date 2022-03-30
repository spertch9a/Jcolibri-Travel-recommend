package com.demo.app;

import com.demo.app.jcolibri.cbrcore.Attribute;
import com.demo.app.jcolibri.cbrcore.CBRQuery;
import com.demo.app.jcolibri.exception.ExecutionException;
import com.demo.app.jcolibri.method.retrieve.FilterBasedRetrieval.predicates.Equal;
import com.demo.app.jcolibri.method.retrieve.NNretrieval.NNConfig;
import com.demo.app.jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import com.demo.app.jcolibri.method.retrieve.NNretrieval.similarity.global.Average;
import com.demo.app.jcolibri.method.retrieve.NNretrieval.similarity.local.Interval;
import com.demo.app.jcolibri.method.retrieve.RetrievalResult;

import java.util.Collection;

public class main{
    public static void main(String[] args) {
        // Launch DDBB manager
        jcolibri.test.database.HSQLDBserver.init();


        try {


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
            Test4 test4 = new Test4() {
                @Override
                public void cycle(CBRQuery query) throws ExecutionException {

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

                    /********* Execute NN ************/
                    Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(_caseBase.getCases(), query, simConfig);
                }
            };
            test4.configure();
            test4.preCycle();
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

    }//main

}
