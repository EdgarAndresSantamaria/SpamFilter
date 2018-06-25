package fss;

import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.evaluation.Evaluation;
import weka.core.Instances;

public class KFoldCrossValidation {

	private static KFoldCrossValidation miKFoldCrossValidation;

	private KFoldCrossValidation() {

	}

	public static KFoldCrossValidation getMiFSS() {
		if (miKFoldCrossValidation==null) miKFoldCrossValidation=new KFoldCrossValidation();
		return miKFoldCrossValidation;
	}

	/**
	 * Evalua por 10-Fold-Cross-Validation el data con el clasificador pasado
	 * como parametro
	 * @param data
	 * @param clasificador
	 * @return El porcentaje de instancias clasificadas correctamente
	 */
	public double evaluar(Instances data, Classifier clasificador) {
		try {
			Evaluation evaluador = new Evaluation(data);
			evaluador.crossValidateModel(clasificador, data, 10, new Random(1));

			//obtenemos la estadistica de aciertos de la evaluacion
			return evaluador.pctCorrect();
		} catch (Exception e) {
			System.out.println("Se ha producido un error al intentar evaluar los datos.");
		}
		
		return -1;
	}
}
