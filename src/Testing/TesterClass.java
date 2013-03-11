///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package Testing;
//
//import IR.Constants;
//import Logic.Chromosome;
//import Logic.Gene;
//import java.util.ArrayList;
//import org.ini4j.Profile.Section;
//import utility.Printer;
//import utility.Reader;
//import utility.Util;
//
///**
// *
// * @author ibrahim
// */
//public class TesterClass {
//
//    public static void FunctionIsEqual() throws Exception {
//        
//        ArrayList<Integer> aGeneList = new ArrayList<Integer>();
//        aGeneList.add(1);
//        aGeneList.add(2);
//        aGeneList.add(0);
//        aGeneList.add(4);
//        
//        Gene aGene = new Gene(aGeneList);
//
//
//        ArrayList<Integer> bGeneList = new ArrayList<Integer>();
//        bGeneList.add(1);
//        bGeneList.add(2);
//        bGeneList.add(3);
//        bGeneList.add(3);
//
//        Gene bGene = new Gene(bGeneList);
//
//        if (aGene.isEqual(bGene)) {
//            Printer.print("Equal");
//        } else {
//            Printer.print("Not Equal");
//        }
//    }
//    
//    public static void FunctionTest() throws Exception{
//        
//        ArrayList< ArrayList <Integer> > dataset = new ArrayList< ArrayList<Integer>>();
//                
//        for(int i=0;i< 10; i++){
//            dataset.add(new ArrayList<Integer>());
//            for(int j=0;j < Constants.NO_OF_ATTR;j++){
//                int randVal = Util.RandomInt(Constants.ATTR_DOMAIN_MIN[j], Constants.ATTR_DOMAIN_MAX[j]);                                        
//                dataset.get(i).add(randVal);                
//            }
//            int randClass = Util.RandomInt(0,1);        
//            dataset.get(i).add(randClass);
//            
//        }        
//        ArrayList<Gene> genes = new ArrayList<Gene>();
//        for(int k=0;k<5;k++){
//            int randIndex = Util.RandomInt(dataset.size());
//            genes.add(new Gene(new ArrayList<Integer>(dataset.get(randIndex).subList(0, Constants.CLASS_INDEX))));
//        }
//        
//        Chromosome chromosome = new Chromosome(genes);
//        
//        Printer.print("Dataset " + dataset.size());
//        for(int i=0;i<dataset.size();i++)
//            Printer.print(dataset.get(i));
//        Printer.print("Chromosome " + chromosome.size());
//        Printer.print(chromosome.toString());
//        
////        Printer.print(Tester.Test(chromosome,dataset));        
//        
//    }
//
//    public static void main(String[] args) throws Exception {
//        
////        TesterClass.FunctionTest();
////        TesterClass.FunctionIsEqual();
//        
//        ArrayList<Section> allConfig = Reader.ReadIniFile("data//test.ini");
//        Printer.print(allConfig.get(0).get("age"));
//        
//    }
//}
