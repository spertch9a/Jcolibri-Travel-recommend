package com.demo.second;

//https://gaia.fdi.ucm.es/research/colibri/jcolibri/doc/apidocs/src-html/es/ucm/fdi/gaia/jcolibri/test/test1/Test1.html




import com.demo.app.Region;
import es.ucm.fdi.gaia.jcolibri.casebase.LinealCaseBase;
import es.ucm.fdi.gaia.jcolibri.cbraplications.StandardCBRApplication;
import es.ucm.fdi.gaia.jcolibri.cbrcore.*;
import es.ucm.fdi.gaia.jcolibri.connector.DataBaseConnector;
import es.ucm.fdi.gaia.jcolibri.exception.ExecutionException;
import es.ucm.fdi.gaia.jcolibri.exception.InitializingException;
import es.ucm.fdi.gaia.jcolibri.method.retain.StoreCasesMethod;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.NNConfig;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.global.Average;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.local.Equal;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.local.Interval;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.RetrievalResult;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.selection.SelectCases;
import es.ucm.fdi.gaia.jcolibri.method.revise.DefineNewIdsMethod;
import es.ucm.fdi.gaia.jcolibri.util.FileIO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;



public class Test1 implements StandardCBRApplication {


    Connector _connector;
    CBRCaseBase _caseBase;
    ArrayList<RetrievalResult> cases;
    public Collection<CBRCase> casestoreturn;
    public Collection<CBRCase> casestoreturnfirst ;
    public Collection<RetrievalResult> evaltogather;

    public void configure() throws ExecutionException {

        // Create a data base connector
        _connector =  new DataBaseConnector();
//            // Init the ddbb connector with the config file
//           _connector.initFromXMLfile(jcolibri.util.FileIO.findFile("main/java/com/ouss/reanimation/model/databaseconfig.xml"));
        _connector.initFromXMLfile(FileIO.findFile("com/demo/second/databaseconfig.xml"));
        // Create a Lineal case base for in-memory organization
        _caseBase = (CBRCaseBase) new LinealCaseBase();
    }

    public CBRCaseBase mycases;

    public CBRCaseBase preCycle() {
        try {
            _caseBase.init(_connector);
        } catch (InitializingException e) {
            e.printStackTrace();
        }
        //ziyada
        mycases = _caseBase;
        for(CBRCase c: _caseBase.getCases())
            System.out.println(c);
        return _caseBase;
    }



    //    public void cycle(CBRQuery cbrQuery, int k)
    public void cycle(CBRQuery cbrQuery) {

        /********* NumericSim Retrieval **********/
//old cofing was here




        //end of old config



        //new config
        /********* NumericSim Retrieval **********/

        NNConfig simConfig = new NNConfig();
        simConfig.setDescriptionSimFunction(new Average());
        simConfig.addMapping(new Attribute("Accommodation", com.demo.app.TravelDescription.class),
                new Equal());
        Attribute duration = new Attribute("Duration", com.demo.app.TravelDescription.class);
        simConfig.addMapping(duration, new Interval(31));
        simConfig.setWeight(duration, 0.5);
        simConfig.addMapping(new Attribute("HolidayType", com.demo.app.TravelDescription.class), new Equal());
        simConfig.addMapping(new Attribute("NumberOfPersons", com.demo.app.TravelDescription.class), new Equal());

        simConfig.addMapping(new Attribute("Region",   com.demo.app.TravelDescription.class), new Average());
        simConfig.addMapping(new Attribute("region",   com.demo.app.Region.class), new Equal());
        simConfig.addMapping(new Attribute("city",     com.demo.app.Region.class), new Equal());
        simConfig.addMapping(new Attribute("airport",  com.demo.app.Region.class), new Equal());
        simConfig.addMapping(new Attribute("currency", Region.class), new Equal());

        //end of new config




//i think this is the culprit
        //  config.setDescriptionSimFunction((GlobalSimilarityFunction) new Average());
        // modified
        // config.setDescriptionSimFunction(new Average());
        // Execute NN
        Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(_caseBase.getCases(), cbrQuery, simConfig);
        evaltogather = eval;
        // Select k cases

        // Collection<CBRCase> selectedcases = SelectCases.selectTopK(eval, k);
//        casestoreturn = selectedcases;
//printing
//        System.out.println("PRINTING");
//        System.out.println(" ######################### ");
//        Collection<RetrievalResult> houma = SelectCases.selectTopKRR(eval, 10);
//        // Print the retrieval
//        System.out.println("Retrieved cases:");
//        for (RetrievalResult nse : houma)
//            System.out.println(nse);

//        System.out.println("Combined + " + k + " cases");
//        for (jcolibri.cbrcore.CBRCase c : selectedcases)
//            System.out.println(c);
//
//        showCases(eval, selectedcases);

    }

    //ill be making a function that retrieves the best cases,
// then it calls the db to make a [casesolutionDTO]
//    it'll be transfered to the client and in routing i'll change the type of what to send
    public void getMeMyCases(int k ) {
        Collection<CBRCase> selectedcases = SelectCases.selectTopK(evaltogather, k);
        casestoreturn = selectedcases;
        //to print the cases after i shortened them to what i want
        System.out.println("Combined + " + k + " cases gathered");
        for (CBRCase c : selectedcases)
            System.out.println(c);


    }

    public void postCycle() {
        this._caseBase.close();
    }


    //ADDITIONAL FUNCTIONS...


    //This function is to adapt new casess to the database

    public void caseAdaption(TravelDescription mytraumadescription, TravelSolution mytraumasolution) throws ExecutionException, ExecutionException {
        configure();
        preCycle();
        CBRCase mycasetolearn = new CBRCase();
        System.out.println("my traumadescription " + mytraumadescription);

        System.out.println("my traumasolution " + mytraumasolution);
        mycasetolearn.setDescription(mytraumadescription);
        mycasetolearn.setSolution(mytraumasolution);

        //I'll teach the casebase with the new case

        System.out.println("my casebase: " + mycases);
        System.out.println("casebase " + _caseBase);
        //test6
        ArrayList<CBRCase> casestoLearnt = new ArrayList<CBRCase>();
        casestoLearnt.add(mycasetolearn);

        HashMap<Attribute, Object> componentsKeys = new HashMap<Attribute, Object>();
        componentsKeys.put(new Attribute("Id", TravelDescription.class), "theid");
        componentsKeys.put(new Attribute("Id", TravelSolution.class), "theid");
        //componentsKeys.put(new Attribute("id",Region.class), 7);
        DefineNewIdsMethod.defineNewIdsMethod(mycasetolearn, componentsKeys);

        System.out.println("Case with new Id");
        System.out.println(mycasetolearn);
        //  _caseBase.learnCases(casestoLearnt);
        StoreCasesMethod.storeCase(_caseBase, mycasetolearn);
        System.out.println("learnt ! ");

        //THESE WOULD HAVE BEEN USED IF WE DIDN'T SUFFER WITH THE DATABASE :'(
        // jcolibri.method.retain.StoreCasesMethod.storeCases((CBRCaseBase) mycases,collectiontolearn );
        //_caseBase.learnCases(collectiontolearn);


    }



    public CBRQuery getQuery(TravelDescription request) {
        CBRQuery query = new CBRQuery();
        query.setDescription(request);

        return query;
    }




//Needs to be modified to show all the cases
    public void showCases(Collection<RetrievalResult> eval, Collection<CBRCase> selected) {
       //15/04/2022 ignored this next line
       // MaladieInsertRepo maladierepo = new MaladieInsertRepo();

        cases = new ArrayList<RetrievalResult>();
        for (RetrievalResult rr : eval) {
            if (selected.contains(rr.get_case())) {
                cases.add(rr);
            }
        }
        System.out.println("casessss");
        System.out.println(cases);
        System.out.println("I'm gonna print my cases from each one i get the description and the solution");
        for (int i = 0; i < cases.size(); i++) {
            RetrievalResult rr_case = cases.get(i);
            CBRCase mycase = rr_case.get_case();
            //for each case i get the description
            TravelDescription desc = (TravelDescription) mycase.getDescription();
            System.out.println("Case description");
            System.out.println(desc);
            //casestoreturn.add(maladierepo.makeCase(desc));
            //i get the solution
            TravelSolution sol = (TravelSolution) mycase.getSolution();
            System.out.println("Case solution");
            System.out.println(sol);
        }
    }


}

//
// //similarityDialog.setVisible(true);
//        NNConfig config = new NNConfig();
//
//        config.setDescriptionSimFunction(new Average());
//
//
//        Attribute attribute;
//
//        //SimilConfigPanel similConfig;
//        LocalSimilarityFunction function;
//
//        //similConfig = nom;
//        attribute = new Attribute("Temperature", TravelDescription.class);
//        config.addMapping(attribute, (es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction) new Euclidienne());
//        //config.setWeight(attribute,0.1);
//
//        attribute = new Attribute("Age", TravelDescription.class);
//        config.addMapping(attribute, (es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction) new Euclidienne());
//        //	config.setWeight(attribute,0.1);
//
//        attribute = new Attribute("Poids", TravelDescription.class);
//        config.addMapping(attribute, (es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction) new Euclidienne());
//        //	config.setWeight(attribute,0.1);
//        attribute = new Attribute("glasgow", TravelDescription.class);
//        config.addMapping(attribute, new Equal());
//        ///had weight
////        config.setWeight(attribute, 0.1);
//
//
//        attribute = new Attribute("tonus", TravelDescription.class);
//        config.addMapping(attribute, new Equal());
//        //config.setWeight(attribute,0.1);
//
//        attribute = new Attribute("conscient", TravelDescription.class);
//        config.addMapping(attribute, new Equal());
//
//
//        attribute = new Attribute("convulsion", TravelDescription.class);
//        config.addMapping(attribute, new Equal());
//        //	config.setWeight(attribute,0.1);
//
//        attribute = new Attribute("vomissement", TravelDescription.class);
//        config.addMapping(attribute, new Equal());
//        //	config.setWeight(attribute,0.1);
//        attribute = new Attribute("motrice", TravelDescription.class);
//        config.addMapping(attribute, new Equal());
//        //	config.setWeight(attribute,0.1);*/
//        attribute = new Attribute("mouvement", TravelDescription.class);
//        config.addMapping(attribute, new Equal());
//        //	config.setWeight(attribute,0.1);
//        attribute = new Attribute("reflexe", TravelDescription.class);
//        config.addMapping(attribute, new Equal());
//        //	config.setWeight(attribute,0.1);
//
//
//        attribute = new Attribute("fracture", TravelDescription.class);
//        config.addMapping(attribute, new Equal());
//        //config.setWeight(attribute,0.1);*/
//
//        attribute = new Attribute("Pas", TravelDescription.class);
//        config.addMapping(attribute, new Equal());
//        //	config.setWeight(attribute,0.1);
//        attribute = new Attribute("tempextr", TravelDescription.class);
//        config.addMapping(attribute, new Equal());
//        ///had weight
////        config.setWeight(attribute, 0.1);
//
//
//        attribute = new Attribute("pad", TravelDescription.class);
//        config.addMapping(attribute, new Equal());
//        //	config.setWeight(attribute,0.1);*/
//
//        attribute = new Attribute("trc", TravelDescription.class);
//        config.addMapping(attribute, new Equal());
//        //	config.setWeight(attribute,0.1);
//
//        attribute = new Attribute("marbure", TravelDescription.class);
//        config.addMapping(attribute, new Equal());
//
//        ///had weight
//        // config.setWeight(attribute, 0.1);
//
//        attribute = new Attribute("cyanose", TravelDescription.class);
//        config.addMapping(attribute, new Equal());
//        config.setWeight(attribute, 0.1);
//
//        attribute = new Attribute("rales", TravelDescription.class);
//        config.addMapping(attribute, new Equal());
//        //config.setWeight(attribute,0.1);*/
//
//        attribute = new Attribute("diurese", TravelDescription.class);
//        config.addMapping(attribute, new Equal());
//        //	config.setWeight(attribute,0.1);*/
//
//        attribute = new Attribute("spo2", TravelDescription.class);
//        config.addMapping(attribute, new Equal());
//        ///had weight
//        // config.setWeight(attribute, 0.1);
//
//        attribute = new Attribute("hemoragie", TravelDescription.class);
//        config.addMapping(attribute, new Equal());
//        ///had weight
//        // config.setWeight(attribute, 0.1);
//
//        attribute = new Attribute("sexe", TravelDescription.class);
//        config.addMapping(attribute, new Equal());
//
//
//        attribute = new Attribute("EncBranchique", TravelDescription.class);
//        config.addMapping(attribute, new Equal());
//        //config.setWeight(attribute,0.1);
//
//        attribute = new Attribute("Oedeme", TravelDescription.class);
//        config.addMapping(attribute, new Equal());
//        ///had weight
//        // config.setWeight(attribute, 0.1);
//
//        attribute = new Attribute("corpsE", TravelDescription.class);
//        config.addMapping(attribute, new Equal());
//        ///had weight
//        // config.setWeight(attribute, 0.1);
//
//
//        attribute = new Attribute("Fc", TravelDescription.class);
//        config.addMapping(attribute, new Equal());
//        ///had weight
//        // config.setWeight(attribute, 0.1);
//
//        attribute = new Attribute("fr", TravelDescription.class);
//        config.addMapping(attribute, new Equal());
//        ///had weight
//        // config.setWeight(attribute, 0.1);
//

