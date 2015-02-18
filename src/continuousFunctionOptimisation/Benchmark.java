package continuousFunctionOptimisation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Random;

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
	
	

	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		// TODO Auto-generated method stub
		
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
			}
		
			System.out.println("Fim");
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
