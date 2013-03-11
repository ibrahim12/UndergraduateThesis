/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Logic;

import IR.Config;
import IR.Constants;
import IR.MyPair;
import java.util.ArrayList;
import java.util.Collections;

import com.sun.corba.se.spi.legacy.connection.GetEndPointInfoAgainException;

import utility.GraphDrawing;
import utility.Printer;
import utility.Writer;

/**
 *
 * @author ibrahim
 */
public class Evalutor {

    Datasets allDatasets = null;
    Population population = null;
    ArrayList<Integer> current_dataset = null;
    Config config = null;
    int mutation_phase_select = -1;
    double mutation_rate_phase_1 = -1;
    double mutation_rate_phase_2 = -1;
    int mutation_phase_2_dataset = -1;
    int mutation_phase_2_process = -1;
    int no_mutation_point = -1;
    double selection_rate = -1;
    int phase1IterationCount = -1;
    int phase2IterationCount = -1;
    int generationCount = 0;
    Chromosome bestone = null;
    Property[] prevProperties = new Property[2];

    public Evalutor(Population population, Datasets allDatasets, Config config) {
        this.population = population;
        this.allDatasets = allDatasets;
        current_dataset = allDatasets.trainSetIndex;
        this.config = config;

        mutation_phase_select = config.getInt("mutation_phase_select");
        no_mutation_point = config.getInt("no_mutation_point");
        mutation_rate_phase_1 = config.getDouble("mutation_rate_phase_1");
        mutation_rate_phase_2 = config.getDouble("mutation_rate_phase_2");
        mutation_phase_2_dataset = config.getInt("mutation_phase_2_dataset");
        mutation_phase_2_process = config.getInt("mutation_phase_2_process");

        selection_rate = config.getDouble("selection_rate");

        this.phase1IterationCount = (int) (Constants.algo_iteration_count_max * Constants.mutation_phase_1_percentage);
        this.phase2IterationCount = Constants.algo_iteration_count_max - this.phase1IterationCount;

        this.population.setParameters(mutation_phase_select, mutation_rate_phase_1, mutation_rate_phase_2, no_mutation_point, selection_rate);

    }

    public void setMutationRatePhase1(int mutation_rate_phase_1) {
        this.mutation_rate_phase_1 = mutation_rate_phase_1;
    }

    public void setMutationRatePhase2(int mutation_rate_phase_2) {
        this.mutation_rate_phase_2 = mutation_rate_phase_2;
    }

    public void updateChromosomeProperties(int chromosomeIndex) throws Exception {
        Chromosome aChromosome = population.chromosomes.get(chromosomeIndex);
        int[][] confusitonMatrix = Tester.Test(aChromosome, current_dataset, population.classIndex);
//        int correct = Tester.Test(aChromosome, current_dataset, population.classIndex);
//        Printer.print(population.classIndex);
//        Printer.print(Datasets.getClassCount(current_dataset));
//            Printer.print(confusitonMatrix);
        
        double generelity = aChromosome.getGenerality();
        aChromosome.properties.setGenerality(generelity);
        
//        double accuracy = correct / (double) current_dataset.size();
//        aChromosome.properties.setAccuracy(accuracy);
//        
//        aChromosome.properties.updateFitness();
//        Printer.print(correct + "/" + current_dataset.size());
        aChromosome.properties.updateAll(confusitonMatrix);

    }

    public void calculatePopulationProperties() throws Exception {

        if (current_dataset == null) {
            throw new Exception("Current Dataset Null Exception");
        }
        for (int chromosomeIndex = 0; chromosomeIndex < population.size(); chromosomeIndex++) {
            updateChromosomeProperties(chromosomeIndex);
        }

    }

    public ArrayList<Double> getRaulatteProbabilites() {
        ArrayList<Double> raulatteProbabilities = null;
        if (mutation_phase_select == 1 && mutation_phase_2_dataset == 0) {
            raulatteProbabilities = allDatasets.trainSetInfoGainProbabilities;
        } else if (mutation_phase_select == 1 && mutation_phase_2_dataset == 1) {
            raulatteProbabilities = allDatasets.trainPlusValidationSetInfoGainProbabilities;
        } else if (mutation_phase_select == 2 && mutation_phase_2_dataset == 0) {
            raulatteProbabilities = allDatasets.trainSetRIProbabilities;
        } else if (mutation_phase_select == 2 && mutation_phase_2_dataset == 1) {
            raulatteProbabilities = allDatasets.trainPlusValidationSetRIProbabilities;
        } else if (mutation_phase_select == 1 && mutation_phase_2_dataset == 2) {
            raulatteProbabilities = allDatasets.validationSetInfoGainValues;
        } else if (mutation_phase_select == 2 && mutation_phase_2_dataset == 2) {
            raulatteProbabilities = allDatasets.validationSetRIProbabilities;
        }
        return raulatteProbabilities;
    }

    public void printBestone(Chromosome bestone) {
        if (config.getInt("debug") == 1) {
            String result = "Class " + this.population.classIndex + " Fitness " + bestone.properties.getFitness() + " Accuracy " + bestone.properties.getAccuracy();
            String out = bestone.properties.getConfustionMatrix() + "\n" + this.population.toString() + "\n" + result;
            if (config.getInt("file_debug") == 1) {
                Writer.WriteFromString(config.get("debug_path"), out);
            }
            Printer.print(result);
        }
    }

    public Chromosome getPopulationBest(Chromosome bestone) {
        Collections.sort(population.chromosomes);
        if (bestone == null
                || population.chromosomes.get(0).properties.getFitness() > bestone.properties.getFitness()) {
            bestone = population.chromosomes.get(0).clone();
            printBestone(bestone);
        }
        return bestone;
    }
    
    public int getPopulationAvgError(){
    	int error = 0;
    	for(int i=0;i<this.population.chromosomes.size();i++){
    		error += this.population.chromosomes.get(i).properties.getError();
    	}
    	return error/this.population.size();
    }
    public void WriteErrorLog(int phaseNo){
//        Chromosome bestone = population.chromosomes.get(0).clone();
//        Chromosome worstOne = population.chromosomes.get(population.chromosomes.size()-1).clone();
//        Chromosome avgOne = population.chromosomes.get(population.chromosomes.size()/2).clone();
//    	int bestone = this.bestone.properties.getError() ;
    	if(generationCount 	% config.getInt("sampleInterval") == 0)
    	{
	    	int bestone = population.chromosomes.get(0).properties.getError();
//    		int bestone = this.bestone.properties.getError() ;
	        int worstOne = population.chromosomes.get(population.chromosomes.size()-1).properties.getError();	        
	        int avgOne =  getPopulationAvgError();      
	        if(bestone != 0 || worstOne != 0 || avgOne != 0)
	        	Main.stats.addGenVsError(generationCount, bestone, avgOne, worstOne,
	        			this.population.classIndex,phaseNo);
    	}
      
    }
    public void evaluatePhase1() throws Exception {

        current_dataset = allDatasets.trainSetIndex;
//        String prevPopulation = "";

        for (int i = 0; i < this.phase1IterationCount; i++,generationCount++) {

            calculatePopulationProperties();

            Population newPopulation = this.population.GetNewPopulation(); //CrossOverDone                
            newPopulation.doPhase1Mutation();

//            Printer.print(this.population.size());
            this.population = newPopulation;
//            Printer.print(this.population.size());

            bestone = this.getPopulationBest(bestone); //Sorted            
            WriteErrorLog(1);
           

//            if(prevPopulation == null ? population.toString() == null : prevPopulation.equals(population.toString())){
//                Printer.print("Population Equal");
//            }
//            prevPopulation = population.toString();
//            Chromosome temp = population.chromosomes.get(0);
//            String result = "Class " + this.population.classIndex + " Fitness " + temp.properties.getFitness()
//                    + " Accuracy " + temp.properties.getAccuracy();
//            Printer.print(result);
//            Printer.print(population.toString());

        }

    }

    public void reInitForPhase2() throws Exception {

//        Printer.print("After Phase 1 Property " + bestone.properties);

//        if (this.mutation_phase_2_dataset == 0) {
//            current_dataset = allDatasets.trainSetIndex;
//        } else if (this.mutation_phase_2_dataset == 1) {
//            current_dataset = allDatasets.trainPlusValidationSetIndex;
//        } else if (this.mutation_phase_2_dataset == 2) {
//            current_dataset = allDatasets.validationSetIndex;
//        }
        
        
        if(mutation_phase_2_dataset == 0)
            current_dataset = allDatasets.trainSetIndex;
        else
            current_dataset = allDatasets.validationSetIndex;

//        Printer.print(current_dataset);
        this.population.addChromosome(bestone);
        calculatePopulationProperties();
        Collections.sort(population.chromosomes);
        this.population.chromosomes.remove(this.population.chromosomes.size() - 1);
//        bestone = population.chromosomes.get(0);

//        Printer.print("Before Phase 2 Property " + population.chromosomes.get(0).properties);

    }

    public void doEvalutionaryPruning() throws Exception {
        for (int i = 0; i < this.phase2IterationCount; i++,generationCount++) {

            calculatePopulationProperties();
            Population newPopulation = this.population.GetNewPopulation(); //CrossOverDone                
            newPopulation.doPhase2Mutation(getRaulatteProbabilites());

//            Printer.print(this.population.size());
            this.population = newPopulation;
//            Printer.print(this.population.size());

            bestone = this.getPopulationBest(bestone);
            if (Constants.adaptive_mutation_enabled == 1) {
                adjustAdaptiveMutationRate();
            }
            WriteErrorLog(2);
        }

    }

    public ArrayList<Double> getDeterministicPruningRIValues() {

        ArrayList<Double> pruningRiValues = null;
        if (mutation_phase_2_dataset == 0) {
            pruningRiValues = allDatasets.trainSetRIValues;
        } else if (mutation_phase_2_dataset == 1) {
            pruningRiValues = allDatasets.trainPlusValidationSetRIValues;
        } else if (mutation_phase_2_dataset == 2) {
            pruningRiValues = allDatasets.validationSetRIValues;
        }
        return pruningRiValues;
    }
    
    public ArrayList<Double> getDeterministicPruningInfoGainValues() {

        ArrayList<Double> pruningInfoGainValues = null;
        if (mutation_phase_2_dataset == 0) {
        	pruningInfoGainValues = allDatasets.trainSetInfoGainValues;
        } else if (mutation_phase_2_dataset == 1) {
        	pruningInfoGainValues = allDatasets.trainPlusValidationSetInfoGainValues;
        } else if (mutation_phase_2_dataset == 2) {
        	pruningInfoGainValues = allDatasets.validationSetInfoGainValues;
        }
        return pruningInfoGainValues;
    }

    public void doDeterministicPruning() throws Exception {

    	ArrayList<Double> attSelectorValues = null;
    	boolean sortType = false;
    	if(mutation_phase_select == 1){
    		attSelectorValues = getDeterministicPruningInfoGainValues();
    		sortType = false;
    	}else if(mutation_phase_select == 2){
    		attSelectorValues = getDeterministicPruningRIValues();
    		sortType = true;
    	}
        
        ArrayList<MyPair> allAttrSelectorValues = new ArrayList<MyPair>();
        for (int i = 0; i < attSelectorValues.size(); i++) {
            allAttrSelectorValues.add(new MyPair(i, attSelectorValues.get(i), sortType));
        }
        Collections.sort(allAttrSelectorValues);

        for (int chromsomeIndex = 0; chromsomeIndex < population.chromosomes.size(); chromsomeIndex++) {
            Chromosome aChromosome = population.chromosomes.get(chromsomeIndex);

            //Duplicate removed
            aChromosome.removeDuplicateGenes();

            //Recalculate Chromosome Properties
            updateChromosomeProperties(chromsomeIndex);

            //Recalculate Accuracy
            calculatePopulationProperties();

            //Chromosome Gene Sorted By RI
            ArrayList<MyPair> allGenes = new ArrayList<MyPair>();
            for (int j = 0; j < aChromosome.genes.size(); j++) {
                Gene aGene = aChromosome.genes.get(j);              
//                double ri = allDatasets.SingleGeneRI(aGene, current_dataset, population.classIndex);
//                allGenes.add(new MyPair(j, ri, true));
                double accuracy = Tester.getGeneAccuracy(aGene, current_dataset); 
              allGenes.add(new MyPair(j, accuracy, true));

            }
            Collections.sort(allGenes);

            double alpha = .33; //Not using
            double beta = .33; //Not using
            double gama = .33; //Not using
            double ZN = 1.95;

            //Semi_Deterministic Pruning
            for (int geneIndex = 0; geneIndex < aChromosome.genes.size(); geneIndex++) {
                Gene aGene = aChromosome.genes.get(allGenes.get(geneIndex).index);
                for (int alleyIndex = 0; alleyIndex < aGene.size(); alleyIndex++) {

                    int attrIndex = allAttrSelectorValues.get(alleyIndex).index;

                    int beforeValue = aGene.allelies.get(attrIndex);

                    double accuracyBefore = aChromosome.properties.getAccuracy();

                    if(mutation_phase_2_dataset == 0)
                        accuracyBefore =  accuracyBefore - ZN * Math.sqrt(   accuracyBefore *(1- accuracyBefore )/current_dataset.size()   );

                    
//                  double ditFitnessBefore = alpha * aChromosome.properties.getAccuracy() +
//                                        beta * aChromosome.properties.getRecall() + 
//                                        gama * aChromosome.properties.getPrecison();


                    aGene.allelies.set(attrIndex, Constants.attr_dont_care_value);
                    updateChromosomeProperties(chromsomeIndex);

                    double accuracyAfter = aChromosome.properties.getAccuracy();
                    
                    if(mutation_phase_2_dataset == 0)
                        accuracyAfter =  accuracyAfter - ZN * Math.sqrt(   accuracyAfter *(1- accuracyAfter )/current_dataset.size()   );
                    
//                    double ditFitnessAfter = alpha * aChromosome.properties.getAccuracy() +
//                                        beta * aChromosome.properties.getRecall() + 
//                                        gama * aChromosome.properties.getPrecison();

                    if (accuracyAfter <= accuracyBefore) {
//                    if(ditFitnessAfter <= ditFitnessBefore){
                        aGene.allelies.set(attrIndex, beforeValue);
                        break;                                          //Break for Semi-Deterministic
                    } else {
                        //Printer.print("Updated " + accuracyBefore + " > " + accuracyAfter);
//                        Printer.print("Updated " + ditFitnessBefore + " > "+ ditFitnessAfter );
                    }

                }
            }

        }

        calculatePopulationProperties();
        Collections.sort(population.chromosomes);
        bestone = population.chromosomes.get(0);

    }

    public void adjustAdaptiveMutationRate() {

        if (prevProperties[0] != null && prevProperties[1] == null) {
            prevProperties[1] = this.population.chromosomes.get(0).properties;
        }

        if (prevProperties[0] == null) {
            prevProperties[0] = this.population.chromosomes.get(0).properties;
        }


        if (prevProperties[0] != null && prevProperties[1] != null) {
            this.mutation_rate_phase_2 -=
                    (prevProperties[1].getGenerality() * this.mutation_rate_phase_2)
                    + (prevProperties[1].getAccuracy() - prevProperties[0].getAccuracy()) * this.mutation_rate_phase_2;

            prevProperties[0] = prevProperties[1];
            prevProperties[1] = this.population.chromosomes.get(0).properties;

//            Printer.print("MutataitonRate udpated : " + this.mutation_rate_phase_2);

        }
    }

    public void evaluatePhase2() throws Exception {
//        Printer.print("Phase 2 Starting");

        if (mutation_phase_2_process == 0) {
            doEvalutionaryPruning();
        } else if (mutation_phase_2_process == 1) {
            doDeterministicPruning();
        }

    }

    public Chromosome doEvalute() throws Exception {

        evaluatePhase1();
        reInitForPhase2();
        evaluatePhase2();
        Main.stats.generatePlots(this.population.classIndex);
        return bestone;
    }

    public void PrintPopulationDataset() {
        Printer.print("Populations:: size :" + this.population.size());
        for (int classIndex = 0; classIndex < this.population.size(); classIndex++) {
            Printer.print("Class " + classIndex + " :: " + this.population.size());

            for (int choromosomeIndex = 0;
                    choromosomeIndex < this.population.size(); choromosomeIndex++) {
                Printer.print("Chromosome " + choromosomeIndex + " :: " + this.population.chromosomes.get(choromosomeIndex).size());
                Printer.print(this.population.chromosomes.get(choromosomeIndex).genes);
            }
        }
    }
}
