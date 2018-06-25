package getModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils.DataSource;

public class DatosModelos {

	private static DatosModelos misDatos;

	public static DatosModelos getMisDatos() {
		if (misDatos==null) misDatos=new DatosModelos();
		return misDatos;
	}

	/**
	 * Se guarda el modelo "clasificador" en path
	 * @param path
	 * @param clasificador
	 */
	public void guardarModelo(String path, MultilayerPerceptron clasificador) {
		//guardar modelo
		try {
			SerializationHelper.write(path, clasificador);
		} catch (Exception e) {
			System.out.println("Se ha producido un error al intentar guardar el modelo.");
		}

	}
}
