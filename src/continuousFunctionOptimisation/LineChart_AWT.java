package continuousFunctionOptimisation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import jxl.Cell;
import jxl.NumberCell;
import jxl.NumberFormulaCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class LineChart_AWT extends ApplicationFrame {
   public LineChart_AWT( String applicationTitle, 
		   				 String chartTitle,
		   				 String xName,
		   				 String yName,
		   				 ArrayList<Double> values, ArrayList<String> tags, ArrayList<String> xLabels) {
      super(applicationTitle);
      JFreeChart lineChart = ChartFactory.createLineChart(
         chartTitle,
         xName,yName,
         createDataset(values, tags, xLabels),
         PlotOrientation.VERTICAL,
         true,true,false);
         
      ChartPanel chartPanel = new ChartPanel( lineChart );
      //chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );
      chartPanel.setPreferredSize( new java.awt.Dimension( 1000 , 600 ) );
      setContentPane( chartPanel );
   }

   private DefaultCategoryDataset createDataset(ArrayList<Double> values, ArrayList<String> tags, ArrayList<String> xLabels) {
      DefaultCategoryDataset dataset = new DefaultCategoryDataset( );
      for (int i = 0; i < values.size(); i++) {
    	  dataset.addValue(values.get(i), tags.get(i), xLabels.get(i));
      }
      return dataset;
   }
   
   public static void main(String[] args) throws BiffException, IOException {
		Workbook table = Workbook.getWorkbook(new File("output.xls"));
		
		Sheet sheetUM = table.getSheet(1);
		Sheet sheetNUM005 = table.getSheet(2);
		Sheet sheetNUM1 = table.getSheet(3);
		Sheet sheetNUM10 = table.getSheet(4);
		Sheet sheetNUM20 = table.getSheet(5);
		Sheet sheetGM005 = table.getSheet(6);
		Sheet sheetGM05 = table.getSheet(7);
		Sheet sheetGM1 = table.getSheet(8);
		Sheet sheetGM10 = table.getSheet(9);
		Sheet sheetGMRULE = table.getSheet(10);
		
		Cell a = sheetUM.getCell(0,0);
		Cell b = sheetUM.getCell(0,1);
		NumberCell b0 = (NumberCell) b;
		Cell c = sheetUM.getCell(1,0);
		Cell d0 = sheetUM.getCell(1,1);
		System.out.println(a.getContents());
		System.out.println(b0.getContents());
		System.out.println(c.getContents());
		System.out.println(d0.getContents());
		
		ArrayList<Double> valuesNUM = new ArrayList<Double>();
		ArrayList<String> tagsNUM = new ArrayList<String>();
		ArrayList<String> xLabelsNUM = new ArrayList<String>();
		
		for (int i = 0; i < Benchmark.numberOfGenerations; i++) {
			Cell c005 = sheetNUM005.getCell(0, i+1);
			NumberFormulaCell nc005 = (NumberFormulaCell) c005;
			valuesNUM.add((Math.log(nc005.getValue())));
			tagsNUM.add("b = 0.05");
			xLabelsNUM.add(Integer.toString(i+1));
		}
		for (int i = 0; i < Benchmark.numberOfGenerations; i++) {
			Cell c1 = sheetNUM1.getCell(0, i+1);
			NumberFormulaCell nc1 = (NumberFormulaCell) c1;
			valuesNUM.add((Math.log(nc1.getValue())));
			tagsNUM.add("b = 1.0");
			xLabelsNUM.add(Integer.toString(i+1));
		}
		for (int i = 0; i < Benchmark.numberOfGenerations; i++) {
			Cell c10 = sheetNUM10.getCell(0, i+1);
			NumberFormulaCell nc10 = (NumberFormulaCell) c10;
			valuesNUM.add((Math.log(nc10.getValue())));
			tagsNUM.add("b = 10.0");
			xLabelsNUM.add(Integer.toString(i+1));
		}
		for (int i = 0; i < Benchmark.numberOfGenerations; i++) {
			Cell c20 = sheetNUM20.getCell(0, i+1);
			NumberFormulaCell nc20 = (NumberFormulaCell) c20;
			valuesNUM.add((Math.log(nc20.getValue())));
			tagsNUM.add("b = 20.0");
			xLabelsNUM.add(Integer.toString(i+1));
		}
		
		ArrayList<Double> valuesGM = new ArrayList<Double>();
		ArrayList<String> tagsGM = new ArrayList<String>();
		ArrayList<String> xLabelsGM = new ArrayList<String>();
		
		for (int i = 0; i < Benchmark.numberOfGenerations; i++) {
			Cell c005 = sheetGM005.getCell(0, i+1);
			NumberFormulaCell nc005 = (NumberFormulaCell) c005;
			valuesGM.add((Math.log(nc005.getValue())));
			tagsGM.add("σ = 0.05");
			xLabelsGM.add(Integer.toString(i+1));
		}
		for (int i = 0; i < Benchmark.numberOfGenerations; i++) {
			Cell c05 = sheetGM05.getCell(0, i+1);
			NumberFormulaCell nc05 = (NumberFormulaCell) c05;
			valuesGM.add((Math.log(nc05.getValue())));
			tagsGM.add("σ = 0.5");
			xLabelsGM.add(Integer.toString(i+1));
		}
		for (int i = 0; i < Benchmark.numberOfGenerations; i++) {
			Cell c1 = sheetGM1.getCell(0, i+1);
			NumberFormulaCell nc1 = (NumberFormulaCell) c1;
			valuesGM.add((Math.log(nc1.getValue())));
			tagsGM.add("σ = 1.0");
			xLabelsGM.add(Integer.toString(i+1));
		}
		for (int i = 0; i < Benchmark.numberOfGenerations; i++) {
			Cell c10 = sheetGM10.getCell(0, i+1);
			NumberFormulaCell nc10 = (NumberFormulaCell) c10;
			valuesGM.add((Math.log(nc10.getValue())));
			tagsGM.add("σ = 10.0");
			xLabelsGM.add(Integer.toString(i+1));
		}
		
		ArrayList<Double> valuesALL = new ArrayList<Double>();
		ArrayList<String> tagsALL = new ArrayList<String>();
		ArrayList<String> xLabelsALL = new ArrayList<String>();
		
		for (int i = 0; i < Benchmark.numberOfGenerations; i++) {
			Cell cUM = sheetUM.getCell(0, i+1);
			NumberFormulaCell ncUM = (NumberFormulaCell) cUM;
			valuesALL.add((Math.log(ncUM.getValue())));
			tagsALL.add("Uniform Mutation");
			xLabelsALL.add(Integer.toString(i+1));
		}
		for (int i = 0; i < Benchmark.numberOfGenerations; i++) {
			Cell c05 = sheetGM05.getCell(0, i+1);
			NumberFormulaCell nc05 = (NumberFormulaCell) c05;
			valuesALL.add((Math.log(nc05.getValue())));
			tagsALL.add("Gaussian Mutation σ = 0.5");
			xLabelsALL.add(Integer.toString(i+1));
		}
		for (int i = 0; i < Benchmark.numberOfGenerations; i++) {
			Cell cR = sheetGMRULE.getCell(0, i+1);
			NumberFormulaCell ncR = (NumberFormulaCell) cR;
			valuesALL.add((Math.log(ncR.getValue())));
			tagsALL.add("Gaussian Mutation with 1/5-rule");
			xLabelsALL.add(Integer.toString(i+1));
		}
		for (int i = 0; i < Benchmark.numberOfGenerations; i++) {
			Cell c10 = sheetNUM10.getCell(0, i+1);
			NumberFormulaCell nc10 = (NumberFormulaCell) c10;
			valuesALL.add((Math.log(nc10.getValue())));
			tagsALL.add("Non Uniform Mutation b = 10.0");
			xLabelsALL.add(Integer.toString(i+1));
		}
		
		LineChart_AWT chartNUM = new LineChart_AWT("Comparing different parameters for non-uniform mutation",
												   "Comparing different parameters for non-uniform mutation",
												   "Iteration number (1 - 5000)",
												   "Average function value in logscale over the 30 trails",
												   valuesNUM,
												   tagsNUM,
												   xLabelsNUM);

	    chartNUM.pack();
	    RefineryUtilities.centerFrameOnScreen(chartNUM);
	    chartNUM.setVisible(true);
	    
		LineChart_AWT chartGM = new LineChart_AWT("Comparing different parameters for gaussian mutation",
												  "Comparing different parameters for gaussian mutation",
												  "Iteration number (1 - 5000)",
												  "Average function value in logscale over the 30 trails",
												  valuesGM,
												  tagsGM,
												  xLabelsGM);

		chartGM.pack();
		RefineryUtilities.centerFrameOnScreen(chartGM);
		chartGM.setVisible(true);
		
		LineChart_AWT chartALL = new LineChart_AWT("Comparing different mutation methods",
				  								   "Comparing different mutation methods",
				  								   "Iteration number (1 - 5000)",
				  								   "Average function value in logscale over the 30 trails",
				  								   valuesALL,
				  								   tagsALL,
				  								   xLabelsALL);

		chartALL.pack();
		RefineryUtilities.centerFrameOnScreen(chartALL);
		chartALL.setVisible(true);
	    
	    table.close();	   
   }

}
