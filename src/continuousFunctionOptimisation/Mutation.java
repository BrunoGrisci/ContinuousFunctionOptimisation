package continuousFunctionOptimisation;

import java.util.Random;

public class Mutation {
	
	public static double[] uniformMutation (double[] individual) {
		Random r = new Random();
		int selectedGene =  r.nextInt(Benchmark.numberOfGenes);
		double newGene = Benchmark.rangeMin + (Benchmark.rangeMax - Benchmark.rangeMin) * r.nextDouble();
		System.out.println(selectedGene);
		System.out.println(newGene);
		individual[selectedGene] = newGene;
		return individual;
	}
	
	public static double[] nonUniformMutation (double[] individual, int currentGeneration, double b) {
		Random r = new Random();
		int selectedGene =  r.nextInt(Benchmark.numberOfGenes);
		double gama = 1.0 * r.nextDouble();
		
		if (gama >= 0.5) {
			individual[selectedGene] = individual[selectedGene] + delta(currentGeneration, 100.0 - individual[selectedGene], b);
			
		}
		else {
			individual[selectedGene] = individual[selectedGene] - delta(currentGeneration, 100.0 + individual[selectedGene], b);
		}
		
		return individual;
	}
	
	private static double delta (int currentGeneration, double y, double b) {
		Random ran = new Random();
		double r = 1.0 * ran.nextDouble();
		
		return y * Math.pow(1 - Math.pow(r, 1 - currentGeneration/Benchmark.numberOfGenerations), b);
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
