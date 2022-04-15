package com.demo.second;

import es.ucm.fdi.gaia.jcolibri.exception.ExecutionException;
//learning
//	public void learnCases(Collection<CBRCase> cases) {
//	workingCases.addAll(cases);
public class Main {
    public static void main(String[] args) {
        System.out.println("This is the main function is package com.demo.second");
        System.out.println("######################");

        Test1 test1 = new Test1();
        try {
            System.out.println("Running configure()");
            test1.configure();

            System.out.println("Running preCycle()");
            test1.preCycle();

            //The cycle will require a query description, so we'll make it before calling the cycle
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
}
