package getModel;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Random;

import com.sun.deploy.uitoolkit.impl.fx.Utils;

import weka.classifiers.Classifier;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.lazy.IBk;
import weka.classifiers.meta.CVParameterSelection;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.Tag;
import weka.core.converters.ConverterUtils.DataSource;

public class Main {


	public static void main(String[] args) {

		// getModel.jar -n -i train.arff dev.arff paramsOptimos.txt -o Modelo.model CalidadEsperada.txt
		//               -t -i train.arff dev.arff paramsOptimos.txt -o Modelo.model CalidadEsperada.txt

			
		MultilayerPerceptron clasificador = null;
		if(args.length==0) error();
		else if(args.length==8 && args[0].contains("-n")) {
			//si la evaluacion es no-honesta

			Instances train = DatosArff.getMisDatos().cargarDatos(args[2]);
			Instances dev = DatosArff.getMisDatos().cargarDatos(args[3]);

			train.addAll(dev); //se anaden las instancias de dev a train
			
			String[] parametrosClasificador = DatosClasificador.getMisDatos().importarParametrosOptimosDe(args[4]);

			clasificador = new MultilayerPerceptron();
			try {
				clasificador.setOptions(parametrosClasificador);
				Evaluador.getMiEvaluador().evaluarPorNoHonesta(train, clasificador, args[7]);
			} catch (Exception e) {
				System.out.println("Se ha producido un error al intentar configurar el clasificador.");
			}


		}else if(args.length==8 && args[0].contains("-t")){
			//si la evaluacion es TenCrossValidation

			Instances train = DatosArff.getMisDatos().cargarDatos(args[2]);
			Instances dev = DatosArff.getMisDatos().cargarDatos(args[3]);
			
			train.addAll(dev); //se anaden las instancias de dev a train
			String[] parametrosClasificador = DatosClasificador.getMisDatos().importarParametrosOptimosDe(args[4]);

			clasificador = new MultilayerPerceptron();
			try {
				clasificador.setOptions(parametrosClasificador);
				Evaluador.getMiEvaluador().evaluarPorTenCrossValidation(train, clasificador, args[7]);
			} catch (Exception e) {
				System.out.println("Se ha producido un error al intentar configurar el clasificador.");
			}
		}else error();

		if(clasificador !=null) {
			DatosModelos.getMisDatos().guardarModelo(args[6], clasificador);
		}

	}

	/**
	 * Mensaje de error a mostrar por pantalla
	 */
	private static void error() {
		// getModel.jar -n -i train.arff dev.arff paramsOptimos.txt -o Modelo.model CalidadEsperada.txt
		//              -t -i train.arff dev.arff paramsOptimos.txt -o Modelo.model CalidadEsperada.txt

		System.out.println("El software getModel.jar dados los parametros optimos,\n"
				+ "obtiene el modelo predictor y su calidad estimada.");
		System.out.println();
		System.out.println("PRE-CONDICION: ");
		System.out.println("Se tienen los parametros optimos en txt.");
		System.out.println("POST-CONDICION: ");
		System.out.println("Se ha guardado el modelo y la calidad esperada.");
		System.out.println();
		System.out.println("java -jar getModel.jar <OPCION1> -i <INPUT> -o <OUTPUT>\n"
				+ "\n<OPCION1>\n"
				+ "-n (No-honesta)\n" 
				+ "      <INPUT>: train.arff dev.arff paramsOptimos.txt \n" 
				+ "      <OUTPUT>: modelo.model CalidadEstimada.txt\n"
				+ "\n"
				+ "-t (Ten-fold-cross-validation)\n" 
				+ "      <INPUT>: train.arff dev.arff \n" 
				+ "      <OUTPUT>: modelo.model CalidadEstimada.txt\n");
		System.out.println();
		System.out.println("Ejemplo de uso:");
		System.out.println("---Evaluacion No-Honesta---");
		System.out.println("java -jar getModel.jar -n -i train.arff dev.arff paramsOptimos.txt -o modelo.model calidad.txt");
		System.out.println();
		System.out.println("---Evaluacion Ten-fold-cross-validation---");
		System.out.println("java -jar getModel.jar -t -i train.arff dev.arff paramsOptimos.txt -o modelo.model calidad.txt");
		System.out.println();
	}

}

