/*Bruno Iochins Grisci
Student ID: 149778
University of Birmingham
Nature Inspired Optimisation – 06-26949
Nature-Inspired Optimisation (Extended) – 06-26948
Course Work I (10%): Programming Assignment
Continuous Function Optimisation*/

package continuousFunctionOptimisation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Random;

import jxl.*; 
import jxl.biff.formula.FormulaException;
import jxl.read.biff.BiffException;
import jxl.write.*; 
import jxl.write.Number;
import jxl.write.biff.RowsExceededException;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class Benchmark {
	
	static double[] individual;
	static double[] offspring;
	static int numberOfGenes = 10;
	static double rangeMin = -100.0;
	static double rangeMax = 100.0;
	static double[] generationScore;
	static final int numberOfGenerations = 5000;
	static final int numberOfExperiments = 30;
	static final int numberOfFunctions = 10;
	
	static final int UNIFORM_MUTATION = 0;
	static final int NON_UNIFORM_MUTATION_0_05 = 1;
	static final int NON_UNIFORM_MUTATION_1 = 2;
	static final int NON_UNIFORM_MUTATION_10 = 3;
	static final int NON_UNIFORM_MUTATION_20 = 4;
	static final int GAUSSIAN_MUTATION_0_05 = 5;
	static final int GAUSSIAN_MUTATION_0_5 = 6;
	static final int GAUSSIAN_MUTATION_1 = 7;
	static final int GAUSSIAN_MUTATION_10 = 8;
	static final int GAUSSIAN__MUTATION_1_5_RULE = 9;
	
	static WritableWorkbook table;
	static WritableSheet sheetMainResults;
	static ArrayList<WritableSheet> sheets;

	public static void main(String[] args) throws RowsExceededException, WriteException, BiffException, IOException {
		
		try {
			
			//Initialize the file with the sheets
			table = Workbook.createWorkbook(new File("output.xls"));
			sheetMainResults = table.createSheet("Average", 0);
			
			ArrayList<Label> tableLabels = new ArrayList<Label>();
			tableLabels.add(new Label(0, 0, "Average fitness value and its standard deviation (over the 30 runs) after 5,000 iterations for each of the considered settings."));
			tableLabels.add(new Label(1, 1, "Uniform Mutation"));
			tableLabels.add(new Label(2, 1, "Non Uniform Mutation b = 0.05"));
			tableLabels.add(new Label(3, 1, "Non Uniform Mutation b = 1.0"));
			tableLabels.add(new Label(4, 1, "Non Uniform Mutation b = 10.0"));
			tableLabels.add(new Label(5, 1, "Non Uniform Mutation b = 20.0"));
			tableLabels.add(new Label(6, 1, "Gaussian Mutation σ = 0.05"));
			tableLabels.add(new Label(7, 1, "Gaussian Mutation σ = 0.5"));
			tableLabels.add(new Label(8, 1, "Gaussian Mutation σ = 1.0"));
			tableLabels.add(new Label(9, 1, "Gaussian Mutation σ = 10.0"));
			tableLabels.add(new Label(10, 1, "Gaussian Mutation with 1/5-rule and initial σ uniformly distributed over [1, 100]"));
			tableLabels.add(new Label(0, 32, "Average fitness value"));
			tableLabels.add(new Label(0, 33, "Standard deviation"));
			
			for (Label label : tableLabels) {
				sheetMainResults.addCell(label);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Add labels
		sheets = new ArrayList<WritableSheet>();
		sheets.add(table.createSheet("Uniform Mutation", 1));
		sheets.add(table.createSheet("Non Uniform Mutation b = 0.05", 2));
		sheets.add(table.createSheet("Non Uniform Mutation b = 1.0", 3));
		sheets.add(table.createSheet("Non Uniform Mutation b = 10.0", 4));
		sheets.add(table.createSheet("Non Uniform Mutation b = 20.0", 5));
		sheets.add(table.createSheet("Gaussian Mutation σ = 0.05", 6));
		sheets.add(table.createSheet("Gaussian Mutation σ = 0.5", 7));
		sheets.add(table.createSheet("Gaussian Mutation σ = 1.0", 8));
		sheets.add(table.createSheet("Gaussian Mutation σ = 10.0", 9));
		sheets.add(table.createSheet("Gaussian Mutation 1 5 rule", 10));
		
		for (WritableSheet sheet : sheets) {
			Label labelAverage = new Label(0, 0, "Average over 30 runs");
			sheet.addCell(labelAverage);
		}
		
		String dirName = "Output/";
		new File(dirName).mkdirs();
		
		//For loop for the different mutation methods
		for (int i = 0; i < numberOfFunctions; i++) {
			//For loop for the number of trails
			for (int j = 0; j < numberOfExperiments; j++) {
				individual = new double[numberOfGenes];
				generationScore = new double[numberOfGenerations];
		
				String fileName = dirName + Integer.toString(i) + "_" + Integer.toString(j) + ".txt";
				//Here the optimisation takes place
				evolutionStrategy(fileName, i, j);
				
				Number bestScore = new Number(i+1, j+2, sphere(individual)); 
				sheetMainResults.addCell(bestScore); 

			}
		}
		
		//Write average in sheet
		for (WritableSheet sheet : sheets) {
			for (int i = 0; i < numberOfGenerations; i++) {
				String formula = "AVERAGE(B" + Integer.toString(i+2) + ":AE" + Integer.toString(i+2) + ")";
				Formula averageCell = new Formula(0, i+1, formula);
				sheet.addCell(averageCell);
			}
		}
		
		//Add the average and standard deviation to first sheet
		String[] letters = {"B","C","D","E","F","G","H","I","J","K"};
		for (int i = 0; i < numberOfFunctions; i++) {
			String avgFormula = "AVERAGE(" + letters[i] + "3:" + letters[i] + "32)";
			String sdFormula = "STDEV(" + letters[i] + "3:" + letters[i] + "32)";
			Formula averageCell = new Formula(i+1, 32, avgFormula);
			Formula sdCell = new Formula(i+1, 33, sdFormula);
			sheetMainResults.addCell(averageCell);
			sheetMainResults.addCell(sdCell);
			
		}
		
		table.write();
		try {
			table.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("The end");
	}
	
	public static void evolutionStrategy(String fileName, int function, int currentRun) throws FileNotFoundException, UnsupportedEncodingException, RowsExceededException, WriteException {
		//Optimisation function
		Random r = new Random();
		PrintWriter writer = new PrintWriter(fileName, "UTF-8");
		individual = createRandomIndividual(individual);
		double goodMutations = 0.0;
		double badMutations = 0.0;
		double n = 10.0;
		double stepSize = 1.0 + (100.0 - 1.0) * r.nextDouble();
		
		//For loop for the number of iterations
		for (int generation = 0; generation < numberOfGenerations; generation++) {
			offspring = new double[numberOfGenes];
			
			switch(function) {
				case UNIFORM_MUTATION:
					offspring = Mutation.uniformMutation(individual.clone());
					break;
				case NON_UNIFORM_MUTATION_0_05:
					offspring = Mutation.nonUniformMutation(individual.clone(), generation, 0.05);
					break;
				case NON_UNIFORM_MUTATION_1:
					offspring = Mutation.nonUniformMutation(individual.clone(), generation, 1.0);
					break;
				case NON_UNIFORM_MUTATION_10:
					offspring = Mutation.nonUniformMutation(individual.clone(), generation, 10.0);
					break;
				case NON_UNIFORM_MUTATION_20:
					offspring = Mutation.nonUniformMutation(individual.clone(), generation, 20.0);
					break;
				case GAUSSIAN_MUTATION_0_05:
					offspring = Mutation.gaussianMutation(individual.clone(), 0.05);
					break;
				case GAUSSIAN_MUTATION_0_5:
					offspring = Mutation.gaussianMutation(individual.clone(), 0.5);
					break;
				case GAUSSIAN_MUTATION_1:
					offspring = Mutation.gaussianMutation(individual.clone(), 1.0);
					break;
				case GAUSSIAN_MUTATION_10:
					offspring = Mutation.gaussianMutation(individual.clone(), 10.0);
					break;
				case GAUSSIAN__MUTATION_1_5_RULE:
					offspring = Mutation.gaussianMutation(individual.clone(), stepSize);
					break;
				default:
					System.out.println("CODE NOT VALID");
					break;
			
			}
			//This code is used by the gaussian mutation with 1/5-rule
			if (sphere(offspring) < sphere(individual)) {
				individual = offspring;
				goodMutations = goodMutations + 1.0;
			}
			else {
				badMutations = badMutations + 1.0;
			}
			if (badMutations + goodMutations == n) {
				if (goodMutations/n > (1.0/5.0)) {
					stepSize = stepSize * 2.0;
				}
				if (goodMutations/n < (1.0/5.0)) {
					stepSize = stepSize/2.0;
				}
				badMutations = 0.0;
				goodMutations = 0.0;
			}
			
			Number currentScore = new Number(currentRun+1, generation+1, sphere(individual)); 
			sheets.get(function).addCell(currentScore); 
			writer.println(sphere(individual));
		}
		writer.close();
	}

	public static double sphere(double[] individual) {
		//Function that returns the fitness value
		double score = 0.0;
		for (double gene : individual) {
			score = score + Math.pow(gene, 2);
		}
		return score;	
	}
	
	public static double[] createRandomIndividual(double[] individual) {
		//It creates a new individual
		for (int gene = 0; gene < numberOfGenes; gene++) {
			Random r = new Random();
			individual[gene] = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
		}
		return individual;
	}
	
}