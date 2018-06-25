package paramOptimization;

import java.util.ArrayList;

import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Instances;

public class Optimizador {

	private static Optimizador miOptimizador;


	public static Optimizador getMiOptimizador() {
		if(miOptimizador==null) miOptimizador = new Optimizador();
		return miOptimizador;
	}


	/**
	 * Se obtienen los parametros optimos del Multilayer Perceptron
	 * @param dataTrain
	 * @param dataDev
	 * @return lista parametros optimos
	 */
	public String[] optimizarMultilayerPerceptron(Instances dataTrain, Instances dataDev) {

		MultilayerPerceptron clasificador = null;

		try {
			/**
			 * h -> hiddenLayers
			 * l -> learning rate
			 */

			//Valores optimos iniciales
			String hiddenLayersOptimo = null; 
			double optimofmeasure=0, learningRateOptimo = 0;

			//hiddenLayers
			ArrayList<String> opcionesHiddenLayers = new ArrayList<String>();

			ArrayList<String> tmp = new ArrayList<String>();
			tmp.add("1");
			tmp.add("2");

			for(int i=0;i<tmp.size();i++) {
				for(int j=0;j<tmp.size();j++) {
					opcionesHiddenLayers.add(tmp.get(i) + ", " + tmp.get(j));
				}
			}

			//learningRate
			double[] opcionesLearningRate= {0.1,0.2,0.3,0.4,0.5,0.6,0.7,0.8,0.9,1.0};

			int loopsTotales = opcionesHiddenLayers.size()*opcionesLearningRate.length;
			int loopActual = 1;


			//bucle para buscar valores optimos para el clasificador
			for(double l : opcionesLearningRate){  //learning rate
				for(String h : opcionesHiddenLayers) { //hiddenLayers

					//se inicializa el clasificador 
					clasificador = ClasificadorMP.getMiClasificador().inicializar(l, h);

					//se evalua y se obtiene el fmeasure de la clase minoritaria
					double fmeasure = HoldOut.getMiEvaluator().getFMeasure(dataTrain, dataDev, clasificador);

								
					System.out.println(loopActual++ + "/" + loopsTotales);

					if(fmeasure > optimofmeasure) {
						learningRateOptimo = l;
						hiddenLayersOptimo = h;
						optimofmeasure=fmeasure;
					}
				}
			}

	
			//se inicializa el clasificador con las opciones óptimas

			clasificador = ClasificadorMP.getMiClasificador().inicializar(learningRateOptimo, hiddenLayersOptimo);
		} catch (Exception e) {
			System.out.println("Se ha producido un error al intentar optimizar el clasificador.");
		}

		return clasificador.getOptions();
	}
}
