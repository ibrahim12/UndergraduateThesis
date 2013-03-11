/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Logic;

import IR.Constants;
import java.util.ArrayList;

/**
 *
 * @author ibrahim
 */
public class Gene  {
    
    
    ArrayList<Integer> allelies = new ArrayList<Integer>();
    
    public Gene(ArrayList<Integer> allelies){
        this.allelies = allelies;        
    }
        
    public int size(){
        return this.allelies.size();
    }
    
    public boolean isEqual(Gene bGene) throws Exception{
        Gene aGene = this;
        
        if(aGene.size() != bGene.size())
            throw new Exception("Gene Size Mismatch");
        
        for (int alleleIndex = 0; alleleIndex < aGene.size(); alleleIndex++) 
        {
            int aGeneVal = aGene.allelies.get(alleleIndex);
            int bGeneVal = bGene.allelies.get(alleleIndex);
            
            if( aGeneVal == Constants.attr_dont_care_value || 
                    bGeneVal == Constants.attr_dont_care_value ){
                continue;
            }
            
            if( aGeneVal != bGeneVal ){            
                return false;
            }
        }
        return true;
    }
    
    public int precentageEqual(Gene bGene) throws Exception{
        Gene aGene = this;
        
        if(aGene.size() != bGene.size())
            throw new Exception("Gene Size Mismatch");
        int alleleMatchCount = 0;
        for (int alleleIndex = 0; alleleIndex < aGene.size(); alleleIndex++) 
        {
            int aGeneVal = aGene.allelies.get(alleleIndex);
            int bGeneVal = bGene.allelies.get(alleleIndex);
            
            if( aGeneVal == Constants.attr_dont_care_value || 
                    bGeneVal == Constants.attr_dont_care_value ){
//                alleleMatchCount++;
                continue;
            }
            
            if( aGeneVal == bGeneVal ){            
                alleleMatchCount++;
            }
        }
        return alleleMatchCount;
    }
    
    public int dontCareAlleleCount(){
        int count = 0;
        for(int i=0;i<allelies.size();i++){
            if(allelies.get(i) == Constants.attr_dont_care_value )
                count++;
        }
        return count;
    }
    
    public double getGenerality(){
        double generality = 0;
        for(int i=0;i<Constants.no_of_attr;i++){
            if( this.allelies.get(i) == Constants.attr_dont_care_value ){
                generality++;
            }
        }
        return generality/Constants.no_of_attr;
    }
    
    @Override
	public Gene clone(){
        ArrayList<Integer> newAllele = new ArrayList<Integer>();
        for(int i=0;i<this.allelies.size();i++){
            newAllele.add(allelies.get(i));
        }
        return new Gene(newAllele);
    }
    
    @Override
    public String toString() {
        return "allelies=" + allelies + '\n';
    }

}
