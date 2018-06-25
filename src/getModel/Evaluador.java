package getModel;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Instances;

public class Evaluador {

	private static Evaluador miEvaluador;

	public static Evaluador getMiEvaluador() {
		if (miEvaluador == null)
			miEvaluador = new Evaluador();
		return miEvaluador;
	}



	/**
	 * Se evalua por ten-cross-validation el conjunto train mediante el clasificador pasado
	 * como parametro. El resultado de la evaluacion se guarda en ficheroCalidad.
	 * @param clasificador
	 * @param ficheroCalidad
	 * @throws Exception
	 */
	public void evaluarPorTenCrossValidation(Instances train, MultilayerPerceptron clasificador, String ficheroCalidad){
		Evaluation evaluator;
		try {
			evaluator = new Evaluation(train);
			// datos para entrenar
			evaluator.crossValidateModel(clasificador, train, 10, new Random(1));
			escribirFicheroCalidad(evaluator, ficheroCalidad, "TenFoldCrossValidation");
		} catch (Exception e) {
			System.out.println("Error al evaluar por Hold-Out");
		}
	}

	/**
	 * Se evalua por evaluacion no-honesta el conjunto train mediante el clasificador pasado
	 * como parametro. El resultado de la evaluacion se guarda en ficheroCalidad.
	 * @param train
	 * @param clasificador
	 * @param ficheroCalidad
	 * @throws Exception
	 */
	public void evaluarPorNoHonesta(Instances train, MultilayerPerceptron clasificador,
			String ficheroCalidad){
		// NO-HONESTA: entrena y testea con las mismas intancias
		// * (use training set) -- Supplied test set
		Evaluation evaluator;
		try {
			evaluator = new Evaluation(train);

			clasificador.buildClassifier(train); // construir clasificador
			evaluator = new Evaluation(train); // datos para entrenar
			evaluator.evaluateModel(clasificador, train);
			escribirFicheroCalidad(evaluator, ficheroCalidad, "No-Honesta");
		} catch (Exception e) {
			System.out.println("Error al evaluar por no-honesta");
		}
	}


	/**
	 * Se escribe un txt con los resultados obtenidos en la evaluacion pasada
	 * como parametro
	 * @param evaluator
	 * @param ficheroCalidad
	 * @param name 
	 * @throws Exception
	 */
	public void escribirFicheroCalidad(Evaluation evaluator, String ficheroCalidad, String name){
		//escribir fichero de calidad
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(ficheroCalidad));
			bw.write("-----------------------------------------------------");
			bw.newLine();
			bw.write("----------------------- Fichero de Calidad " + name + "----------------------");
			bw.newLine();

			bw.write("-----------------------------------------------------");

			bw.newLine();
			bw.write(evaluator.toSummaryString());
			bw.newLine();
			bw.write(evaluator.toMatrixString());
			bw.newLine();
			bw.write(evaluator.toClassDetailsString());
			bw.newLine();
			bw.flush();
			bw.close();
		} catch (Exception e) {
			System.out.println("Se ha producido un error al intentar guardar \n"
					+ "la evaluacion en un fichero");
		}
	}

}
