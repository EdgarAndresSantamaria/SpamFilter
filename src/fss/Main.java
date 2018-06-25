package fss;

public class Main {

	public static void main(String[] args)  {
		
		// fss.jar -t -i train.arff -o filtro trainfss.arff
		//         -r -i dev.arff filtro -o devfss.arff
		//         -r -i test.arff filtro -o testfss.arff
		
			if(args.length==0) error();
			else if(args.length==6 && args[0].contains("-t")) {
				String trainOrigen = args[2];
				String pathFiltro = args[4];
				String trainDestino = args[5];
				Optimizador.getMiOptimizador().optimizarTrain(trainOrigen, pathFiltro, trainDestino);

			}else if(args.length==6 && args[0].contains("-r")) {
				String fichero = args[2];
				String pathFiltro = args[3];
				String ficheroDestino = args[5];
				Compatibilizador.getMiCompatibilizador().compatibilizarAFiltro(fichero, pathFiltro, ficheroDestino);
			}
			
	}

	/**
	 * Mensaje de error por pantalla
	 */
	private static void error() {
		// fss.jar -t -i train.arff -o filtro trainfss.arff
		//         -r -i dev.arff filtro -o devfss.arff
		//         -r -i test.arff filtro -o testfss.arff
		
		System.out.println("El software fss.jar permite seleccionar atributos predictores en el conjunto\n "
				+ "de entrenamiento y adaptar el espacio de atributos del conjunto\n "
				+ "de evaluacion al de entrenamiento.");
		System.out.println();
		System.out.println("PRE-CONDICION:  Los ficheros a convertir pertenecen a uno de los conjuntos de "
				+ "datos siguientes: \n"
				+ "- movie_reviews\n"
				+ "- tweet_sentiment\n"
				+ "- sms_spam");
		System.out.println("POST-CONDICION: ");
		System.out.println("Los ficheros train, dev y test son compatibles.");
		System.out.println();
		System.out.println("java -jar fss.jar <OPCION1> -i <INPUT> -o <OUTPUT>\n"
				+ "\n<OPCION1>\n"
				+ "-t\n"
				+ "      <INPUT>: train.arff\n"
				+ "      <OUTPUT>: filtro trainfss.arff\n"
				+ "\n"
				+ "-r\n"
				+ "      <INPUT>: fichero.arff filtro\n" 
				+ "      <OUTPUT>: ficherofss.arff");
		System.out.println();
		System.out.println("Ejemplo de uso:");
		System.out.println("---Fichero train---");
		System.out.println("java -jar fss.jar -t -i train.arff -o filtro trainfss.arff");
		System.out.println();
		System.out.println("---Fichero dev---");
		System.out.println("java -jar fss.jar -r -i dev.arff filtro -o devfss.arff");
		System.out.println();
		System.out.println("---Fichero test---");
		System.out.println("java -jar fss.jar -r -i test.arff filtro -o testfss.arff");
	}



}
