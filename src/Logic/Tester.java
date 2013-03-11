package Logic;


import IR.Constants;
import IR.MyPair;
import java.util.ArrayList;
import java.util.Arrays;
import utility.Util;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author ibrahim
 */
public class Tester {

    
    
    public static int[][] Test(Chromosome aChoromosome, ArrayList<Integer>  datasetToTest , int populationClassIndex) throws Exception {
//        Printer.print(aChoromosome.toString());
        int[][] confustionMatrix = new int[Constants.total_class][Constants.total_class];

        for (int dataSetIndex = 0; dataSetIndex < datasetToTest.size(); dataSetIndex++) {

//            Gene bGene = new Gene(new ArrayList(Datasets.dataset.get(datasetToTest.get(dataSetIndex)).subList(0, Constants.dataset_class_index)));
            Gene bGene = Datasets.datasetGenes.get(datasetToTest.get(dataSetIndex));
            
            boolean isClassified = false;
            for (int geneIndex = 0; geneIndex < aChoromosome.size(); geneIndex++) {

                Gene aGene = aChoromosome.genes.get(geneIndex);
                if(bGene.isEqual(aGene)){                         
//                    Printer.print( aGene.toString() );
//                    Printer.print( bGene.toString() );                    
                    isClassified = true;
                    break;
                }
            }
            int actualClass = Datasets.dataset.get( datasetToTest.get(dataSetIndex) ).get(Constants.dataset_class_index);                        
//            if(isClassified){
//                confustionMatrix[actualClass][populationClassIndex]++;
//            }else{
//                confustionMatrix[actualClass][1 - populationClassIndex]++;
//            }
            if( actualClass == populationClassIndex){
                if(isClassified)
                    confustionMatrix[1][1]++;
                else{
                    confustionMatrix[1][0]++;
                }
            }else{
                if(isClassified)
                    confustionMatrix[0][1]++;
                else
                    confustionMatrix[0][0]++;
            }
            
        }

        return confustionMatrix;
    }
   
    
    public static int[][] Test(Chromosome[] chromosomes,ArrayList<Integer>  datasetToTest) throws Exception {

        int[][] confustionMatrix = new int[Constants.total_class][Constants.total_class];

        for (int dataSetIndex = 0; dataSetIndex < datasetToTest.size(); dataSetIndex++) {

//            Gene bGene = new Gene(new ArrayList(Datasets.dataset.get(datasetToTest.get(dataSetIndex)).subList(0, Constants.dataset_class_index)));
            Gene bGene = Datasets.datasetGenes.get(datasetToTest.get(dataSetIndex));
                        
            MyPair[] percentageMatch = new MyPair[2];
            for (int i = 0; i < chromosomes.length; i++) {
                percentageMatch[i] = new MyPair();                 
                percentageMatch[i].index = i;
//                percentageMatch[i].value = chromosomes[i].maxAlleleMatch(bGene);
                double sumMatch = chromosomes[i].sumAlleleMatch(bGene);
                double validAlleleCount = chromosomes[i].getValidAlleleCount();
                percentageMatch[i].value = sumMatch/validAlleleCount;
            }
            
            Arrays.sort(percentageMatch);            
//            Printer.print(percentageMatch);
            int i=0;
            while(i < percentageMatch.length -1 ){
                if(percentageMatch[i].value != percentageMatch[i+1].value){
                    break;
                }
                i++;
            }
            
            int predictedClass = percentageMatch[0].index;		
            if(i > 0 ){
//                Printer.print("Random");
                predictedClass = percentageMatch[Util.RandomInt(0, i)].index;
//                Printer.print("Majority Class Assigned");
//                predictedClass = Constants.majority_class;
            }
//            Printer.print(percentageMatch);
//            Printer.print(predictedClass);
            int actualClass = Datasets.dataset.get( datasetToTest.get(dataSetIndex) ).get(Constants.dataset_class_index);            
            
            confustionMatrix[actualClass][predictedClass]++;                        
        }

        return confustionMatrix;
    
    }
    
    public static int getGeneAccuracy(Gene aGene,ArrayList<Integer>  datasetToTest) throws Exception {
    	
    	int accuracy = 0;

        for (int dataSetIndex = 0; dataSetIndex < datasetToTest.size(); dataSetIndex++) {

            Gene bGene = Datasets.datasetGenes.get(datasetToTest.get(dataSetIndex));
            if(aGene.isEqual(bGene)){
            	accuracy++;
            }

        }
        return accuracy;
    	
    }
}
