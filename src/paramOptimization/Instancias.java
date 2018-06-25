package paramOptimization;

import weka.core.Instances;

public class Instancias {

	private static Instancias misInstancias;

	public static Instancias getMisInstancias() {
		if (misInstancias==null) misInstancias=new Instancias();
		return misInstancias;
	}
	
	
	/**
	 * Calcula el indice de la clase que tiene menos instancias de data
	 * @param data
	 * @return un int con el indice de la clase
	 */
	public int indiceClaseConMenosInstancias(Instances data) {
		int indiceClase = 0;
		int numeroApariciones=-1;

		for(int i=0;i<data.numClasses();i++) {
			int numAparicionesClase = data.attributeStats(data.classIndex()).nominalCounts[i];

			if(numeroApariciones==-1 || numAparicionesClase < numeroApariciones) {
				numeroApariciones=numAparicionesClase; 
				indiceClase = i;
			}
		}
		
		return indiceClase;
	}
}
