package fss;

import weka.core.Instances;
import weka.filters.supervised.attribute.AttributeSelection;

public class Optimizador {

	private static Optimizador miOptimizador;

	private Optimizador() {

	}

	public static Optimizador getMiOptimizador() {
		if (miOptimizador==null) miOptimizador=new Optimizador();
		return miOptimizador;
	}
	
	public void optimizarTrain(String pathTrain, String pathFiltroDestino, String pathTrainDestino){
		Instances dataTrain = DatosARFF.getMisDatos().cargarDatos(pathTrain);
		int numAtributosOptimosEnTrain = FSS.getMiFSS().calcularNumeroAtributosOptimos(dataTrain);
		
		AttributeSelection filtroOptimo = FSS.getMiFSS().configurarFiltro(dataTrain, numAtributosOptimosEnTrain);
		Instances dataFiltrada = FSS.getMiFSS().aplicarFiltro(filtroOptimo, dataTrain);
		
		DatosARFF.getMisDatos().guardarDatos(pathTrainDestino, dataFiltrada);
		FSS.getMiFSS().guardarFiltro(pathFiltroDestino, filtroOptimo);
		
	}
	
	
	
}
