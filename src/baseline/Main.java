package baseline;

import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;

public class Main {


	public static void main(String[] args) {


		// GetBaselineModel.jar -h -i train.arff dev.arff -o NaiveBayes.model CalidadEstimada.txt
		//						-n -i train.arff -o NaiveBayes.model CalidadEstimada.txt
		//                      -t -i train.arff -o NaiveBayes.model CalidadEstimada.txt


		// crear fichero de calidad dependiendo del tipo de evaluacion elegida
		NaiveBayes modelo = null;
		String pathGuardarModelo = null;

		if(args.length==0) error();
		else if (args.length==7 && args[0].contains("-h")) {
			//-h = HoldOut
			Instances train = Datos.getMisDatos().cargarDatos(args[2]);
			Instances dev = Datos.getMisDatos().cargarDatos(args[3]);
			pathGuardarModelo = args[5];
			modelo = Evaluador.getMiEvaluador().evaluarPorHoldOut(train, dev, new NaiveBayes(), args[6]);
		}
		else if (args.length==6 && args[0].contains("-n")) {
			Instances train = Datos.getMisDatos().cargarDatos(args[2]);
			pathGuardarModelo = args[4];
			//-n = NoHonesta
			modelo = Evaluador.getMiEvaluador().evaluarPorNoHonesta(train, new NaiveBayes(), args[5]);
		}
		else if (args.length==6 && args[0].contains("-t")) {
			Instances train = Datos.getMisDatos().cargarDatos(args[2]);
			pathGuardarModelo = args[4];
			//-t = TenCrossValidation
			modelo = Evaluador.getMiEvaluador().evaluarPorTenCrossValidation(train, new NaiveBayes(), args[5]);
		}else error();

		if(modelo != null) {
			// Se guarda el modelo
			try {
				weka.core.SerializationHelper.write(pathGuardarModelo, modelo);
			} catch (Exception e) {
				System.out.println("Error al intentar guardar el modelo.");
			}
		}

	}


	/**
	 * Instrucciones de uso que se muestran por falta de argumentos o errores.
	 */
	public static void error() {
		
		// GetBaselineModel.jar -h -i train.arff dev.arff -o NaiveBayes.model CalidadEstimada.txt
		//						-n -i train.arff -o NaiveBayes.model CalidadEstimada.txt
		//                      -t -i train.arff -o NaiveBayes.model CalidadEstimada.txt
		
		System.out.println("El software GetBaselineModel.jar permite obtener el modelo\n"
				+ "baseline NaiveBayes.model y su calidad estimada.");
		System.out.println();
		System.out.println("PRE-CONDICION: ");
		System.out.println("Los archivos train y dev son compatibles.");
		System.out.println("POST-CONDICION: ");
		System.out.println("Se ha guardado el modelo NaiveBayes y su calidad estimada en dos ficheros.");
		System.out.println();
		System.out.println("java -jar getBaselineModel.jar <OPCION1> -i <INPUT> -o <OUTPUT>\n"
				+ "\n<OPCION1>\n"
				+ "-h (Hold-out)\n"
				+ "      <INPUT>: train.arff dev.arff\n"
				+ "      <OUTPUT>: NaiveBayes.model CalidadEstimada.txt\n"
				+ "\n"
				+ "-n (No-honesta)\n" 
				+ "      <INPUT>: train.arff \n" 
				+ "      <OUTPUT>: NaiveBayes.model CalidadEstimada.txt\n"
				+ "\n"
				+ "-t (Ten-fold-cross-validation)\n" 
				+ "      <INPUT>: train.arff \n" 
				+ "      <OUTPUT>: NaiveBayes.model CalidadEstimada.txt\n");
		System.out.println();
		System.out.println("Ejemplo de uso:");
		System.out.println("---Evaluacion Hold-out---");
		System.out.println("java -jar getBaselineModel.jar -h -i train.arff dev.arff -o modelo.model calidad.txt");
		System.out.println();
		System.out.println("---Evaluacion No-Honesta---");
		System.out.println("java -jar getBaselineModel.jar -n -i train.arff -o modelo.model calidad.txt");
		System.out.println();
		System.out.println("---Evaluacion Ten-fold-cross-validation---");
		System.out.println("java -jar getBaselineModel.jar -t -i train.arff -o modelo.model calidad.txt");
		System.out.println();
	}
}
