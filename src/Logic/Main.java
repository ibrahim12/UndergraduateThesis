package Logic;

import IR.Config;
import IR.Constants;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import utility.Printer;
import utility.Reader;
import utility.Stats;
import utility.Util;
import utility.Writer;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author ibrahim
 */
public class Main {

    public Config config = null;
    public Datasets allDatasets = null;
    public ArrayList<Population> populations = new ArrayList<Population>();
    public static String datasetName = "";
    public static Stats stats;

    public Main() throws IOException {
        LoadConfig();
        stats = new Stats(config);
        allDatasets = new Datasets(config);
    }

    public void Init() throws Exception {

        Constants.no_of_fold = config.getInt("no_of_fold");
        Constants.exception_path = config.get("exception_path");
        Constants.algo_iteration_count_max = config.getInt("algo_iteration_count_max");

        Constants.total_class = config.getInt("total_class");

//        Constants.dataset_class_index = config.getInt("dataset_class_index");
//        Constants.no_of_attr = config.getInt("no_of_attr");

        Constants.attr_dont_care_value = config.getInt("attr_dont_care_value");

        Constants.chromosome_size_percentage = config.getDouble("chromosome_size_percentage");
        Constants.no_of_chromosome = config.getInt("no_of_chromosome");
        Constants.elite_chromosome_count = config.getInt("elite_chromosome_count");

        Constants.fitness_alpha = config.getDouble("fitness_alpha");
        Constants.fitness_beta = config.getDouble("fitness_beta");
        Constants.fitness_type = config.getInt("fitness_type");
        Constants.adaptive_mutation_enabled = config.getInt("adaptive_mutation_enabled");

        Constants.generality_threshold = config.getDouble("generality_threshold");

        Constants.mutation_phase_1_percentage = config.getDouble("mutation_phase_1_percentage");

        Constants.all_attr_domain_max = Integer.MIN_VALUE;
        Constants.attr_domain_max = new int[Datasets.dataset.get(0).size()];
        Constants.attr_domain_min = new int[Datasets.dataset.get(0).size()];


        Constants.no_of_attr = Datasets.dataset.get(0).size() - 1;
        Constants.dataset_class_index = Constants.no_of_attr;

        for (int j = 0; j < Constants.no_of_attr; j++) {
            Constants.attr_domain_min[j] = Integer.MAX_VALUE;
            Constants.attr_domain_max[j] = Integer.MIN_VALUE;
        }

        for (int i = 0; i < Datasets.dataset.size(); i++) {
            int len = Datasets.dataset.get(i).size();
            for (int j = 0; j < len; j++) {
                int value = Datasets.dataset.get(i).get(j);
                if (Constants.attr_domain_max[j] < value) {
                    Constants.attr_domain_max[j] = value;
                }
                if (Constants.attr_domain_min[j] > value) {
                    Constants.attr_domain_min[j] = value;
                }
                if (Constants.all_attr_domain_max < value) {
                    Constants.all_attr_domain_max = value;
                }
            }
        }




        if (Constants.attr_domain_max[Constants.dataset_class_index] != 1) {
            Printer.print(Constants.attr_domain_max[Constants.dataset_class_index]);
            throw new Exception("No of class is not two");
        }
        
        String[] names = config.get("dataset_path").split("/");
        this.datasetName = names[2];
        Printer.print(datasetName);
        Constants.datasetName = datasetName;

    }

    public void InitPopulation() {

        populations.clear();

        for (int classIterationIndex = 0; classIterationIndex < Constants.total_class; classIterationIndex++) {

            populations.add(new Population(classIterationIndex));
            populations.get(classIterationIndex).setParameters(config);
            int aChoromosomeSize = (int) (allDatasets.classSperatedDatsetIndex.get(classIterationIndex).size() * Constants.chromosome_size_percentage);

            for (int chromosomeIndex = 0; chromosomeIndex < Constants.no_of_chromosome;
                    chromosomeIndex++) {
                int[] indexes = Util.GetNRandomInt(aChoromosomeSize, 0, allDatasets.classSperatedDatsetIndex.get(classIterationIndex).size());
                ArrayList<Gene> chromosomeGenes = new ArrayList<Gene>();
                for (int i = 0; i < indexes.length; i++) {
//                    chromosomeGenes.add(
//                                        new Gene(
//                                                new ArrayList(
//                                                    allDatasets.dataset.get(
//                                                                    allDatasets.classSperatedDatsetIndex
//                                                                        .get(classIterationIndex).get(indexes[i])
//                                                                        ).subList(0, Constants.dataset_class_index)
//                                                               )
//                                                )
//                                        );
                    chromosomeGenes.add(Datasets.datasetGenes.get(allDatasets.classSperatedDatsetIndex.get(classIterationIndex).get(indexes[i])));
                }

                populations.get(classIterationIndex).addChromosome(new Chromosome(chromosomeGenes, populations.get(classIterationIndex)));
            }

        }

    }

    public void PrintPopulationDataset() {
        Printer.print("Populations:: size :" + populations.size());
        for (int classIndex = 0; classIndex < populations.size(); classIndex++) {
            Printer.print("Class " + classIndex + " :: " + populations.get(classIndex).size());

            for (int choromosomeIndex = 0;
                    choromosomeIndex < populations.get(classIndex).size(); choromosomeIndex++) {
                Printer.print("Chromosome " + choromosomeIndex + " :: " + populations.get(classIndex).chromosomes.get(choromosomeIndex).size());
                Printer.print(populations.get(classIndex).chromosomes.get(choromosomeIndex).genes);
            }
        }
    }

    public void LoadConfig() throws IOException {
        config = new Config("data//config.ini");
        config.ReadIni();
    }

    public void dumpUniqueDataset() {
        ArrayList< ArrayList<Integer>> newDataset = new ArrayList< ArrayList<Integer>>();
        for (int i = 0; i < allDatasets.classSperatedDatsetIndex.get(0).size(); i++) {
            newDataset.add(Datasets.dataset.get(allDatasets.classSperatedDatsetIndex.get(0).get(i)));
        }

        Writer.WriteFromArrayList("data//datasetUnique", Datasets.dataset);
    }

    public Chromosome[] findBestChromosome() throws Exception {
        Chromosome[] allClassBestChromosme = new Chromosome[populations.size()];
        for (int i = 0; i < populations.size(); i++) {
            Evalutor eval = new Evalutor(populations.get(i), allDatasets, config);
            allClassBestChromosme[i] = eval.doEvalute();
//            eval.PrintPopulationDataset();
        }
        return allClassBestChromosme;

    }

    public Property GetResult() throws Exception {

        allDatasets.ReadDataSet();
        Init();
        allDatasets.InitDatasetGenes();
//        allDatasets.InitDataSetHashMap();
//        allDatasets.MakeDataSetUnique();

        if (config.getInt("mutation_phase_2_dataset") == 0) {
            allDatasets.InitTrainTestSet();

            allDatasets.InitTrainInfoGains();
            allDatasets.InitTrainAllAttrAllValRI();
        } else {
            allDatasets.InitTrainValidationTestSet();

            if (config.getInt("mutation_phase_2_dataset") == 2) {
                allDatasets.InitValidationInfoGains();
            } else {
                allDatasets.InitTrainPlusValidationInfoGains();
            }

            allDatasets.InitTrainPlusValidationAllAttrAllValRI();
        }


//        allDatasets.DumpTrainSet();
//        allDatasets.DumpTestSet();
//        allDatasets.PrintTrainSetIndex();
//        allDatasets.PrintTestSetIndex();
//        allDatasets.PrintValidationSetIndex();


        allDatasets.InitClassSepratedDataSet();

        if (allDatasets.classSperatedDatsetIndex.get(0).size() > allDatasets.classSperatedDatsetIndex.get(1).size()) {
            Constants.majority_class = 0;
        } else {
            Constants.majority_class = 1;
        }


        InitPopulation();


//        Printer.print(config.toString());

        Chromosome[] allClassBestCromosome = findBestChromosome();


//        Printer.print("");
        for (int i = 0; i < allClassBestCromosome.length; i++) {
//            Printer.print("Class "+ i + " "+ allClassBestCromosome[i].properties.toString());            
//            Printer.print("Class "+ i + " Fitness " + allClassBestCromosome[i].properties.getFitness() + " Accuracy " + allClassBestCromosome[i].properties.getAccuracy());            
//            allClassBestCromosome[i].removeDuplicateGenes();
//            Printer.print("Generality " +  allClassBestCromosome[i].getGenerality() );
        }
        int[][] result = Tester.Test(allClassBestCromosome, allDatasets.testSetIndex);
        
//        Printer.print(result);

        Property property = new Property(Constants.fitness_alpha, Constants.fitness_beta);
        property.updateAll(result);
        return property;
//        Printer.print(" Fitness " + property.getFitness() + " Accuracy " + property.getAccuracy() );
//        Printer.print(property.toString());

    }

    public void NFoldCrossValidation() throws Exception {

        allDatasets.InitDatasetGenes();
        Datasets[] nFoldDataset = allDatasets.NFoldTrainTest();

//        for(int i=0;i<nFoldDataset.length;i++){
//            Printer.print("Fold Index : " + i);
//            nFoldDataset[i].PrintTrainSetIndex();
//            nFoldDataset[i].PrintTestSetIndex();            
//        }

        Property[] property = new Property[Constants.no_of_fold];
        Property avgProperty = new Property(Constants.fitness_alpha, Constants.fitness_beta);

        for (int i = 0; i < Constants.no_of_fold; i++) {

            //Printer.print("Fold " + i + " started");

            allDatasets = nFoldDataset[i];

            if (config.getInt("mutation_phase_2_dataset") == 0) {

                allDatasets.InitTrainInfoGains();
                allDatasets.InitTrainAllAttrAllValRI();
            } else {

                allDatasets.InitValidationSetIndexFromTrainSet();

                if (config.getInt("mutation_phase_2_dataset") == 2) {
                    allDatasets.InitValidationInfoGains();
                } else {
                    allDatasets.InitTrainPlusValidationInfoGains();
                }

                allDatasets.InitTrainPlusValidationAllAttrAllValRI();
            }

            allDatasets.InitClassSepratedDataSet();
            InitPopulation();

            Chromosome[] allClassBestCromosome = findBestChromosome();

//            for (int i = 0; i < allClassBestCromosome.length; i++) {
//    //            allClassBestCromosome[i].removeDuplicateGenes();
//            }
            int[][] result = Tester.Test(allClassBestCromosome, allDatasets.testSetIndex);

            property[i] = new Property(Constants.fitness_alpha, Constants.fitness_beta);
            property[i].updateAll(result);

            avgProperty.setAccuracy(avgProperty.getAccuracy() + property[i].getAccuracy());
            avgProperty.setRecall(avgProperty.getRecall() + property[i].getRecall());
            avgProperty.setPrecison(avgProperty.getPrecison() + property[i].getPrecison());
            avgProperty.setfMeasure(avgProperty.getfMeasure() + property[i].getfMeasure());
            avgProperty.setgMean(avgProperty.getgMean() + property[i].getgMean());
            avgProperty.setFpRate(avgProperty.getFpRate() + property[i].getFpRate());
            avgProperty.setTpRate(avgProperty.getTpRate() + property[i].getTpRate());
            avgProperty.setGenerality(avgProperty.getGenerality() + property[i].getGenerality());
            avgProperty.setSensitivity(avgProperty.getSensitivity() + property[i].getSensitivity());
            avgProperty.setSpecificity(avgProperty.getSpecificity() + property[i].getSpecificity());

        }

        avgProperty.setAccuracy(avgProperty.getAccuracy() / Constants.no_of_fold);
        avgProperty.setRecall(avgProperty.getRecall() / Constants.no_of_fold);
        avgProperty.setPrecison(avgProperty.getPrecison() / Constants.no_of_fold);
        avgProperty.setfMeasure(avgProperty.getfMeasure() / Constants.no_of_fold);
        avgProperty.setgMean(avgProperty.getgMean() / Constants.no_of_fold);
        avgProperty.setFpRate(avgProperty.getFpRate() / Constants.no_of_fold);
        avgProperty.setTpRate(avgProperty.getTpRate() / Constants.no_of_fold);
        avgProperty.setGenerality(avgProperty.getGenerality() / Constants.no_of_fold);
        avgProperty.setSensitivity(avgProperty.getSensitivity() / Constants.no_of_fold);
        avgProperty.setSpecificity(avgProperty.getSpecificity() / Constants.no_of_fold);

        Printer.print(avgProperty.toString());

    }

    public void OnePass() throws Exception {

        allDatasets.ReadDataSet();
        Init();
        int avgCount = config.getInt("avg_count");
        int friendship_dataset = config.getInt("friendship_dataset");
        Property[] property = new Property[avgCount];
        Property avgProperty = new Property(Constants.fitness_alpha, Constants.fitness_beta);


        for (int i = 0; i < avgCount; i++) {
            Main algo = new Main();
            
            if(friendship_dataset == 1)
                property[i] = algo.GetResultWithSepcifiedTestTrain();
            else
                property[i] = algo.GetResult();
            
            avgProperty.setAccuracy(avgProperty.getAccuracy() + property[i].getAccuracy());
            avgProperty.setRecall(avgProperty.getRecall() + property[i].getRecall());
            avgProperty.setPrecison(avgProperty.getPrecison() + property[i].getPrecison());
            avgProperty.setfMeasure(avgProperty.getfMeasure() + property[i].getfMeasure());
            avgProperty.setgMean(avgProperty.getgMean() + property[i].getgMean());
            avgProperty.setFpRate(avgProperty.getFpRate() + property[i].getFpRate());
            avgProperty.setTpRate(avgProperty.getTpRate() + property[i].getTpRate());
            avgProperty.setGenerality(avgProperty.getGenerality() + property[i].getGenerality());
            avgProperty.setSensitivity(avgProperty.getSensitivity() + property[i].getSensitivity());
            avgProperty.setSpecificity(avgProperty.getSpecificity() + property[i].getSpecificity());

//            Printer.print(property[i].toString());
            Printer.print(property[i].getAccuracy());
//            Printer.print(property[i].confustionMatrix);
        }

        avgProperty.setAccuracy(avgProperty.getAccuracy() / avgCount);
        avgProperty.setRecall(avgProperty.getRecall() / avgCount);
        avgProperty.setPrecison(avgProperty.getPrecison() / avgCount);
        avgProperty.setfMeasure(avgProperty.getfMeasure() / avgCount);
        avgProperty.setgMean(avgProperty.getgMean() / avgCount);
        avgProperty.setFpRate(avgProperty.getFpRate() / avgCount);
        avgProperty.setTpRate(avgProperty.getTpRate() / avgCount);
        avgProperty.setGenerality(avgProperty.getGenerality() / avgCount);
        avgProperty.setSensitivity(avgProperty.getSensitivity() / avgCount);
        avgProperty.setSpecificity(avgProperty.getSpecificity() / avgCount);

        Printer.print(avgProperty.toString());
    }

    public void MakeDataSetFromTrainTestSet() {

        String trainSetPath = config.get("trainset_path");
        String testSetPath = config.get("testset_path");
        String dataSetPath = config.get("dataset_path");

        ArrayList< ArrayList<Integer>> trainSet = Reader.ReadInDataSet(trainSetPath);
        ArrayList< ArrayList<Integer>> testSet = Reader.ReadInDataSet(testSetPath);

        for (int i = 0; i < trainSet.size(); i++) {
            Datasets.dataset.add(trainSet.get(i));
            allDatasets.trainSetIndex.add(i);
        }

        int testSetIndexStart = trainSet.size();
        for (int i = 0; i < testSet.size(); i++) {
            Datasets.dataset.add(testSet.get(i));
            allDatasets.testSetIndex.add(testSetIndexStart + i);
        }

    }

    public Property GetResultWithSepcifiedTestTrain() throws Exception {

        MakeDataSetFromTrainTestSet();
        Init();
        allDatasets.InitDatasetGenes();

        if (config.getInt("mutation_phase_2_dataset") == 0) {

            allDatasets.InitTrainInfoGains();
            allDatasets.InitTrainAllAttrAllValRI();
        } else {
            allDatasets.InitValidationSetIndexFromTrainSet();

            if (config.getInt("mutation_phase_2_dataset") == 2) {
                allDatasets.InitValidationInfoGains();
            } else {
                allDatasets.InitTrainPlusValidationInfoGains();
            }

            allDatasets.InitTrainPlusValidationAllAttrAllValRI();
        }


//        allDatasets.DumpTrainSet();
//        allDatasets.DumpTestSet();
//        allDatasets.PrintTrainSetIndex();
//        allDatasets.PrintTestSetIndex();
//        allDatasets.PrintValidationSetIndex();

        allDatasets.InitClassSepratedDataSet();
        if (allDatasets.classSperatedDatsetIndex.get(0).size() > allDatasets.classSperatedDatsetIndex.get(1).size()) {
            Constants.majority_class = 0;
        } else {
            Constants.majority_class = 1;
        }

        InitPopulation();


//        Printer.print(config.toString());

        Chromosome[] allClassBestCromosome = findBestChromosome();


//        Printer.print("");
        ArrayList<Integer> ruleTrainAcc = new ArrayList<Integer>();
        ArrayList<Integer> ruleTestAcc = new ArrayList<Integer>();
        for (int i = 0; i < allClassBestCromosome.length; i++) {
//            Printer.print("Class "+ i + " "+ allClassBestCromosome[i].properties.toString());            
//            Printer.print("Class "+ i + " Fitness " + allClassBestCromosome[i].properties.getFitness() + " Accuracy " + allClassBestCromosome[i].properties.getAccuracy());            
            
        	allClassBestCromosome[i].removeDuplicateGenes();        	
        	for(int j=0;j < allClassBestCromosome[i].genes.size() ;j++){
        		int testAccuracy = Tester.getGeneAccuracy( allClassBestCromosome[i].genes.get(j), allDatasets.testSetIndex) ;
        		int trainAccuracy = Tester.getGeneAccuracy ( allClassBestCromosome[i].genes.get(j), allDatasets.trainSetIndex) ; 
        		Printer.print( allClassBestCromosome[i].genes.get(j).toString() + " " + trainAccuracy + " " + testAccuracy );
        	}
        	Printer.print("#__________________#");
        }
        int[][] result = Tester.Test(allClassBestCromosome, allDatasets.testSetIndex);
//        Printer.print(result);

        Property property = new Property(Constants.fitness_alpha, Constants.fitness_beta);
        property.updateAll(result);
        return property;
//        Printer.print(" Fitness " + property.getFitness() + " Accuracy " + property.getAccuracy() );
//        Printer.print(property.toString());


    }

    public static void main(String[] args) throws Exception {
                
        
        Printer.print("accuracy\t precison\t recall\t gMean\t fMeasure");
        Main algo = new Main();
        
        int algoType = algo.config.getInt("mutation_phase_2_process");
        int mutation_phase2_dataset = algo.config.getInt("mutation_phase_2_dataset");
        String dataset = algo.config.get("dataset_path");
        String algoName = "EA";
        if( mutation_phase2_dataset == 0 && algoType == 0){
            algoName += "1b";
        }else if( mutation_phase2_dataset == 0 && algoType == 1){
            algoName += "2b";
        }else if( mutation_phase2_dataset == 1 && algoType == 0){
            algoName += "1a";
        }else{
            algoName += "2a";
        }
        Printer.print(dataset +" "+ algoName);
        
        
        algo.allDatasets.ReadDataSet();
        algo.Init();
//        algo.allDatasets.InitDatasetGenes();
        algo.OnePass();
     
//        algo.NFoldCrossValidation();

//
//        algo.allDatasets.PrintDataSet();
//        algo.allDatasets.PrintDataSetHashMap();        
//        algo.allDatasets.PrintUniqueDatasetHashCount();
////        algo.PrintDataSetReverseMap();
//        algo.allDatasets.PrintTrainSetIndex();
//        algo.allDatasets.PrintTestSetIndex();
//        algo.allDatasets.PrintValidationSetIndex();
//        algo.allDatasets.PrintClassSepratedDataSetIndex();
//        algo.PrintPopulationDataset();



    }
}
