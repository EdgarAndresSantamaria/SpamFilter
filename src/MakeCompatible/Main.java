package MakeCompatible;

public class Main {

	public static void main(String[] args)  {
				
		if(args.length==0) error();
		else if(args.length==4) {
			
			ArffBOWCompatible.getMiArffBOWCompatible().compatibilizar(args[1],args[2], args[3], args[0]);
			
		}else error();

	}

	private static void error() {
		System.out.println("Este programa consiste en crear ficheros dev/test compatibles\n"
				+ "a ficheros trainBOW.arff o trainTFIDF.arff, para ello se utilizara su diccionario de palabras.");
		System.out.println();
		System.out.println("PRE-CONDICION: Los ficheros a compatibilizar estan en formato raw.arff\n"
							+ "y existe un diccionario del fichero train sobre el que compatibilizar ");
		System.out.println();
		System.out.println("Instrucciones de uso");
		System.out.println("java -jar makeCompatible.jar config.txt pathDiccionario archivo.arff archivoCompatible.arff");
		System.out.println();
		System.out.print("POST-CONDICION: El fichero .arff compatible se ha creado");
	}

	
}
