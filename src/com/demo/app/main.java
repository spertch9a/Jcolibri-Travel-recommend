package com.demo.app;

import com.demo.app.jcolibri.cbrcore.CBRQuery;
import com.demo.app.jcolibri.exception.ExecutionException;

public class main{
    public static void main(String[] args) {
        // Launch DDBB manager
        HSQLDBserver.init();

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

    }//main

}
