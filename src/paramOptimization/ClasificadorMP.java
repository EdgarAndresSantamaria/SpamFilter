package paramOptimization;

import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.lazy.IBk;
import weka.core.DistanceFunction;
import weka.core.EuclideanDistance;
import weka.core.ManhattanDistance;
import weka.core.ChebyshevDistance;
import weka.core.SelectedTag;
import weka.core.Tag;
import weka.core.neighboursearch.LinearNNSearch;


public class ClasificadorMP {
	/**
	 * atributos
	 */
	private static ClasificadorMP miClasificador;

	/**
	 * devuelve la instancia unica del clasificadorKNN
	 * @return
	 */
	public static ClasificadorMP getMiClasificador() {
		if(miClasificador==null) miClasificador = new ClasificadorMP();
		return miClasificador;
	}
	
	/**
	 * Se inicializa el MultilayerPerceptron con las opciones
	 * pasadas como parametro
	 * @param learningRate
	 * @param hiddenLayers
	 * @return El multilayer perceptron configurado
	 * @throws Exception
	 */
	public MultilayerPerceptron inicializar(double learningRate, String hiddenLayers) {
		MultilayerPerceptron clasificador = new MultilayerPerceptron();
		clasificador.setHiddenLayers(hiddenLayers);
		clasificador.setLearningRate(learningRate);
		clasificador.setTrainingTime(800);

		return clasificador;

	}



}
