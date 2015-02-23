package org.jeep.stock;

//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.Locale;

public class Test {

    /** *//**
    * @param args
    *    */
        public static void main(String[] args) {
            testStock(args);
            //        testDate();
        }

    private static void testStock(String[] args){
        CodesView view = new CodesView();
        String[] codes ;
        codes = view.getAllCodes();
        System.out.println("Test.testStock: get all codes:");
        for(int i = 0; i < codes.length; i++)
            System.out.println("\t["+i+"]:" + codes[i]);

        Stock stock;
        if(args != null && args.length != 0){
            stock = new Stock(args[0]);
        }else{
            stock = new Stock(codes);
        }
    }

}
