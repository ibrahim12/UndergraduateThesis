package Logic;

import IR.Constants;

public class Property {

    double accuracy;
    double generality;
    double recall;
    double precison;
    double fMeasure;
    double gMean;
    double tpRate;
    double fpRate;
    double sensitivity;
    double specificity;
    double fitness;
    double fitnessAlpha;
    double fitnessBeta;    
    int[][] confustionMatrix;

    public Property(double fitnessAlpha, double fitnessBeta) {

        this.accuracy = 0;
        this.generality = 0;
        this.recall = 0;
        this.precison = 0;
        this.fMeasure = 0;
        this.gMean = 0;
        this.sensitivity = 0;
        this.specificity = 0;
        this.fitness = 0;
        this.fitnessAlpha = fitnessAlpha;
        this.fitnessBeta = fitnessBeta;        
        confustionMatrix = null;

    }

    public int getError() {
        int error = this.confustionMatrix[0][1] + this.confustionMatrix[1][0];
        int total = this.confustionMatrix[0][1] + this.confustionMatrix[1][0] + 
        			 this.confustionMatrix[1][1] + this.confustionMatrix[0][0];
        
//        return (int)((error*100.0)/(total));
        return error;
        
    }

        
    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public double getGenerality() {
        return generality;
    }

    public void setGenerality(double generality) {
        this.generality = generality;
    }

    public double getRecall() {
        return recall;
    }

    public void setRecall(double recall) {
        this.recall = recall;
    }

    public double getPrecison() {
        return precison;
    }

    public void setPrecison(double precison) {
        this.precison = precison;
    }

    public double getfMeasure() {
        return fMeasure;
    }

    public void setfMeasure(double fMeasure) {
        this.fMeasure = fMeasure;
    }

    public double getgMean() {
        return gMean;
    }

    public void setgMean(double gMean) {
        this.gMean = gMean;
    }

    public double getTpRate() {
        return tpRate;
    }

    public void setTpRate(double tpRate) {
        this.tpRate = tpRate;
    }

    public double getFpRate() {
        return fpRate;
    }

    public void setFpRate(double fpRate) {
        this.fpRate = fpRate;
    }

    public double getSensitivity() {
        return sensitivity;
    }

    public void setSensitivity(double sensitivity) {
        this.sensitivity = sensitivity;
    }

    public double getSpecificity() {
        return specificity;
    }

    public void setSpecificity(double specificity) {
        this.specificity = specificity;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public double getFitnessAlpha() {
        return fitnessAlpha;
    }

    public void setFitnessAlpha(double fitnessAlpha) {
        this.fitnessAlpha = fitnessAlpha;
    }

    public double getFitnessBeta() {
        return fitnessBeta;
    }

    public void setFitnessBeta(double fitnessBeta) {
        this.fitnessBeta = fitnessBeta;
    }

    public int[][] getConfustionMatrix() {
        return confustionMatrix;
    }

    @Override
    protected Property clone() {
        Property newProperty = new Property(this.fitnessAlpha, this.fitnessBeta);
        newProperty.setAccuracy(this.getAccuracy());
        newProperty.setfMeasure(this.getfMeasure());
        newProperty.setFpRate(this.getFpRate());
        newProperty.setGenerality(this.getGenerality());
        newProperty.setgMean(this.getgMean());
        newProperty.setPrecison(this.getPrecison());
        newProperty.setRecall(this.getRecall());
        newProperty.setTpRate(this.getRecall());
        newProperty.setSensitivity(sensitivity);
        newProperty.setSpecificity(specificity);
        newProperty.setFitness(fitness);
        newProperty.confustionMatrix = this.confustionMatrix;
       
        return newProperty;
    }

    public void updateFitness() {


        double fitness = -1;
        if (Constants.fitness_type == 0) {
            fitness = fitnessAlpha * accuracy + fitnessBeta * generality;
        } else if (Constants.fitness_type == 1) {
            fitness = fitnessAlpha * (specificity * sensitivity) + fitnessBeta * generality;
        }

        setFitness(fitness);
    }

    public void updateAll(int[][] confusionMatrix) {

//        Printer.print(confusionMatrix);
        this.confustionMatrix = confusionMatrix;

        double tp = confusionMatrix[1][1];
        double tn = confusionMatrix[0][0];
        double fp = confusionMatrix[0][1];
        double fn = confusionMatrix[1][0];

        double recall = 0;
        if (tp + fn != 0) {
            recall = tp / (tp + fn);
        }

        double precision = 0;
        if (tp + fp != 0) {
            precision = tp / (tp + fp);
        }

        double accuracy = 0;
        if (tp + tn + fp + fn != 0) {
            accuracy = (tp + tn) / (tp + tn + fp + fn);
        }

        double tpRate = 0;
        if ((tp + fn) != 0) {
            tpRate = tp / (tp + fn);
        }

        double fpRate = 0;
        if (fp + tn == 0) {
            fpRate = fp / (fp + tn);
        }

        double gMean = 0;
        if ((tp + fn) != 0 && (tn + fp) != 0) {
            gMean = Math.sqrt((tp / (tp + fn)) * (tn / (tn + fp)));
        }

        double fMeasure = 0;
        if (precision != 0 && recall != 0) {
            fMeasure = 2 / (1 / precision + 1 / recall);
        }

        double sensitivity = 0;
        double specificity = 0;
        if (tp + fp > 0) {
            sensitivity = tp / (tp + fp);
        }
        if (tn + fn > 0) {
            specificity = tn / (tn + fn);
        }


        double fitness = -1;
        if (Constants.fitness_type == 0) {
            fitness = fitnessAlpha * accuracy + fitnessBeta * generality;
        } else if (Constants.fitness_type == 1) {
            fitness = fitnessAlpha * (specificity * sensitivity) + fitnessBeta * generality;
        }


        this.setAccuracy(accuracy);
        this.setfMeasure(fMeasure);
        this.setRecall(recall);
        this.setgMean(gMean);
        this.setPrecison(precision);
        this.setTpRate(tpRate);
        this.setFpRate(fpRate);
        this.setSensitivity(sensitivity);
        this.setSpecificity(specificity);
        this.setFitness(fitness);

    }

    @Override
    public String toString() {
        String str = "";
//        System.out.format(str,"%f %f %f %f %f",accuracy,precison,recall,gMean,fMeasure);
//        return str;
        return accuracy + " " + precison + " " + recall + " " + gMean + " " + fMeasure;                
    }
//    public String toString() {
//        return "\n Property{" + "accuracy=" + accuracy + ",\n"
//                + " generality=" + generality + ",\n"
//                + " recall=" + recall + ",\n"
//                + " precison=" + precison + ",\n"
//                + " fMeasure=" + fMeasure + ",\n"
//                + " gMean=" + gMean + ",\n"
//                + " tpRate=" + tpRate + ",\n"
//                + " fpRate=" + fpRate + ",\n"
//                + " sensitivity=" + sensitivity + ",\n"
//                + " specificity=" + specificity + ",\n"
//                + " fitness=" + fitness + ",\n"
//                + " fitnessAlpha=" + fitnessAlpha + ",\n"
//                + " fitnessBeta=" + fitnessBeta + '}'+"\n";
//    }
}
