package getRaw;
import java.io.File;

public class Main {

	public static void main(String[] args){

		// java -jar getArff.jar

		// load :
		//
		// -d (directory) pathorigen pathdestino.arff
		// -c (csv) pathorigen.csv pathdestino.arff carpeta
		// -t (txt) pathorigen.txt pathdestino.arff 


		if(args.length == 0) {
			error();
		}else if(args[0].equals("-d")) { //Text directory
			if (args.length!=3 
					|| !args[2].contains(".arff")
					|| !new File(args[1]).isDirectory()) error();
			else {
				System.out.println("convertir directorio a .arff....");
				TextDirectoryToArff.getMiTextDirectoryToArff().convertir(args[1], args[2]);
			}
		}else if(args[0].equals("-c")) { //csv
			File tmp = new File(args[3]);
			if (args.length!=4 
					|| !args[1].contains(".csv") 
					|| !args[2].contains(".arff")
					|| (!tmp.isDirectory() && tmp.exists())) error();
			else {
				if(!tmp.exists()) tmp.mkdir();
				System.out.println("convertir csv a .arff....");
				CSVToArff.getMiCSVToArff().convertir(args[1], args[2], args[3]);
			}
		}else if(args[0].equals("-t")) { //txt
			if (args.length!=3 
					|| !args[1].contains(".txt") 
					|| !args[2].contains(".arff")) error();
			else {
				System.out.println("convertir texto a .arff....");
				TxtToArff.getMiTxtToArff().convertir(args[1], args[2]);
			}
		}else error();

	}

	/**
	 * Imprime por pantalla el uso optimo del programa cuando
	 * el usuario comete un error al introducir los argumentos o
	 * no introduce ninguno.
	 */
	private static void error() {
		System.out.println("Este programa consiste en convertir carpetas, ficheros .csv y ficheros .txt "
				+ "a ficheros .arff");
		System.out.println();
		System.out.println("PRE-CONDICION: Los ficheros a convertir pertenecen a uno de los conjuntos de "
				+ "datos siguientes: \n"
				+ "- movie_reviews\n"
				+ "- tweet_sentiment\n"
				+ "- sms_spam");
		System.out.println();
		System.out.println("Instrucciones de uso");
		System.out.println("java -jar getRaw.jar <OPCION>\r\n");
		System.out.println("<OPCION>:\n"
				+ "Para carpetas: -d origenCarpeta destino.arff\r\n" + 
				"Para .csv: -c origen.csv destino.arff carpetaTemporal\r\n" + 
				"Para .txt: -t origen.txt destino.arff");
		System.out.println();
		System.out.print("POST-CONDICION: El fichero .arff se ha creado");
	}
}

