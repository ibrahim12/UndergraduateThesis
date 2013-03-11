/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Logic;

import IR.Config;
import IR.Constants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import utility.Printer;
import utility.Reader;
import utility.Util;
import utility.Writer;

/**
 *
 * @author ibrahim
 */
public class Datasets {

    public HashMap<String, Integer> datasetMap = new HashMap<String, Integer>();
    public HashMap<Integer, String> datasetReverseMap = new HashMap<Integer, String>();
    public HashMap<String, Integer> uniqueDataSetHashCount = new HashMap<String, Integer>();
    public HashMap<String, Integer> uniqueDataSetWithOutClassHashCount = new HashMap<String, Integer>();
    public static ArrayList< ArrayList< Integer>> dataset = null;
    public ArrayList< ArrayList<Integer>> classSperatedDatsetIndex = new ArrayList< ArrayList< Integer>>();
    public ArrayList<Integer> trainSetIndex = new ArrayList<Integer>();
    public ArrayList<Integer> testSetIndex = new ArrayList<Integer>();
    public ArrayList<Integer> validationSetIndex = new ArrayList<Integer>();
    public ArrayList<Integer> trainPlusValidationSetIndex = new ArrayList<Integer>();
    //------------------------- InfoGain -------------------------------------    
    public ArrayList<Double> trainSetInfoGainValues = new ArrayList<Double>();
    public ArrayList<Double> trainSetInfoGainProbabilities = new ArrayList<Double>();
    public ArrayList<Double> validationSetInfoGainValues = new ArrayList<Double>();
    public ArrayList<Double> validationSetInfoGainProbabilities = new ArrayList<Double>();
    public ArrayList<Double> trainPlusValidationSetInfoGainValues = new ArrayList<Double>();
    public ArrayList<Double> trainPlusValidationSetInfoGainProbabilities = new ArrayList<Double>();
    //------------------------- Rule Interestingness -------------------------------------    
    public ArrayList<Double> trainSetRIValues = new ArrayList<Double>();
    public ArrayList<Double> trainSetRIProbabilities = new ArrayList<Double>();
    public ArrayList<Double> validationSetRIValues = new ArrayList<Double>();
    public ArrayList<Double> validationSetRIProbabilities = new ArrayList<Double>();
    public ArrayList<Double> trainPlusValidationSetRIValues = new ArrayList<Double>();
    public ArrayList<Double> trainPlusValidationSetRIProbabilities = new ArrayList<Double>();
    //------------------------- Genes -------------------------------------        
    public static ArrayList<Gene> datasetGenes = new ArrayList<Gene>();
    //------------------------- Others -------------------------------------        
    Config config = null;
    public double dataset_train_precentage = -1;
    public double dataset_validation_percentage = -1;
    public int dataset_randomize = -1;
    public String dataset_path = null;
    public String train_dataset_path = null;
    public String test_dataset_path = null;

    public Datasets(Config config) {

        dataset_path = config.get("dataset_path");

        dataset_train_precentage = config.getDouble("dataset_train_precentage");
        dataset_validation_percentage = config.getDouble("dataset_validation_percentage");
        train_dataset_path = config.get("train_dataset_path");
        test_dataset_path = config.get("test_dataset_path");
        dataset_randomize = config.getInt("dataset_randomize");


        this.config = config;
    }

    public void ReadDataSet() {
        Datasets.dataset = Reader.ReadInDataSet(dataset_path);
    }
       

    public void PrintDataSet() {
        Printer.print("Dataset : " + dataset.size());
        Printer.print(dataset);
    }

    public static int[] getClassCount(ArrayList<Integer> datasetToCount) throws Exception {
        if (dataset == null) {
            throw new Exception("Dataset Uninitialized");
        }

        int[] classCount = new int[Constants.total_class];
        for (int i = 0; i < datasetToCount.size(); i++) {
            classCount[dataset.get(datasetToCount.get(i)).get(Constants.dataset_class_index)]++;
        }
        return classCount;
    }
    
    public void InitDatasetGenes() throws Exception{
        
        if(dataset.isEmpty())
            throw new Exception("Dataset is Empty");
        
        datasetGenes.clear();
        for(int i=0;i<dataset.size();i++){            
            datasetGenes.add(new Gene( new ArrayList( dataset.get(i).subList(0, Constants.dataset_class_index) ) )); 
        }
        
    }

    public void MakeDataSetUnique() {

        for (int i = 0; i < dataset.size(); i++) {
            String key = "";
            String withOutClassKey = "";
            for (int j = 0; j < dataset.get(i).size(); j++) {
                key += "|" + dataset.get(i).get(j);
                if (j < dataset.get(i).size() - 1) {
                    withOutClassKey += "|" + dataset.get(i).get(j);
                }
            }
            if (!uniqueDataSetHashCount.containsKey(key)) {
                uniqueDataSetHashCount.put(key, 1);

            } else {
                uniqueDataSetHashCount.put(key, uniqueDataSetHashCount.get(key) + 1);

            }

            if (!uniqueDataSetWithOutClassHashCount.containsKey(withOutClassKey)) {
                uniqueDataSetWithOutClassHashCount.put(withOutClassKey, 1);
            } else {
                uniqueDataSetWithOutClassHashCount.put(withOutClassKey, uniqueDataSetWithOutClassHashCount.get(withOutClassKey) + 1);
            }

        }
        Datasets.dataset.clear();

        int datasetIndex = 0;
        Iterator it = uniqueDataSetWithOutClassHashCount.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            String key = (String) pairs.getKey();

            int classIndex = -1;
            int value = Integer.MIN_VALUE;
            for (int i = 0; i < Constants.total_class; i++) {
                Integer keyValue = uniqueDataSetHashCount.get(key + "|" + i);
                if (keyValue != null && keyValue > value) {
                    value = uniqueDataSetHashCount.get(key + "|" + i);
                    classIndex = i;
                }
            }

            key += "|" + classIndex;
            String[] attribues = key.split("\\|");
            dataset.add(new ArrayList<Integer>());
            for (int i = 0; i < attribues.length; i++) {
                if (attribues[i].compareTo("") != 0) {
                    dataset.get(datasetIndex).add(Integer.parseInt(attribues[i]));
                }
            }
            datasetIndex++;
        }

    }

    public void PrintUniqueDatasetHashCount() {
        Printer.print("Unique Dataset");
        Printer.print(uniqueDataSetHashCount);
        Printer.print("Unique Dataset Without Class");
        Printer.print(uniqueDataSetWithOutClassHashCount);
    }

    public void InitDataSetHashMap() {
        for (int i = 0; i < dataset.size(); i++) {
            String key = "";
            for (int j = 0; j < dataset.get(i).size(); j++) {
                key += "|" + dataset.get(i).get(j);
            }
            datasetMap.put(key, i);
            datasetReverseMap.put(i, key);
        }
    }

    public void PrintDataSetHashMap() {
        Printer.print(datasetMap);
    }

    public void PrintDataSetReverseMap() {
        Printer.print(datasetReverseMap);
    }

    public int[] GetDatasetIndexes() {
        int[] indexes = null;
        if (dataset_randomize == 1) {
            indexes = Util.GetNRandomInt(Datasets.dataset.size(), 0, Datasets.dataset.size());
        } else {
            Printer.print("Randomization is OFF");
            indexes = new int[Datasets.dataset.size()];
            for (int i = 0; i < indexes.length; i++) {
                indexes[i] = i;
            }
        }
        return indexes;
    }

    public Datasets[] NFoldTrainTest() {

        Datasets[] nFoldDatasets = new Datasets[Constants.no_of_fold];


        int[] indexes = GetDatasetIndexes();
        int foldSize = indexes.length / Constants.no_of_fold;

        for (int k = 0; k < Constants.no_of_fold; k++) {

            nFoldDatasets[k] = new Datasets(config);            

            int startIndex = k * foldSize;

            for (int i = 0; i < indexes.length; i++) {

                if (i >= startIndex && i < startIndex + foldSize) {
                    continue;
                }

                nFoldDatasets[k].trainSetIndex.add(indexes[i]);
            }

            for (int i = startIndex; i < startIndex + foldSize; i++) {
                nFoldDatasets[k].testSetIndex.add(indexes[i]);
            }

        }

        return nFoldDatasets;

    }

    public void InitTrainTestSet() {
        int size = (int) (Datasets.dataset.size() * this.dataset_train_precentage);

        int[] indexes = GetDatasetIndexes();


        for (int i = 0; i < size; i++) {
            this.trainSetIndex.add(indexes[i]);
        }

        for (int i = size; i < indexes.length; i++) {
            this.testSetIndex.add(indexes[i]);
        }

    }

    public void InitValidationSetIndexFromTrainSet() throws Exception {

        if (trainSetIndex.isEmpty()) {
            throw new Exception("TrainSet Index Not Set");
        }

        int actualTrainSize = (int) (trainSetIndex.size() * this.dataset_validation_percentage);
        int validationSize = trainSetIndex.size() - actualTrainSize;

        for (int i = 0; i < validationSize; i++) {
            this.validationSetIndex.add(this.trainSetIndex.get(i));
            this.trainSetIndex.remove(i);
        }
        this.trainPlusValidationSetIndex.addAll(trainSetIndex);
        this.trainPlusValidationSetIndex.addAll(validationSetIndex);

    }

    public void InitTrainValidationTestSet() {
        int trainSize = (int) (Datasets.dataset.size() * this.dataset_train_precentage);
        int actualTrainSize = (int) (trainSize * this.dataset_validation_percentage);
//        int testSize = this.dataset.size() - trainSize;
        int validationSize = trainSize - actualTrainSize;

        int[] indexes = GetDatasetIndexes();

        for (int i = 0; i < actualTrainSize; i++) {
            this.trainSetIndex.add(indexes[i]);
            this.trainPlusValidationSetIndex.add(indexes[i]);
        }
        for (int i = actualTrainSize; i < actualTrainSize + validationSize; i++) {
            this.validationSetIndex.add(indexes[i]);
            this.trainPlusValidationSetIndex.add(indexes[i]);
        }
        for (int i = actualTrainSize + validationSize; i < indexes.length; i++) {
            this.testSetIndex.add(indexes[i]);
        }
    }

    public void InitTrainInfoGains() {

        double sum = 0;
        for (int attrIndex = 0; attrIndex < Constants.no_of_attr; attrIndex++) {
            double trainInfoGain = GetInfoGainByAttr(trainSetIndex, attrIndex);
            trainSetInfoGainValues.add(trainInfoGain);
            sum += trainInfoGain;
        }

        trainSetInfoGainProbabilities.add(0.0);
        for (int i = 1; i <= trainSetInfoGainValues.size(); i++) {
            double value =
                    trainSetInfoGainProbabilities.get(i - 1)
                    + (trainSetInfoGainValues.get(i - 1) / sum);
            trainSetInfoGainProbabilities.add(1 / value);
        }
    }

    public void InitValidationInfoGains() {

        double sum = 0;
        for (int attrIndex = 0; attrIndex < Constants.no_of_attr; attrIndex++) {
            double validationInfoGain = GetInfoGainByAttr(validationSetIndex, attrIndex);
            validationSetInfoGainValues.add(validationInfoGain);
            sum += validationInfoGain;
        }

        validationSetInfoGainProbabilities.add(0.0);
        for (int i = 1; i <= validationSetInfoGainValues.size(); i++) {
            double value =
                    validationSetInfoGainProbabilities.get(i - 1)
                    + (validationSetInfoGainValues.get(i - 1) / sum);
            validationSetInfoGainProbabilities.add(1 / value);
        }
    }

    public void InitTrainPlusValidationInfoGains() {

        double sum = 0;
        for (int attrIndex = 0; attrIndex < Constants.no_of_attr; attrIndex++) {
            double trainPlusValidationInfoGain = GetInfoGainByAttr(trainPlusValidationSetIndex, attrIndex);
            trainPlusValidationSetInfoGainValues.add(trainPlusValidationInfoGain);
            sum += trainPlusValidationInfoGain;
        }

        trainPlusValidationSetInfoGainProbabilities.add(0.0);
        for (int i = 1; i <= trainPlusValidationSetInfoGainValues.size(); i++) {
            double value =
                    trainPlusValidationSetInfoGainProbabilities.get(i - 1)
                    + (trainPlusValidationSetInfoGainValues.get(i - 1) / sum);
            trainPlusValidationSetInfoGainProbabilities.add(1 / value);
        }

    }

    public void DumpTrainSet() {
        ArrayList< ArrayList<Integer>> trainSet = new ArrayList< ArrayList<Integer>>();
        for (int i = 0; i < trainSetIndex.size(); i++) {
            trainSet.add(dataset.get(trainSetIndex.get(i)));
        }
        Writer.WriteFromArrayList(dataset_path +"_trainset.txt", trainSet);
    }

    public void DumpTestSet() {
        ArrayList< ArrayList<Integer>> testset = new ArrayList< ArrayList<Integer>>();
        for (int i = 0; i < testSetIndex.size(); i++) {
            testset.add(dataset.get(testSetIndex.get(i)));
        }
        Writer.WriteFromArrayList(dataset_path +"_testset.txt", testset);
    }

    public void PrintTrainSetIndex() {
        Printer.print("Train Set : " + trainSetIndex.size());
        Printer.print(trainSetIndex);
    }

    public void PrintValidationSetIndex() {
        Printer.print("Validation Set : " + validationSetIndex.size());
        Printer.print(validationSetIndex);
    }

    public void PrintTestSetIndex() {
        Printer.print("Test Set : " + testSetIndex.size());
        Printer.print(testSetIndex);
    }

    public void InitClassSepratedDataSet() {

        for (int i = 0; i < Constants.total_class; i++) {
            classSperatedDatsetIndex.add(new ArrayList());
        }
        for (int i = 0; i < trainSetIndex.size(); i++) {
            int classValue = dataset.get(trainSetIndex.get(i)).get(Constants.dataset_class_index);
            classSperatedDatsetIndex.get(classValue).add(i);
        }
    }

    public void PrintClassSepratedDataSetIndex() {
        Printer.print("Class Seprated Dataset " + classSperatedDatsetIndex.size());
        for (int i = 0; i < classSperatedDatsetIndex.size(); i++) {
            Printer.print("Class " + i + " : " + classSperatedDatsetIndex.get(i).size());
            Printer.print(classSperatedDatsetIndex.get(i));
        }
    }

    public int GetValueOfIndex(int datasetIndex, int attrIndex) {
        return dataset.get(datasetIndex).get(attrIndex);
    }

    public int GetClassValueOfIndex(int datasetIndex) {
        return GetValueOfIndex(datasetIndex, Constants.dataset_class_index);
    }

    public double GetEntropyValue(ArrayList<Integer> localDatasetIndex) {
        int positiveCount = 0;
        int negetiveCount = 0;
        for (int i = 0; i < localDatasetIndex.size(); i++) {
            if (GetClassValueOfIndex(localDatasetIndex.get(i)) == 1) {
                positiveCount++;
            } else {
                negetiveCount++;
            }
        }

        double positive_ratio = positiveCount / (double) localDatasetIndex.size();
        double negetive_ratio = negetiveCount / (double) localDatasetIndex.size();

        double entropy = -(positive_ratio) * Util.Log2(positive_ratio)
                - (negetive_ratio) * Util.Log2(negetive_ratio);

        return entropy;

    }

    public HashMap<Integer, ArrayList<Integer>> GetSplitedDataSetByAttr(ArrayList<Integer> localDataset, int attrIndex) {

        HashMap<Integer, ArrayList<Integer>> splitedDataset = new HashMap<Integer, ArrayList<Integer>>();

        for (int i = 0; i < localDataset.size(); i++) {
            int value = GetValueOfIndex(localDataset.get(i), attrIndex);
            if (splitedDataset.get(value) == null) {
                splitedDataset.put(value, new ArrayList<Integer>());
            } else {
                splitedDataset.get(value).add(localDataset.get(i));
            }
        }
        return splitedDataset;

    }

    public double GetInfoGainByAttr(ArrayList<Integer> localDataset, int attrIndex) {
        double infoGain = 0;
        double totalEntropy = GetEntropyValue(localDataset);
        double expectedEntropy = 0;

        HashMap<Integer, ArrayList<Integer>> splitedDataSet =
                GetSplitedDataSetByAttr(localDataset, attrIndex);


        for (int i = Constants.attr_domain_min[attrIndex]; i <= Constants.attr_domain_max[attrIndex]; i++) {

            if (splitedDataSet.get(i) != null) {
                double splitedEntropy = this.GetEntropyValue(splitedDataSet.get(i));
                if (splitedEntropy > 0) {
                    expectedEntropy += splitedDataSet.get(i).size()
                            * splitedEntropy / localDataset.size();
                }
            }
        }

        infoGain = totalEntropy - expectedEntropy;

        return infoGain;

    }

    //-------------------------------------- Rule Interestingness ------------------------------ //
    public double SingleGeneRI(Gene aGene,ArrayList<Integer> localDataset, int classNo) throws Exception {
        double ri = 0;
        int nBoth = 0;
        int nLeft = 0;
        int nRight = 0;

        for (int i = 0; i < localDataset.size(); i++) {

            if ( Datasets.datasetGenes.get(localDataset.get(i)).isEqual(aGene) ) {
                nLeft++;
                if (Datasets.dataset.get(localDataset.get(i)).get(Constants.dataset_class_index) == classNo) {
                    nBoth++;
                }
            }
            if (Datasets.dataset.get(localDataset.get(i)).get(Constants.dataset_class_index) == classNo) {
                nRight++;
            }
        }

        ri = nBoth - nLeft * nRight / localDataset.size();

        return ri;
    }

    public double SingleAttrSingleValRI(ArrayList<Integer> localDataset, int attrIndex, int attrValue, int classIndex) {

        double riVal = 0;

        int nBoth = 0;
        int nLeft = 0;
        int nRight = 0;
        for (int i = 0; i < localDataset.size(); i++) {

            if (GetValueOfIndex(localDataset.get(i), attrIndex) == attrValue) {
                nLeft++;
                if (GetClassValueOfIndex(localDataset.get(i)) == classIndex) {
                    nBoth++;
                }
            }
            if (GetClassValueOfIndex(localDataset.get(i)) == classIndex) {
                nRight++;
            }
        }
        riVal = nBoth - nLeft * (nRight / (double) localDataset.size());

        return riVal;


    }

    public double SingleAttrOverallRI(ArrayList<Integer> localDataset, int attrIndex) {

        double sum = 0;
        for (int attrVal = Constants.attr_domain_min[attrIndex];
                attrVal <= Constants.attr_domain_max[attrIndex]; attrVal++) {

            for (int classIndex = 0; classIndex < Constants.total_class; classIndex++) {
                sum += SingleAttrSingleValRI(localDataset, attrIndex, attrVal, classIndex);
            }

        }
//		Printer.print(sum);
        int k = Constants.attr_domain_max[attrIndex] - Constants.attr_domain_min[attrIndex] + 1;
        double ri = sum / 2 * k;
        return ri;

    }

    public void InitTrainAllAttrAllValRI() {

        double trainSum = 0;

        for (int j = 0; j < Constants.no_of_attr; j++) {
            double trainValue = SingleAttrOverallRI(trainSetIndex, j);
            this.trainSetRIValues.add(trainValue);
            trainSum += trainValue;
        }

        trainSetRIProbabilities.add(0.0);
        for (int i = 1; i <= trainSetRIValues.size(); i++) {
            double trainValue =
                    trainSetRIProbabilities.get(i - 1)
                    + (trainSetRIValues.get(i - 1) / trainSum);

            trainSetRIProbabilities.add(1 / trainValue);
        }

    }

    public void InitTrainPlusValidationAllAttrAllValRI() {

        double trainSum = 0;
        double validationSum = 0;
        double trainPlusValidationSum = 0;

        for (int j = 0; j < Constants.no_of_attr; j++) {
            double trainValue = SingleAttrOverallRI(trainSetIndex, j);
            double validationValue = SingleAttrOverallRI(validationSetIndex, j);
            double trainPlusValidationValue = SingleAttrOverallRI(trainPlusValidationSetIndex, j);
            this.trainSetRIValues.add(trainValue);
            this.validationSetRIValues.add(validationValue);
            this.trainPlusValidationSetRIValues.add(trainPlusValidationValue);

            trainSum += trainValue;
            validationSum += validationValue;
            trainPlusValidationSum += trainPlusValidationValue;
        }

        trainSetRIProbabilities.add(0.0);

        validationSetRIProbabilities.add(0.0);
        trainPlusValidationSetRIProbabilities.add(0.0);
        for (int i = 1; i <= Constants.no_of_attr; i++) {
            double trainValue =
                    trainSetRIProbabilities.get(i - 1)
                    + (trainSetRIValues.get(i - 1) / trainSum);
            double validationsValue =
                    validationSetRIProbabilities.get(i - 1)
                    + (validationSetRIValues.get(i - 1) / validationSum);
            double trainPlusValidationsValue =
                    trainPlusValidationSetRIProbabilities.get(i - 1)
                    + (this.trainPlusValidationSetRIValues.get(i - 1) / trainPlusValidationSum);

            validationSetRIProbabilities.add(1 / trainValue);
            trainSetRIProbabilities.add(1 / validationsValue);
            trainPlusValidationSetRIProbabilities.add(1 / trainPlusValidationsValue);
        }

    }
    //-------------------------------------- Rule Interestingness ------------------------------ //
}
