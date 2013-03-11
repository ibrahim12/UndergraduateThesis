/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import java.util.ArrayList;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.ui.Drawable;

import IR.Config;
import IR.Constants;
import Logic.Main;

/**
 * 
 * @author Ibrahim
 */



public class Stats {

	public GraphsData GenVsError;
	public ArrayList<XYSeries> GenVsErrorDatasPhase1;
	public ArrayList<XYSeries> GenVsErrorDatasPhase2;
	public Config config;
	
	public static final int GEN_VS_ERROR_BEST = 0;
	public static final int GEN_VS_ERROR_AVG = 1;
	public static final int GEN_VS_ERROR_WROST = 2;
	
	public Stats(Config config){
		this.config = config;				
		if(Main.datasetName != ""){
			GenVsErrorDatasPhase1 = new ArrayList<XYSeries>();			
			GenVsErrorDatasPhase1.add(new XYSeries("BEST"));
			GenVsErrorDatasPhase1.add(new XYSeries("AVG"));
			GenVsErrorDatasPhase1.add(new XYSeries("WROST"));
			
			GenVsErrorDatasPhase2 = new ArrayList<XYSeries>();			
			GenVsErrorDatasPhase2.add(new XYSeries("BEST"));
			GenVsErrorDatasPhase2.add(new XYSeries("AVG"));
			GenVsErrorDatasPhase2.add(new XYSeries("WROST"));
			

			String path = config.get("graphsGenVsError") + "/" + Main.datasetName + "/";
			GenVsError = new GraphsData(path);				
		}
	}
	
	public void addGenVsError(int gen,int best,int avg,int worst,int classIndex,int phaseNo){
				
		if(phaseNo == 1){
			GenVsErrorDatasPhase1.get(GEN_VS_ERROR_BEST).add(gen,best);
			GenVsErrorDatasPhase1.get(GEN_VS_ERROR_AVG).add(gen+4,avg);
			GenVsErrorDatasPhase1.get(GEN_VS_ERROR_WROST).add(gen-4,worst);	
		}
		else{
			GenVsErrorDatasPhase2.get(GEN_VS_ERROR_BEST).add(gen,best);
			GenVsErrorDatasPhase2.get(GEN_VS_ERROR_AVG).add(gen+4,avg);
			GenVsErrorDatasPhase2.get(GEN_VS_ERROR_WROST).add(gen-4,worst);
		}

//		String stats = "Gen Best Avg Wrost " + gen + " " + best + " " + avg + " " + worst;
		String stats = gen + "\t" + best + "\t" + avg + "\t" + worst;
		if(phaseNo == 1)
			GenVsError.writePhase1(stats,classIndex);
		else
			GenVsError.writePhase2(stats,classIndex);
	}

	public void generatePlots(int classIndex){
		String path = config.get("graphsGenVsError") + "/" + Main.datasetName + "/";		
		GraphDrawing.DrawGenVsError( path, GenVsErrorDatasPhase1,GenVsErrorDatasPhase2,classIndex);
	}
	
}
