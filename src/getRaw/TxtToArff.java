package getRaw;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

public class TxtToArff {

	private static TxtToArff miTxtToArff;


	public static TxtToArff getMiTxtToArff() {
		if (miTxtToArff==null) miTxtToArff=new TxtToArff();
		return miTxtToArff;
	}

	/**
	 * Se convierte un fichero (origen.txt) en un fichero arff (destino.arff)
	 * @param pathOrigen (.txt)
	 * @param pathDestino (.arff)
	 */
	public void convertir(String pathOrigen, String pathDestino)  {
		ArrayList<Attribute> listaAtributos = new ArrayList<Attribute>();
		//Atributo string text
		listaAtributos.add(new Attribute("text", (List<String>) null)); 

		//Atributo nominal class
		List<String> b = new ArrayList<String>();
		b.add("ham");
		b.add("spam");
		listaAtributos.add(new Attribute("@@class@@", b));

		//Se crean las instancias vacias
		Instances data = new Instances("text_files_in_" + pathOrigen, listaAtributos, 0);

		data.setClassIndex(data.numAttributes()-1); //Se define la clase

		//Se obtiene el contenido del fichero txt
		File txt = new File(pathOrigen);
		try (BufferedReader br = new BufferedReader(new FileReader(txt))) {
			String line = null;
			while ((line = br.readLine()) != null) { //Por cada linea

				String mensaje=line;
				String clase = null;

				String[] lineaSeparada = line.split("\\t",2); //Los dos valores se separan por tabulador

				//por si no fuera test
				if(lineaSeparada.length != 1) {//si hay tab.....
					mensaje = lineaSeparada[1];//hay mensaje..
					clase = lineaSeparada[0];//hay clase..
				}
				
				//Se limpian los caracteres
				mensaje = mensaje.replaceAll("[^A-Za-z]", " ");

				//Se crean los valores a anadir a una instancia
				double[] valores = new double[2];

				//Se anade el contenido los valores de la instancia
				valores[0] = (double)data.attribute(0).addStringValue(mensaje); //texto

				//Se crea una instancia y se anaden los valores
				Instance unaInstancia = new DenseInstance(1.0,valores);

				unaInstancia.setDataset(data); //Se especifica el dataset al que pertenece

				if(clase!=null) unaInstancia.setClassValue(clase); //si tiene clase
				else unaInstancia.setClassMissing(); //Se especifica que la clase no esta (test)

				//Se anade la instancia al conjunto de instancias "instances"
				data.add(unaInstancia);
			}
			//	System.out.print(data.toString());

			//Se guardan en un fichero arff
			Datos.getMisDatos().guardarDatos(pathDestino, data);

		} catch (IOException e) {
			System.out.print("Se ha producido un error en la conversion");
			e.printStackTrace();
		}
	}
}
