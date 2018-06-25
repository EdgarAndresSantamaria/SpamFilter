package getRaw;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import weka.core.Instances;
import weka.core.converters.CSVLoader;

public class CSVToArff {

	private static CSVToArff miCSVToArff;
	private final String COMMA_DELIMITER = ",";
	private final String NEW_LINE_SEPARATOR = "\n";

	public static CSVToArff getMiCSVToArff() {
		if (miCSVToArff==null) miCSVToArff=new CSVToArff();
		return miCSVToArff;
	}

	/**
	 * Guarda en pathDestino (destino.arff) los datos contenidos por pathOrigen (origen.csv)
	 * @param pathOrigen (,csv)
	 * @param pathDestino (.arff)
	 * @param pathDirectorioTemporal (directory)
	 */
	public void convertir(String pathOrigen, String pathDestino, String pathDirectorioTemporal) {
		try {
			//Cargar fichero csv
			BufferedReader br = new BufferedReader(new FileReader(pathOrigen));
			String line = "";

			//Leer header para atributos
			String header = br.readLine();
			String[] a = header.split(",");
			header = a[4] + "," + "@@class@@"; //Se guarda la clase y el texto del mensaje
			
			//Generar fichero temporal csv limpio 
			File tmp = new File(pathDirectorioTemporal + "/csvtweetsLimpio.csv");

			FileWriter fiW = new FileWriter(tmp);
			fiW.write(header); //Se escriben los atributos en la cabecera
			fiW.append(NEW_LINE_SEPARATOR);

			while ((line = br.readLine()) != null) { //Por cada linea del fichero csv original..

				if(!line.isEmpty()) { //Si la linea no esta vacia..

					//Se separa por comas, siempre que haya un numero par de comillas detras
					String[] instanciaElemento=	line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"); 

					//Si estan todos los valores (5 atributos = 5 valores en una fila)
					if(instanciaElemento.length == 5) {
						String textoStrip = "\"" + instanciaElemento[4].replaceAll("\"", "").replaceAll(";", "").replaceAll("[^A-Za-z]", " ")+ "\"";
						fiW.append(textoStrip);
						fiW.append(COMMA_DELIMITER);
						fiW.append(instanciaElemento[1]);
						fiW.append(NEW_LINE_SEPARATOR);
					}
				}
			}
			fiW.close();
			br.close();

			//Se utiliza el CSVLoader para obtener los datos
			Instances data = CSVLoader(tmp, ",", "2", "1", "UNKNOWN");
			
			//Los datos se guardan en un fichero arff
			Datos.getMisDatos().guardarDatos(pathDestino, data);
			
			//Se borra el directorio temporal
			tmp.delete();
			
		} catch (IOException e) {
			System.out.println("Se ha producido un error en la conversion.");
			e.printStackTrace();
		}
	}

	/**
	 * * Se hace uso de las librerias de WEKA para obtener los datos de un fichero CSV
	 * @param tmp (directory)
	 * @param fieldSeparator
	 * @param nominalAttributes
	 * @param stringAttributes
	 * @param missingValue
	 * @return
	 * @throws IOException
	 */
	public Instances CSVLoader(File tmp, String fieldSeparator, String nominalAttributes,String stringAttributes, String missingValue) throws IOException {
		//configurar...
		//generamos Loader de ficheros CSV
		CSVLoader loader = new CSVLoader();
		//expresion que separa los atributos en nuestro caso (,)
		loader.setFieldSeparator(fieldSeparator);
		//despreciar la fila cabecera normalmente descripcion del problema
		loader.setNoHeaderRowPresent(false);
		//determinar posiciones de los atributos nominales 	
		//		ejemplo "2" --> nominal attribute index 
		loader.setNominalAttributes(nominalAttributes);
		//determinar posiciones de los atributos String 
		//		ejemplo "2" --> String attribute index 
		loader.setStringAttributes(stringAttributes);
		//asignar el fichero a cargar
		loader.setFile(tmp);
		//establecer el valor de atributo ddesconocido
		loader.setMissingValue(missingValue);
		//cargar......
		//recuperar instancias
		Instances data = loader.getDataSet();
		//establecer indice de la clase (ultimo valor)
		data.setClassIndex(data.numAttributes()-1);
		return data;
		
	}

}
