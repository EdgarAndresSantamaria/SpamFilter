package transformRaw;


public class Main {

	/**
	 * Transformar el espacio de atributos del conjunto de entrenamiento a BoW 
	 * o TFIDF (segun se indique en los argumentos) permitiendo dar como 
	 * salida una representacion Sparse o NonSparse (indicado mediante argumentos).
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		boolean bow=false, sparse=false;

		if(args.length==0) error();
		else if(args.length == 6) { 

			if(args[0].contains("-bow")) bow=true;
			if(args[1].contains("-s")) sparse=true;

			//aplicar filtros a instancias train 
			ArffToBOWorTFIDF.getMiArffToArffBow().convertirTrain(args[2], args[3], args[4], bow, sparse, args[5]);

		}else error();

	}

	/**
	 * Mensaje de error
	 */
	private static void error() {
		System.out.println("Este programa permite transformar el espacio de atributos \\n"
				+ "del conjunto de entrenamiento a BoW o TFIDF\\n "
				+ "(segun se indique en los argumentos) permitiendo dar como salida una\\n "
				+ "representacion Sparse o NonSparse (indicado mediante argumentos).");
		System.out.println();
		System.out.println("PRE-CONDICION: Los ficheros a convertir estan en formato raw.arff");
		System.out.println();
		System.out.println("Instrucciones de uso");
		System.out.println("java -jar transformRaw.jar <OPCION1> <OPCION2> <ARCHIVOS>\r\n");
		System.out.println("<OPCION1>:\n"
				+ "-tfidf\n"
				+ "-bow\n");
		System.out.println("<OPCION2>:\n"
				+ "-s   (sparse)\n"
				+ "-ns    (non-sparse)\n");
		System.out.println("<ARCHIVOS>:\n"
				+ " origen.arff destinoBOW.arff pathDiccionario pathConfigFile.txt\n");
		System.out.println("Ejemplo: Teniendo trainraw.arff se desea obtener un archivo\n"
				+ "bow y sparse:\n"
				+ "java -jar transformRaw.jar -bow -s trainraw.arff trainDestino.arff diccionario config.txt");
		System.out.println();
		System.out.print("POST-CONDICION: El fichero .arff, el diccionario y config.txt se han creado");
		
		
	}

}
