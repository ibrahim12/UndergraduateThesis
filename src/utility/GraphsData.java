package utility;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import org.jfree.data.category.CategoryDataset;



public class GraphsData{
	public String dataPath;	
	public BufferedWriter writerTruePopPhase1;
	public BufferedWriter writerFalsePopPhase1;
	
	public BufferedWriter writerTruePopPhase2;
	public BufferedWriter writerFalsePopPhase2;
	
	GraphsData(String path){
		this.dataPath = path;		
		try {
			
			writerTruePopPhase1 = new BufferedWriter(new FileWriter(path+"/phase1/TruePop.txt",false));
			writerTruePopPhase1 = new BufferedWriter(new FileWriter(path+"/phase1/TruePop.txt",true));
			
			writerFalsePopPhase1 = new BufferedWriter(new FileWriter(path+"/phase1/FalsePop.txt",false));
			writerFalsePopPhase1 = new BufferedWriter(new FileWriter(path+"/phase1/FalsePop.txt",true));
			
			writerTruePopPhase2 = new BufferedWriter(new FileWriter(path+"/phase2/TruePop.txt",false));
			writerTruePopPhase2 = new BufferedWriter(new FileWriter(path+"/phase2/TruePop.txt",true));
			
			writerFalsePopPhase2 = new BufferedWriter(new FileWriter(path+"/phase2/FalsePop.txt",false));
			writerFalsePopPhase2 = new BufferedWriter(new FileWriter(path+"/phase2/FalsePop.txt",true));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	public void writePhase1(String s,int classIndex){
		try {
			if(classIndex == 1){
				writerTruePopPhase1.append(s + "\n");
				writerTruePopPhase1.flush();
			}else{
				writerFalsePopPhase1.append(s + "\n");
				writerFalsePopPhase1.flush();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void writePhase2(String s,int classIndex){
		try {
			if(classIndex == 1){
				writerTruePopPhase2.append(s + "\n");
				writerTruePopPhase2.flush();
			}else{
				writerFalsePopPhase2.append(s + "\n");
				writerFalsePopPhase2.flush();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void drawGraph(){
		
	}
}