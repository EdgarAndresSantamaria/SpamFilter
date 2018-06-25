package MakeCompatible;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.FixedDictionaryStringToWordVector;
import weka.filters.unsupervised.attribute.StringToWordVector;
import weka.filters.unsupervised.instance.SparseToNonSparse;

public class ArffBOWCompatible {

	private static ArffBOWCompatible miArffBOWCompatible;

	public static ArffBOWCompatible getMiArffBOWCompatible() {
		if (miArffBOWCompatible==null) miArffBOWCompatible=new ArffBOWCompatible();
		return miArffBOWCompatible;
	}



	/**
	 * Los textos del fichero .arff se transforman en ficheros con textos
	 * en formato BOW o TFIDF compatibles con el espacio de atributos de train
	 * @param pathDiccionario
	 * @param pathFichero
	 * @param pathDestino
	 * @param bow
	 * @param sparse
	 * @param pathConfigFile 
	 */
	public void compatibilizar(String pathDiccionario, String pathFichero, String pathDestino, String pathConfigFile) {
		try {
			//Se cargan los datos
			Instances dataACompatibilizar = Datos.getMisDatos().cargarDatos(pathFichero);

			//Se carga la configuracion
			boolean bow=false; boolean sparse=true;
			File txt = new File(pathConfigFile);
			try (BufferedReader br = new BufferedReader(new FileReader(txt))) {
				String line = null;
				while ((line = br.readLine()) != null) { //Por cada linea
					if (line.contains("bow"))bow=true;
					if (line.contains("nosparse"))sparse=false;
				};
				br.close();
			}
			
			//Se busca el indice del atributo que tiene el texto a filtrar (atributo 'text')
			int indexAtrTexto;
			for(indexAtrTexto=0;indexAtrTexto<dataACompatibilizar.numAttributes();indexAtrTexto++) {
				String atributoTexto = dataACompatibilizar.attribute(indexAtrTexto).name();
				if(atributoTexto.toLowerCase().contains("text")) break;
			}
			indexAtrTexto += 1; //En weka se empieza desde 1, no 0

			// Se crea el filtro FixedDictionaryStringToWordVector (configuracion)
			FixedDictionaryStringToWordVector filtroFDSTV = new FixedDictionaryStringToWordVector();
			filtroFDSTV.setAttributeIndices(String.valueOf(indexAtrTexto)); //pos del atributo texto
			filtroFDSTV.setLowerCaseTokens(true);	// no distinguir entre mayusculas y minusculas
			filtroFDSTV.setOutputWordCounts(false);	// indicar el numero de veces que aparece la palabra en el texto
			//filtroSTWV.setWordsToKeep(2000);  // el numero de palabras a guardar 
			filtroFDSTV.setDictionaryFile(new File(pathDiccionario));

			if(!bow) { //Si se quiere TFIDF
				filtroFDSTV.setOutputWordCounts(true);
				filtroFDSTV.setNormalizeDocLength(true);
				filtroFDSTV.setTFTransform(false);
				filtroFDSTV.setIDFTransform(false);
			}

			// Se aplica el filtro FixedDictionaryStringToWordVector
			filtroFDSTV.setInputFormat(dataACompatibilizar);
			Instances bowData = Filter.useFilter(dataACompatibilizar, filtroFDSTV);

			if(!sparse) { //Si se quiere non-sparse
				
				/**Se crea el filtro SparseToNonSparse:
				 * 
				 * @attributes
				 * @ clase
				 * @ 'a'
				 * @ 'hola'
				 * 
				 * @data
				 * {ham, 2 1} --->{ham,0,1}
				 * 
				 * {matriz dispersa} ---->{matriz no dispersa}
				 */
				SparseToNonSparse filtroSTNS = new SparseToNonSparse();
				filtroSTNS.setInputFormat(bowData);
				
				
				// Se aplica el filtro
				bowData = Filter.useFilter(bowData, filtroSTNS);
				
			}

			//Se guardan los datos
			Datos.getMisDatos().guardarDatos(pathDestino, bowData);
		} catch (Exception e) {
			System.err.print("Se ha producido un error en la conversion");
			//e.printStackTrace();
		}  
	}

}
