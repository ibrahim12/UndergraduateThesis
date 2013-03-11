///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package Testing;
//
//import Logic.Evalutor;
//import Logic.Main;
//import utility.Printer;
//
///**
// *
// * @author ibrahim
// */
//public class EvalutorClass {
//
//    public static void TestCalculatePopulationProperties() throws Exception {
//
//
//        Main pain = new Main();
//        pain.allDatasets.ReadDataSet();
////        pain.InitDataSetHashMap();        
//        pain.allDatasets.MakeDataSetUnique();
//        pain.allDatasets.InitTrainTestSet();
////        pain.allDatasets.InitTrainValidationTestSet();
//        pain.allDatasets.InitClassSepratedDataSet();
//        pain.InitPopulation();
////
////
//        pain.allDatasets.PrintDataSet();
//
//        for (int i = 0; i < pain.populations.size(); i++) {
//            Printer.print("Population :: " + i );
//            Evalutor ev = new Evalutor(pain.populations.get(i), pain.allDatasets);
//            ev.doEvalute();
//            
//        }
//
//    }
//    public static void main(String[] args) throws Exception{
//        TestCalculatePopulationProperties();
//    }
//}
