package utility;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.experimental.chart.plot.CombinedCategoryPlot;
import org.jfree.ui.Layer;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.TextAnchor;

public class GraphDrawing {

		
	public static JFreeChart drawFromSeries(String path,ArrayList<XYSeries> series,String title){
		
		XYSeriesCollection dataset = new XYSeriesCollection();
		for(int i=0;i<series.size();i++){
			dataset.addSeries(series.get(i));
			dataset.setIntervalWidth(4);
		}
		
		final JFreeChart chart1 = ChartFactory.createXYLineChart
				(title, "Genrations", "Distance",
				dataset, PlotOrientation.VERTICAL, true, true, false);
		
	
		final JFreeChart chart = ChartFactory.createXYBarChart(title,
	            "Genrations", 
	            false,
	            "Distance", 
	            dataset,
	            PlotOrientation.VERTICAL,
	            true,
	            true,
	            false
	        );
		
		
		try {
			ChartUtilities.saveChartAsJPEG(new File(path), chart, 1024,400);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
//			String temp = path.substring(0,path.indexOf("."));
//			path = temp + "_1.jpeg";
			String newPath = Util.insert(".",path,"_1");
			ChartUtilities.saveChartAsJPEG(new File(newPath), chart1, 1024,400);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return chart;
		
		
	}
	public static JFreeChart DrawGenVsError(String path,ArrayList<XYSeries> seriesPhase1,
			ArrayList<XYSeries> seriesPhase2,
			int classIndex){
		
		String title = "Generation Vs Error ( ";
		String chartPop = "";
		if(classIndex == 1){
			chartPop =  "TruePopChart.jpeg";
			title += "True Population )";
		}
		else{
			chartPop =  "FalsePopChart.jpeg";
			title += "False Population )";
		}
		
		drawFromSeries(path+"/phase1/"+chartPop,seriesPhase1,title);
		return drawFromSeries(path+"/phase2/"+chartPop,seriesPhase2,title);

	}
		
}
