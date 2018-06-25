package getRaw;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.TextDirectoryLoader;

public class TextDirectoryToArff {

	private static TextDirectoryToArff miTextDirectoryToArff;

	public static TextDirectoryToArff getMiTextDirectoryToArff() {
		if (miTextDirectoryToArff==null) miTextDirectoryToArff=new TextDirectoryToArff();
		return miTextDirectoryToArff;
	}

	/**
	 * Convertir directorio de archivos a ARFF
	 * @param pathOrigen
	 * @param pathDestino
	 * @throws Exception 
	 */
	public void convertir(String pathOrigen, String pathDestino) {
		try {
			
			File[] directories = new File(pathOrigen).listFiles(File::isDirectory);
			if(directories.length == 0) convertirSinClase(pathOrigen, pathDestino);
			else convertirConClase(pathOrigen, pathDestino);
		} catch (IOException e) {
			System.out.println("Se ha producido un error en la conversion.");
			e.printStackTrace();
		}
	}

	/**
	 * Convertir un directorio que contiene subdirectorios con el nombre de las clases. 
	 * Cada subdirectorio contiene el listado de txt correspondiente.
	 * @param pathOrigen
	 * @param pathDestino
	 * @throws IOException
	 */
	private void convertirConClase(String pathOrigen, String pathDestino) throws IOException {
		//Se carga el directorio
		File fi = new File(pathOrigen);
		TextDirectoryLoader loader = new TextDirectoryLoader();
		loader.setSource(fi);

		//Se obtienen los datos
		Instances data = loader.getDataSet();

		//Se guardan en un fichero arff
		Datos.getMisDatos().guardarDatos(pathDestino, data);
	}


	/**
	 * Convertir un directorio sin saber la clase (un conjunto de ficheros txt)
	 * @param pathOrigen
	 * @param pathDestino
	 * @throws IOException
	 */
	private void convertirSinClase(String pathOrigen, String pathDestino) throws IOException{

		ArrayList<Attribute> listaAtributos = new ArrayList<Attribute>();
		//Atributo string text
		listaAtributos.add(new Attribute("text", (List<String>) null)); 

		//Atributo nominal class
		List<String> b = new ArrayList<String>();
		b.add("neg");
		b.add("pos");
		listaAtributos.add(new Attribute("@@class@@", b));

		//Se crean las instancias vacias
		Instances data = new Instances("text_files_in_" + pathOrigen, listaAtributos, 0);

		data.setClassIndex(data.numAttributes()-1); //Se define la clase

		File dir = new File(pathOrigen);
		for (final File oneFile : dir.listFiles()) { //Por cada fichero txt dentro del directorio
			String path = oneFile.getPath();
			if (path.endsWith(".txt")) {
				try {
					//Se crean los valores a anadir a una instancia
					double[] valores = new double[2];

					//Se obtiene el contenido del fichero txt
					File txt = new File(path);

					//generamos un buffer streameado (leer fichero)
					InputStreamReader is = new InputStreamReader(new FileInputStream(txt));
					//generamos un buffer para el contenido
					StringBuffer txtStr = new StringBuffer();
					int c;
					//cargamos el buffer con el contenido del documento
					while ((c = is.read()) != -1) {
						txtStr.append((char)c);
					}
					is.close();

					//Se anade el contenido a los valores de la instancia
					valores[0] = (double)data.attribute(0).addStringValue(txtStr.toString()); //texto

					//Se crea una instancia y se anaden los valores
					Instance unaInstancia = new DenseInstance(1.0,valores);

					unaInstancia.setDataset(data); //Se especifica el dataset al que pertenece

					unaInstancia.setClassMissing(); //Se especifica que la clase no esta

					//Se anade la instancia al conjunto de instancias "instances"
					data.add(unaInstancia);
				} catch (Exception e) {
					//System.out.print(e.getLocalizedMessage());
					System.err.println("failed to convert file: " + path);
				}
			}
		}
		
		//Se guardan en un fichero arff
		Datos.getMisDatos().guardarDatos(pathDestino, data);
	}



}
