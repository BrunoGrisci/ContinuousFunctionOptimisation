/*Bruno Iochins Grisci
Student ID: 149778
University of Birmingham
Nature Inspired Optimisation – 06-26949
Nature-Inspired Optimisation (Extended) – 06-26948
Course Work I (10%): Programming Assignment
Continuous Function Optimisation*/

package continuousFunctionOptimisation;

import java.util.Random;

public class Mutation {
	
	//This class contains the implementation of the methods for the mutation operators
	
	public static double[] uniformMutation (double[] individual) {
		Random r = new Random();
		int selectedGene =  r.nextInt(Benchmark.numberOfGenes);
		double newGene = Benchmark.rangeMin + (Benchmark.rangeMax - Benchmark.rangeMin) * r.nextDouble();
		individual[selectedGene] = newGene;
		return individual;
	}
	
	public static double[] nonUniformMutation (double[] individual, int currentGeneration, double b) {
		Random r = new Random();
		int selectedGene =  r.nextInt(Benchmark.numberOfGenes);
		double tau = 1.0 * r.nextDouble();
		
		if (tau >= 0.5) {
			individual[selectedGene] = individual[selectedGene] + delta(currentGeneration, 100.0 - individual[selectedGene], b);
			
		}
		else {
			individual[selectedGene] = individual[selectedGene] - delta(currentGeneration, 100.0 + individual[selectedGene], b);
		}
		
		return individual;
	}
	
	private static double delta (double currentGeneration, double y, double b) {
		//Method used by non uniform mutation
		Random ran = new Random();
		double r = 1.0 * ran.nextDouble();
		
		return y * Math.pow(1.0 - Math.pow(r, 1.0 - (currentGeneration/Benchmark.numberOfGenerations)), b);
	}
	
	public static double[] gaussianMutation (double[] individual, double stepSize) {
		Random r = new Random();
		for (int gene = 0; gene < Benchmark.numberOfGenes; gene++) {
			double m = r.nextGaussian();
			individual[gene] = individual[gene] + (stepSize * m);
		}
		return individual;
	}

}
