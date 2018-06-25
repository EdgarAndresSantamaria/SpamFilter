package paramOptimization;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Random;

import com.sun.deploy.uitoolkit.impl.fx.Utils;

import weka.classifiers.Classifier;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.lazy.IBk;
import weka.classifiers.meta.CVParameterSelection;
import weka.core.Instances;
import weka.core.Tag;
import weka.core.converters.ConverterUtils.DataSource;

public class Main {

	
	public static void main(String[] args) {

		// paramOptimization.jar -i train.arff dev.arff -o paramOptimos.txt
		
		if(args.length == 0) error();
		else if(args.length==5 && args[0].contains("-i") && args[3].contains("-o")) {
			Instances dataTrain = DatosArff.getMisDatos().cargarDatos(args[1]);
			Instances dataDev = DatosArff.getMisDatos().cargarDatos(args[2]);

			String[] listaOpcionesOptimas = Optimizador.getMiOptimizador().optimizarMultilayerPerceptron(dataTrain, dataDev);

			DatosClasificador.getMisDatos().guardarParametrosOptimosEn(args[4], listaOpcionesOptimas);

		}else error();
	}

	/**
	 * Mensaje de error por pantalla
	 */
	private static void error() {
		// paramOptimization.jar -i train.arff dev.arff -o paramOptimos.txt

		System.out.println("El software paramOptimization.jar permite obtener los parametros\n"
				+ "optimos del MultilayerPerceptron.");
		System.out.println();
		System.out.println("PRE-CONDICION: ");
		System.out.println("Train y dev son compatibles.");
		System.out.println("POST-CONDICION: ");
		System.out.println("Los parametros optimos se han guardado en el fichero de texto correspondiente.");
		System.out.println();
		System.out.println("java -jar paramOptimization.jar -i <INPUT> -o <OUTPUT>\n"
				+ "      <INPUT>: train.arff dev.arff\n"
				+ "      <OUTPUT>: paramOptimos.txt\n");
		System.out.println();
		System.out.println("Ejemplo de uso:");
		System.out.println("java -jar paramOptimization.jar -i train.arff dev.arff -o paramsOptimos.txt");

	}


}
