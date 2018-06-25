package getRaw;

import java.io.File;
import java.io.IOException;

import weka.core.Instances;
import weka.core.converters.ArffSaver;

public class Datos {

	private static Datos misDatos;

	public static Datos getMisDatos() {
		if (misDatos==null) misDatos=new Datos();
		return misDatos;
	}


	/**
	 * Se guardan las instancias "data" en el fichero "path"
	 * @param path
	 * @param data
	 */
	public void guardarDatos(String path, Instances data) {
		try {
			//guardar datos....
			File fi = new File(path);
			ArffSaver saver = new ArffSaver();
			//establecer conjunto de instancias a guardar
			saver.setInstances(data);
			//establecer fichero en el que guardar
			saver.setFile(fi);
			//escritura del fichero
			saver.writeBatch();
		} catch (IOException e) {
			System.err.print("Error al guardar los datos en un fichero .arff");
		}
	}
}
