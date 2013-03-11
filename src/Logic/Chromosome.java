/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Logic;

import IR.Constants;
import java.util.ArrayList;
import utility.Printer;
import utility.Util;

/**
 *
 * @author ibrahim
 */
public class Chromosome implements Comparable {

    boolean assendingSort = false;
    ArrayList<Gene> genes = new ArrayList<Gene>();
    Property properties = null;
    private Population population = null;

    public Chromosome(ArrayList<Gene> genes, Population population) {
        this.genes = genes;
        properties = new Property(Constants.fitness_alpha, Constants.fitness_beta);
        this.population = population;

    }

    public int size() {
        return this.genes.size();
    }

    @Override
	public Chromosome clone() {
        ArrayList<Gene> newGenes = new ArrayList<Gene>();
        for (int i = 0; i < genes.size(); i++) {
            newGenes.add(genes.get(i).clone());
        }
        Chromosome newChromosome = new Chromosome(newGenes, this.population);
        newChromosome.assendingSort = this.assendingSort;
        newChromosome.properties = this.properties.clone();
        newChromosome.population = this.population;
        return newChromosome;
    }

    public double getGenerality() {
        return dontCareAlleleCount() / (double) (Constants.no_of_attr * size());
    }

    @Override
    public String toString() {
        return "Chromosome{" + '\n' + "genes=" + '\n' + genes + '\n' + '}';
    }

    public void removeDuplicateGenes() throws Exception {

//        ArrayList<Integer> duplicateGeneIndex = new ArrayList<Integer>();
//        Printer.print(this.genes.size());
        for (int i = 0; i < this.size(); i++) {
            for (int j = i + 1; j < this.size() - 1; j++) {
                if (genes.get(i).isEqual(genes.get(j))) {
                    //duplicateGeneIndex.add(i);
                    this.genes.remove(this.genes.get(j));
                }
            }
        }

//        Printer.print(duplicateGeneIndex);
//        for(int i=0;i<duplicateGeneIndex.size();i++){
//            this.genes.remove(this.genes.get(duplicateGeneIndex.get(i)));
//        }
//        Printer.print(this.genes.size());
    }

    public int maxAlleleMatch(Gene aGene) throws Exception {

        int maxCount = Integer.MIN_VALUE;
        for (int geneIndex = 0; geneIndex < this.size(); geneIndex++) {

            int value = this.genes.get(geneIndex).precentageEqual(aGene);
            if (maxCount < value) {
                maxCount = value;
            }
        }
        return maxCount;
    }

    public int sumAlleleMatch(Gene aGene) throws Exception {

        int sum = 0;
        for (int geneIndex = 0; geneIndex < this.size(); geneIndex++) {

            sum += this.genes.get(geneIndex).precentageEqual(aGene);
        }
        return sum;
    }

    public int dontCareAlleleCount() {
        int count = 0;
        for (int geneIndex = 0; geneIndex < this.size(); geneIndex++) {

            count += this.genes.get(geneIndex).dontCareAlleleCount();

        }
        return count;
    }
    
    public int getValidAlleleCount(){
        return ( this.genes.size() * Constants.no_of_attr ) - dontCareAlleleCount();
    }
    

    public Chromosome[] CrossOver(Chromosome bChromosome) {
    	

        Chromosome aChromosome = this.clone();

        int totalAttrCount = this.size() * Constants.no_of_attr;
        
        if(totalAttrCount <= 0 )Printer.print(this.size() + " " + Constants.no_of_attr);
        
        int pair1CrossOverStart = Util.RandomInt(totalAttrCount);
        int pair2CrossOverStart = Util.RandomInt(totalAttrCount);
        int crossOverLength = Util.RandomInt(1, totalAttrCount);

        int validEnd = Math.min(totalAttrCount - pair2CrossOverStart, totalAttrCount - pair1CrossOverStart);
        crossOverLength = Math.min(crossOverLength, validEnd);

//		Printer.print("--");
//                Printer.print("Total Len     " + totalAttrCount);
//		Printer.print("Start         " + pair1CrossOverStart);
//		Printer.print("End           " + pair2CrossOverStart);
//		Printer.print("CorssOver Len " + crossOverLength);


        Chromosome[] offspring = new Chromosome[2];
        offspring[0] = aChromosome;
        offspring[1] = bChromosome.clone();

        int len = crossOverLength;
        for (int index = 0; index < len; index++) {
            int pair1GeneIndex = (pair1CrossOverStart + index) / Constants.no_of_attr;
            int pair1AlleyIndex = (pair1CrossOverStart + index) % Constants.no_of_attr;

            int pair2GeneIndex = (pair2CrossOverStart + index) / Constants.no_of_attr;
            int pair2AlleyIndex = (pair2CrossOverStart + index) % Constants.no_of_attr;

            int pair1Alley = offspring[0].genes.get(pair1GeneIndex).allelies.get(pair1AlleyIndex);
            int pair2Alley = offspring[1].genes.get(pair2GeneIndex).allelies.get(pair2AlleyIndex);


            offspring[0].genes.get(pair1GeneIndex).allelies.set(pair1AlleyIndex, pair2Alley);
            offspring[1].genes.get(pair2GeneIndex).allelies.set(pair2AlleyIndex, pair1Alley);
//			Printer.print("Gene Index "+pair1GeneIndex + " " + pair1AlleyIndex);
//			Printer.print("Gene Index "+pair2GeneIndex + " " + pair2AlleyIndex);
//			Printer.print("--");
        }

//		matePair1.PrintAsInt();
//		offspring[0].PrintAsInt();
//
//		matePair2.PrintAsInt();
//		offspring[1].PrintAsInt();
//		System.exit(0);

        return offspring;



    }

    public void mutatePhase1() {

        int totalAttrCount = this.size() * Constants.no_of_attr;
        int[] mutationPoints = Util.GetNRandomInt(this.population.no_mutation_point, 0, totalAttrCount);
//		 Printer.print(mutationPoints);


//        Printer.print(this.genes);
        for (int i = 0; i < mutationPoints.length; i++) {
            int selectedGeneIndex = mutationPoints[i] / Constants.no_of_attr;
            int selectedAlleleIndex = mutationPoints[i] % Constants.no_of_attr;
//            Printer.print(selectedGeneIndex + " " + selectedAlleleIndex);
            int val = Util.RandomInt(Constants.attr_domain_min[selectedAlleleIndex],
                    Constants.attr_domain_max[selectedAlleleIndex]);
            
//            int prevVal = this.genes.get(selectedGeneIndex).allelies.get(selectedAlleleIndex);

//            Printer.print(Constants.attr_domain_min);
//            Printer.print(Constants.attr_domain_max);
//            Printer.print(val +"->" +prevVal);

//            if (selectedAlleleIndex == Constants.dataset_class_index) {
//            }
            
            this.genes.get(selectedGeneIndex).allelies.set(selectedAlleleIndex, val);

        }

//        Printer.print(this.genes);


    }

    public void mutatePhase2(ArrayList<Double> roulatteProbabilites) {
        if (this.population.mutation_phase_select == 0) { //Random
            this.mutatePhase2Random();
        } else if (this.population.mutation_phase_select == 1) { //InfoGain            
            this.mutatePhase2InfoGain(roulatteProbabilites);
        } else if (this.population.mutation_phase_select == 2) { //RuleInterstingness
            this.mutatePhase2RI(roulatteProbabilites);
        }

    }

    
    public void mutatePhase2Random() {

        int totalAttrCount = this.size() * Constants.no_of_attr;
        int[] mutationPoints = Util.GetNRandomInt(this.population.no_mutation_point, 0, totalAttrCount);
//		 Printer.print(mutationPoints);

        // offspring.genes[selectedGeneIndex].Print(true);
        // Printer.print("");

        for (int i = 0; i < mutationPoints.length; i++) {
            int selectedGeneIndex = mutationPoints[i] / Constants.no_of_attr;
            int selectedAlleleIndex = mutationPoints[i] % Constants.no_of_attr;
//            Printer.print(selectedGeneIndex + " " + selectedAlleleIndex);            
            
            
            this.genes.get(selectedGeneIndex).allelies.set(selectedAlleleIndex, Constants.attr_dont_care_value);
            
//            if (this.genes.get(selectedGeneIndex).getGenerality() < Constants.generality_threshold) {
//                
//            }else{
//                
//                Printer.print("Generality Threshold Crossed. " +this.genes.get(selectedGeneIndex).getGenerality());
//            }

        }

        // offspring.genes[selectedGeneIndex].Print(true);
//		this.PrintAsInt();
//		offspring.PrintAsInt();
//		System.exit(0);


    }

    public void mutatePhase2InfoGain(ArrayList<Double> roulatteProbabilites) {

        int selectedGeneIndex = Util.RandomInt(size());
        int raulatteIndex = Util.GetRandomRaulatteIndex(roulatteProbabilites);

        this.genes.get(selectedGeneIndex).allelies.set(raulatteIndex, Constants.attr_dont_care_value);
//        if (this.genes.get(selectedGeneIndex).getGenerality() < Constants.generality_threshold) {
//            
//        }else{
////             Printer.print("Generality Threshold Crossed. " +this.genes.get(selectedGeneIndex).getGenerality());
//        }

    }

    public void mutatePhase2RI(ArrayList<Double> roulatteProbabilites) {

        int selectedGeneIndex = Util.RandomInt(size());
        int raulatteIndex = Util.GetRandomRaulatteIndex(roulatteProbabilites);

        
        this.genes.get(selectedGeneIndex).allelies.set(raulatteIndex, Constants.attr_dont_care_value);
        
//        if (this.genes.get(selectedGeneIndex).getGenerality() < Constants.generality_threshold) {
//            
//        }else{
////             Printer.print("Generality Threshold Crossed. " +this.genes.get(selectedGeneIndex).getGenerality());
//        }

    }
    
    public void deocdeRule(){
    	
    }
    

    public void setAssendingSort(boolean assendingSort) {
        this.assendingSort = assendingSort;
    }

    @Override
    public int compareTo(Object t) {

        Chromosome tmp = (Chromosome) t;

        if (this.properties.getFitness() < tmp.properties.getFitness()) {
            /*
             * instance lt received
             */
            return assendingSort ? -1 : 1;
        } else if (this.properties.getFitness() > tmp.properties.getFitness()) {
            /*
             * instance gt received
             */
            return assendingSort ? 1 : -1;

        }
        /*
         * instance == received
         */
        return 0;
    }
}
