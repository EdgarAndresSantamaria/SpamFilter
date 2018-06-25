package paramOptimization;

import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.lazy.IBk;
import weka.core.Instances;

public class HoldOut {

	private static HoldOut miEvaluator;


	/**
	 * recoger la instancia unica de Cross-validation con k=10
	 * @return
	 */
	public static HoldOut getMiEvaluator() {
		if(miEvaluator==null)
			miEvaluator = new HoldOut();
		return miEvaluator;
	}

	/**
	 * devuelve la f-measure de la clasificacion
	 * @param dataTrain (dataset de entrenamiento/evaluacion)
	 * @param dataDev 
	 * @param clasificador (algoritmo de clasificacion)
	 * @return
	 * @throws Exception
	 */
	public double getFMeasure(Instances dataTrain, Instances dataDev, MultilayerPerceptron clasificador){
		Evaluation evaluator;
		double fMeasure=-1;
		try {
			clasificador.buildClassifier(dataTrain);

			//evaluacion del modelo
			evaluator = new Evaluation(dataTrain);
			evaluator.evaluateModel(clasificador, dataDev);

			//Fmeasure de la clase con menos instancias
			int indiceClase = Instancias.getMisInstancias().indiceClaseConMenosInstancias(dataTrain);
			fMeasure=evaluator.fMeasure(indiceClase);
		//	System.out.println(evaluator.pctCorrect());

			// precision and recall are both zero, the F1-score is undefined (NaN)
			if(Double.isNaN(fMeasure)) fMeasure = 0;
		} catch (Exception e) {
			System.out.println("Fallo al entrenar/evaluar el modelo.");
		}
		return fMeasure;
	}



}
