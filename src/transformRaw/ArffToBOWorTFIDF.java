package transformRaw;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.Tag;
import weka.core.stopwords.StopwordsHandler;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.FixedDictionaryStringToWordVector;
import weka.filters.unsupervised.attribute.StringToWordVector;
import weka.filters.unsupervised.instance.SparseToNonSparse;

public class ArffToBOWorTFIDF {

	private static ArffToBOWorTFIDF miArffToArffBow;

	public static ArffToBOWorTFIDF getMiArffToArffBow() {
		if (miArffToArffBow==null) miArffToArffBow=new ArffToBOWorTFIDF();
		return miArffToArffBow;
	}

	/**
	 * Los textos del fichero train.arff se transforman en ficheros con textos
	 * en formato BOW o TFIDF
	 * @param pathOrigen
	 * @param pathDestino
	 * @param pathDiccionario
	 * @param bow
	 * @param sparse
	 * @param pathConfig 
	 */
	public void convertirTrain(String pathOrigen, String pathDestino, String pathDiccionario, boolean bow, boolean sparse, String pathConfig) {
		try {
			//Se cargan los datos
			Instances data = Datos.getMisDatos().cargarDatos(pathOrigen);

			//Se busca el indice del atributo que tiene el texto a filtrar (atributo 'text')
			int indexAtrTexto;
			for(indexAtrTexto=0;indexAtrTexto<data.numAttributes();indexAtrTexto++) {
				String atributoTexto = data.attribute(indexAtrTexto).name();
				if(atributoTexto.toLowerCase().contains("text")) break;
			}
			indexAtrTexto += 1; //En weka se empieza desde 1, no 0

			// Se crea el filtro StringToWordVector (configuracion)
			StringToWordVector filtroSTWV=new StringToWordVector();
			filtroSTWV.setAttributeIndices(String.valueOf(indexAtrTexto)); //pos del atributo texto
			filtroSTWV.setLowerCaseTokens(true);	// no distinguir entre mayusculas y minusculas
			filtroSTWV.setOutputWordCounts(false);	// indicar el numero de veces que aparece la palabra en el texto
			//filtroSTWV.setWordsToKeep(2000);  // el numero de palabras a guardar 
			filtroSTWV.setDictionaryFileToSaveTo(new File(pathDiccionario));

			if(!bow) { //Si se quiere TFIDF
				filtroSTWV.setOutputWordCounts(true);
				filtroSTWV.setNormalizeDocLength(new SelectedTag(StringToWordVector.FILTER_NORMALIZE_ALL, StringToWordVector.TAGS_FILTER)); 
				filtroSTWV.setTFTransform(true);
				filtroSTWV.setIDFTransform(true);
			}

			// Se aplica el filtro StringToWordVector
			filtroSTWV.setInputFormat(data);
			Instances bowData = Filter.useFilter(data, filtroSTWV);


			if(!sparse) { //Si se quiere non-sparse


				/**Se crea el filtro SparseToNonSparse:
				 * 
				 * @attributes
				 * @ clasefiltroSTNS
				 * @ 'a'
				 * @ 'hola'
				 * 
				 * @data
				 * {ham, 2 1} --->{ham,0,1}
				 * 
				 * {matriz dispersa} --->{matriz no dispersa}
				 */
				SparseToNonSparse filtroSTNS = new SparseToNonSparse();
				filtroSTNS.setInputFormat(bowData);
				// Se aplica el filtro
				bowData = Filter.useFilter(bowData, filtroSTNS);

			}
			//Se guardan los datos
			Datos.getMisDatos().guardarDatos(pathDestino, bowData);


			//Se guarda el fichero de configuracion
			File txt = new File(pathConfig);
			try (BufferedWriter br = new BufferedWriter(new FileWriter(txt))) {
				if(bow)br.write("bow\n\r");else br.write("tfidf\n\r");
				br.newLine();
				if(sparse)br.write("sparse\n");else br.write("nosparse\n");
				br.close();
				
			}


		} catch (Exception e) {
			//e.getMessage();
			//System.err.print("Se ha producido un error en la conversion");
			e.printStackTrace();
		}  


	}


}
