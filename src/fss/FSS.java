package fss;

import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;

public class FSS {

	private static FSS miFSS;

	private FSS() {

	}

	public static FSS getMiFSS() {
		if (miFSS==null) miFSS=new FSS();
		return miFSS;
	}


	/**
	 * Optiene el numero de atributos optimos para el fichero trainData
	 * @param trainData -> fichero arff de los datos train
	 * @return integer con el numero de atributos optimos de train
	 */
	public int calcularNumeroAtributosOptimos(Instances trainData) {
		try {
			//Se aplica el filtro con la configuracion por defecto
			AttributeSelection filtroFSS = (AttributeSelection) configurarFiltro(trainData, -1);
			Instances trainDataFiltrado = 	aplicarFiltro(filtroFSS, trainData); //SE aplica el filtro

			//Se evalua por primera vez con NaiveBayes
			double estadisticaAntigua = KFoldCrossValidation.getMiFSS().evaluar(trainDataFiltrado, new NaiveBayes());
			double estadisticaActual = 100.0;

			//se van borrando los atributos y se va comprobando el porcentaje de aciertos
			while(estadisticaAntigua < estadisticaActual) {
				estadisticaAntigua = estadisticaActual;

				//se borra el ultimo atributo
				trainDataFiltrado.deleteAttributeAt(trainDataFiltrado.numAttributes()-2); //Se tiene en cuenta la clase

				//se evalua y se obtiene la estadistica de aciertos
				estadisticaActual = KFoldCrossValidation.getMiFSS().evaluar(trainDataFiltrado, new NaiveBayes());
				//System.out.println(estadisticaActual);
			}
			//obtenemos el numero maximo de atributos con el que mantenemos la maxima estadistica
			// se resta uno, porque el parametro numToSelect no cuenta con la clase
			return trainDataFiltrado.numAttributes()-1;

		} catch (Exception e) {
			System.out.println("Error al calcular el numero de atributos optimos");
			//e.printStackTrace();
		}
		return 0;
	}


	/**
	 * Se crea un filtro AttributeSelection, se configura segun los parametros
	 * pasados, y se devuelve
	 * @param data
	 * @param numToSelectParam
	 * @return El filtro configurado
	 * @throws Exception
	 */
	public AttributeSelection configurarFiltro(Instances data, int numToSelectParam) {
		//Se aplica el filtro con la configuracion por defecto
		AttributeSelection filtroFSS = new AttributeSelection();
		InfoGainAttributeEval evaluator = new InfoGainAttributeEval();
		Ranker search = new Ranker();

		//damos valor a los parametros del algoritmo ranker
		search.setNumToSelect(numToSelectParam); //se mantienen todos los atributos
		search.setThreshold(0.0); //Si es cero o menor que cero se descartan
		filtroFSS.setEvaluator(evaluator);
		filtroFSS.setSearch(search);
		try {
			filtroFSS.setInputFormat(data);
		} catch (Exception e) {
			System.out.println("Se ha producido un error al configurar el filtro.");
		//	e.printStackTrace();
		}

		return filtroFSS;
	}


	/**
	 * Se aplica el filtro AttributeSelection a un conjunto de datos
	 * @param filtro
	 * @param data
	 * @return data tras pasar por el filtro
	 * @throws Exception
	 */
	public Instances aplicarFiltro(AttributeSelection filtro, Instances data) {
		//Aplicamos el filtro fss obtenido con el train
		Instances dataResultante = null;
		try {
			dataResultante = Filter.useFilter(data, filtro);
		} catch (Exception e) {
			System.out.println("Se ha producido un error al intentar aplicar el filtro.");
		}

		return dataResultante;

	}

	/**
	 * Carga el filtro desde un fichero
	 * @param pathFiltro
	 * @return El filtro en formato weka java
	 * @throws Exception
	 */
	public AttributeSelection cargarFiltro(String pathFiltro) {
		AttributeSelection filtro = null;
		try {
			filtro = (AttributeSelection)weka.core.SerializationHelper.read(pathFiltro);
		} catch (Exception e) {
			System.out.println("Se ha producido un error al intentar cargar el filtro.");
		}
		return filtro;
	}

	/**
	 * Guarda el filtro en un fichero
	 * @param pathDestinoFiltro
	 * @param filtro
	 * @throws Exception
	 */
	public void guardarFiltro(String pathDestinoFiltro, AttributeSelection filtro) {
		try {
			weka.core.SerializationHelper.write(pathDestinoFiltro, filtro);
		} catch (Exception e) {
			System.out.println("Se ha producido un error al intentar guardar el filtro.");
		}
	}
}
