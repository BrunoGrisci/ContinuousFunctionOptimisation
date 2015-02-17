package continuousFunctionOptimisation;

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
	static int numberOfGenerations = 5000;
	
	//
	

	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		// TODO Auto-generated method stub
		individual = new double[numberOfGenes];
		generationScore = new double[numberOfGenerations];
		
		evolutionStrategy();
		
		for (double gene : individual) {
			System.out.println(gene);
		}
		System.out.println(sphere(individual));
		
	}
	
	public static void evolutionStrategy() throws FileNotFoundException, UnsupportedEncodingException {
		Random r = new Random();
		PrintWriter writer = new PrintWriter("output.txt", "UTF-8");
		individual = createRandomIndividual(individual);
		int goodMutations = 0;
		int badMutations = 0;
		int n = 0; //LOOK IT
		double stepSize = 1.0 + (100.0 - 1.0) * r.nextDouble();
		for (int generation = 0; generation < numberOfGenerations; generation++) {
			offspring = new double[numberOfGenes];
			//offspring = Mutation.uniformMutation(individual.clone());
			//offspring = Mutation.nonUniformMutation(individual.clone(), generation, 0.05);
			//offspring = Mutation.nonUniformMutation(individual.clone(), generation, 1.0);
			//offspring = Mutation.nonUniformMutation(individual.clone(), generation, 10.0);
			//offspring = Mutation.nonUniformMutation(individual.clone(), generation, 20.0);
			//offspring = Mutation.gaussianMutation(individual.clone(), 0.05);
			//offspring = Mutation.gaussianMutation(individual.clone(), 0.5);
			//offspring = Mutation.gaussianMutation(individual.clone(), 1.0);
			//offspring = Mutation.gaussianMutation(individual.clone(), 10.0);
			offspring = Mutation.gaussianMutation(individual.clone(), stepSize);
			if (sphere(offspring) < sphere(individual)) {
				individual = offspring;
				goodMutations = goodMutations + 1;
			}
			else {
				badMutations = badMutations + 1;
			}
			if (badMutations + goodMutations == n) {
				if (goodMutations/n > 1/5) {
					stepSize = stepSize * 2;
				}
				if (goodMutations/n < 1/5) {
					stepSize = stepSize/2;
				}
				badMutations = 0;
				goodMutations = 0;
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
