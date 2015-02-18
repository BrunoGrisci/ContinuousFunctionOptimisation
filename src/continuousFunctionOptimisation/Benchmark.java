package continuousFunctionOptimisation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Date; 

import jxl.*; 
import jxl.write.*; 
import jxl.write.Number;
import jxl.write.biff.RowsExceededException;
import jxl.read.biff.BiffException;

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

	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException, RowsExceededException, WriteException {
		
		try {
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
		
		
		String dirName = "Output/";
		new File(dirName).mkdirs();
		
		for (int i = 0; i < numberOfFunctions; i++) {
			for (int j = 0; j < numberOfExperiments; j++) {
				individual = new double[numberOfGenes];
				generationScore = new double[numberOfGenerations];
		
				String fileName = dirName + Integer.toString(i) + "_" + Integer.toString(j) + ".txt";
				evolutionStrategy(fileName, i);
		
				//for (double gene : individual) {
					//System.out.println(gene);
				//}
				System.out.println(sphere(individual));
				
				Number bestScore = new Number(i+1, j+2, sphere(individual)); 
				sheetMainResults.addCell(bestScore); 

			}
			System.out.println("Fim");
		}
		
		try {
			table.write();
			table.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void evolutionStrategy(String fileName, int function) throws FileNotFoundException, UnsupportedEncodingException {
		Random r = new Random();
		PrintWriter writer = new PrintWriter(fileName, "UTF-8");
		individual = createRandomIndividual(individual);
		double goodMutations = 0.0;
		double badMutations = 0.0;
		double n = 10.0;
		double stepSize = 1.0 + (100.0 - 1.0) * r.nextDouble();
		
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
			if (sphere(offspring) < sphere(individual)) {
				individual = offspring;
				goodMutations = goodMutations + 1;
			}
			else {
				badMutations = badMutations + 1;
			}
			if (badMutations + goodMutations == n) {
				if (goodMutations/n > (1/5)) {
					stepSize = stepSize * 2.0;
					//System.out.println(stepSize);
				}
				if (goodMutations/n < (1/5)) {
					stepSize = stepSize/2.0;
					//System.out.println(stepSize);
				}
				badMutations = 0.0;
				goodMutations = 0.0;
			}
			
			
			writer.println(sphere(individual));
		}
		writer.close();
	}

	public static double sphere(double[] individual) {
		double score = 0.0;
		for (double gene : individual) {
			score = score + Math.pow(gene, 2);
		}
		return score;	
	}
	
	public static double[] createRandomIndividual(double[] individual) {
		for (int gene = 0; gene < numberOfGenes; gene++) {
			Random r = new Random();
			individual[gene] = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
		}
		return individual;
	}
}
