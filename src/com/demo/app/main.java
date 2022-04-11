package com.demo.app;

import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRQuery;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;
import es.ucm.fdi.gaia.jcolibri.exception.ExecutionException;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.NNConfig;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.global.Average;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.local.Equal;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.local.Interval;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.RetrievalResult;

import java.util.Collection;


public class main{
    public static void main(String[] args) {

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
            query.setDescription((CaseComponent) queryDesc);
            Test4 test4 =  new Test4() {
                @Override
                public void cycle(CBRQuery cbrQuery) throws ExecutionException {

                }
            };
            test4.configure();
            test4.preCycle();
            test4.cycle(query);


            //	System.out.println("Cycle finished. Type exit to idem");
            //}while(!reader.readLine().equals("exit"));

            test4.postCycle();

        } catch (ExecutionException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }//main

}
