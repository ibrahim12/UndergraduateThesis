/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Logic;

import IR.Config;
import java.util.ArrayList;
import java.util.Collections;

import utility.Printer;
import utility.Util;

/**
 *
 * @author ibrahim
 */
public class Population {

    int no_mutation_point = -1;
    double mutation_point_phase_1 = -1;
    double mutation_point_phase_2 = -1;
    double selection_rate = -1;
    int mutation_phase_select = -1;
    int classIndex = -1;
    ArrayList<Chromosome> chromosomes = new ArrayList<Chromosome>();

    public Population(int classIndex) {
        this.classIndex = classIndex;
    }

    public void setParameters(int mutation_phase_select, double mutation_rate_phase_1,
            double mutation_rate_phase_2, int no_mutation_point,
            double selection_rate) {
        this.mutation_point_phase_1 = mutation_rate_phase_1;
        this.mutation_point_phase_2 = mutation_rate_phase_2;
        this.no_mutation_point = no_mutation_point;
        this.mutation_phase_select = mutation_phase_select;
        this.selection_rate = selection_rate;

    }

    public void setParameters(Config config) {
        this.mutation_point_phase_1 = config.getDouble("mutation_rate_phase_1");
        this.mutation_point_phase_2 = config.getDouble("mutation_rate_phase_2");
        this.no_mutation_point = config.getInt("no_mutation_point");
        this.mutation_phase_select = config.getInt("mutation_phase_select");
        this.selection_rate = config.getDouble("selection_rate");
    }

    public void addChromosome(Chromosome aChromosome) {
        this.chromosomes.add(aChromosome);
    }

    public int size() {
        return chromosomes.size();
    }

    @Override
    public String toString() {
        return "Population{" + "classIndex=" + classIndex + ", chromosomes=" + chromosomes + '}' + '\n';
    }

    public double[] GetProbabilities() {
        double sum = 0;
        for (int i = 0; i < this.size(); i++) {
            sum += this.chromosomes.get(i).properties.getFitness();
        }
        double[] probabilites = new double[this.size() + 1];
        probabilites[0] = 0;
        for (int i = 1; i <= this.size(); i++) {
            probabilites[i] = probabilites[i - 1]
                    + (this.chromosomes.get(i - 1).properties.getFitness() / sum);
        }
        return probabilites;
    }
    
    public boolean emptyCheckForChromosmoe(){
    	boolean flag = false;
    	for(int i=0;i<this.size();i++){
    		if(this.chromosomes.get(i).size() == 0){
    			Printer.print("i " + i);
    			flag = true;
    		}
    	}
    	return flag;
    }
    
    public boolean emptyCheckForChromosmoe(Population newPop){
    	boolean flag = false;
    	for(int i=0;i<newPop.size();i++){
    		if(newPop.chromosomes.get(i).size() == 0){
    			Printer.print("i " + i);
    			flag = true;
    		}
    	}
    	return flag;
    }

    public Population GetNewPopulation() {

    	
    	
        int parentSize = (int) (this.size() * selection_rate);
        int childSize = this.size() - parentSize;
        if( (childSize%2) != 0){ 
            childSize++;       
            parentSize--;
        }

//        Printer.print("Population Size " + this.size());
//        Printer.print("Parent Size " + parentSize);
//        Printer.print("Child Size " + childSize);

        double[] probabilities = GetProbabilities();

        ArrayList<Integer> newIndexes = new ArrayList<Integer>();
        while (newIndexes.size() < this.size()) {
            newIndexes.add(Util.GetRandomRaulatteIndex(probabilities));
        }

        Population newPopulation = new Population(this.classIndex);
        newPopulation.setParameters(mutation_phase_select, mutation_point_phase_1,
                mutation_point_phase_2, no_mutation_point, selection_rate);

//        Collections.shuffle(newIndexes);
        for (int i = 0; i < parentSize; i++) {
            newPopulation.addChromosome(this.chromosomes.get(newIndexes.get(i)));
        }
        int childLen = parentSize + (int)Math.ceil(childSize / 2.0);
        for (int i = parentSize; i < childLen; i++) {
        	Chromosome candidate = this.chromosomes.get(i + 1).clone();
        	if(candidate.size() <= 0){
        		Printer.print("Chromosome Size " + candidate.size() + " i " + i);
        		Printer.print("Parent Size " + parentSize + " " + " Child Size " + childSize );
        		Printer.print("Population Size " + this.size());
        	} 
            Chromosome[] offsprings = this.chromosomes.get(i).CrossOver(candidate);
            newPopulation.addChromosome(offsprings[0]);
            newPopulation.addChromosome(offsprings[1]);
        }
        if(emptyCheckForChromosmoe(newPopulation)){
        	Printer.print("Population Size " + newPopulation.size());
        	System.exit(0);
        }
        return newPopulation;

    }

    public void doPhase1Mutation() {

        for (int i = 0; i < this.size(); i++) {
            this.chromosomes.get(i).mutatePhase1();
        }
    }

    public void doPhase2Mutation(ArrayList<Double> roulatteProbabilites) {

        for (int i = 0; i < this.size(); i++) {
            this.chromosomes.get(i).mutatePhase2(roulatteProbabilites);
        }
    }
}
