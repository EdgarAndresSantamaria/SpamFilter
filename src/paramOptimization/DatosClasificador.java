package paramOptimization;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils.DataSource;

public class DatosClasificador {

	private static DatosClasificador misDatos;

	public static DatosClasificador getMisDatos() {
		if (misDatos==null) misDatos=new DatosClasificador();
		return misDatos;
	}


	/**
	 * Se importan los parametros optimos almacenados en pathTxt
	 * @param pathTxt
	 * @return
	 */
	public String[] importarParametrosOptimosDe(String pathTxt) {
		String[] listaParametros = null;
		try {

			StringBuilder contenidoTxt = new StringBuilder();
			BufferedReader in = new BufferedReader(new FileReader(pathTxt));

			String linea = in.readLine();

			while(linea != null)	{
				contenidoTxt.append(linea+ "p");
				linea = in.readLine();
				
			}
			in.close();

			contenidoTxt.deleteCharAt(contenidoTxt.length()-1);

			listaParametros = contenidoTxt.toString().split("p");

		} catch (IOException e) {
			System.out.println("Se ha producido un error importando los parametros optimos");
		}
		return listaParametros;
	}



	public void guardarParametrosOptimosEn(String pathTxt, String[] listaOpciones) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(pathTxt));

			for(String unaOpcion : listaOpciones) {
				bw.write(unaOpcion);
				bw.newLine();
			}

			bw.flush();
			bw.close();
		} catch (IOException e) {
			System.out.println("Se ha producido un error guardando los parametros optimos");
		}
	}
}
