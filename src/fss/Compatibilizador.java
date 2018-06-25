package fss;

import weka.core.Instances;
import weka.filters.supervised.attribute.AttributeSelection;

public class Compatibilizador {

	private static Compatibilizador miCompatibilizador;

	private Compatibilizador() {

	}

	public static Compatibilizador getMiCompatibilizador() {
		if (miCompatibilizador==null) miCompatibilizador=new Compatibilizador();
		return miCompatibilizador;
	}
	
	/**
	 * Se compatibiliza un fichero respecto al filtro pasado como parametro y se guarda
	 * @param pathFicheroACompatibilizar
	 * @param pathFiltro
	 * @param pathFicheroDestino
	 * @throws Exception
	 */
	public void compatibilizarAFiltro(String pathFicheroACompatibilizar, String pathFiltro, String pathFicheroDestino) {
		AttributeSelection filtro = FSS.getMiFSS().cargarFiltro(pathFiltro);
		Instances dataACompatibilizar = DatosARFF.getMisDatos().cargarDatos(pathFicheroACompatibilizar);
		
		Instances dataFiltradaCompatible = FSS.getMiFSS().aplicarFiltro(filtro, dataACompatibilizar);
		DatosARFF.getMisDatos().guardarDatos(pathFicheroDestino, dataFiltradaCompatible);

	}
}
