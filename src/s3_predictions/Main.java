package s3_predictions;

import java.io.File;
import java.nio.file.Files;

import org.apache.commons.compress.compressors.FileNameUtil;

public class Main {

	private static Utilidades utilidades = Utilidades.getMisUtilidades();

	public static void main(String[] args) {
		// Primer caso
		// java -jar predictions.jar <OPCION1> <OPCION2> -i carpeta_data -o program_data
		// (carpeta)
		// <OPCION1> : -bow/-tfidf (arg[0]]) <OPCION2>: -s/-ns (arg[1]])
		// solo train y dev

		args = new String[6];
		args[0] = "-bow";
		args[1] = "-ns";
		args[3] = "\"C:\\Users\\Paula\\Google Drive\\UPV_EHU\\3º\\2º Cuatrimestre\\SAD\\Proyecto\\parte 1\\datos\\22\"";
		args[5] = "\"C:\\Users\\Paula\\Google Drive\\UPV_EHU\\3º\\2º Cuatrimestre\\SAD\\Proyecto\\parte 1\\datos\\22\\programData\"";

		// los argumentos de los paths estan entre "" y las carpetas no tienen barra al
		// final
		if (args.length == 0)
			error();
		else if (args.length == 6) {
			// Primer caso

			// quitar comillas si hay
			args[3] = args[3].split("\"")[1];
			args[5] = args[5].split("\"")[1];

			// Las carpetas de datos programa y datos existe,
			String datosPrograma = args[5];
			String carpetaData = args[3];

			if (!utilidades.isFolder(datosPrograma) || !utilidades.isFolder(carpetaData))
				error();
			else {
				// Se obtienen los paths de train y dev dentro de la carpeta data
				String trainPath = utilidades.getPath("train", carpetaData);
				String devPath = utilidades.getPath("dev", carpetaData);
				if (trainPath.contains("FAIL") || devPath.contains("FAIL"))
					error();
				else {
					// Si los ficheros train.csv/txt y dev.csv/txt (o carpetas train y dev)
					// existen dentro de la carpeta data
					String sentencia = "";

					if (utilidades.isFolder(trainPath) && utilidades.isFolder(devPath)) {
						// Si son carpetas
						sentencia = "-dsepara" + trainPath + "separa" + datosPrograma + "/1trainraw.arff";
						getRaw.Main.main(sentencia.split("separa")); // train

						sentencia = "-dsepara" + devPath + "separa" + datosPrograma + "/1devraw.arff";
						getRaw.Main.main(sentencia.split("separa")); // Dev
					} else if (utilidades.extensionesIgual(trainPath, devPath)
							&& utilidades.getFileExtension(trainPath).contains("txt")) {
						// Ficheros txt
						sentencia = "-tsepara" + trainPath + "separa" + datosPrograma + "/1trainraw.arff";
						getRaw.Main.main(sentencia.split("separa")); // train

						sentencia = "-tsepara" + devPath + "separa" + datosPrograma + "/1devraw.arff";
						getRaw.Main.main(sentencia.split("separa")); // Dev
					} else if (utilidades.extensionesIgual(trainPath, devPath)
							&& utilidades.getFileExtension(trainPath).contains("csv")) {
						// Ficheros csv

						// Se crea la carpeta temporal
						String path = datosPrograma + "/TEMPORAL";
						File f = new File(path);
						f.mkdirs();

						sentencia = "-csepara" + trainPath + "separa" + datosPrograma + "/1trainraw.arff" + "separa"
								+ f.getAbsolutePath();
						getRaw.Main.main(sentencia.split("separa")); // train

						sentencia = "-csepara" + devPath + "separa" + datosPrograma + "/1devraw.arffsepara"
								+ f.getAbsolutePath();
						getRaw.Main.main(sentencia.split("separa")); // Dev
					}

					trainPath = datosPrograma + "/1trainraw.arff";
					devPath = datosPrograma + "/1devraw.arff";

					String opcion1 = args[0];
					String opcion2 = args[1];

					// transformRaw
					String diccionarioPath = datosPrograma + "/2diccionario";
					String configPath = datosPrograma + "/2config.txt";
					String trainDestinoPath = datosPrograma + "/2train" + opcion1.split("-")[1] + ".arff";
					sentencia = opcion1 + "separa" + opcion2 + "separa" + trainPath + "separa" + trainDestinoPath
							+ "separa" + diccionarioPath + "separa" + configPath;
					transformRaw.Main.main(sentencia.split("separa"));

					// makeCompatible
					String compatibleDevPath = datosPrograma + "/3devCompatible.arff";
					sentencia = configPath + "separa" + diccionarioPath + "separa" + devPath + "separa"
							+ compatibleDevPath;
					MakeCompatible.Main.main(sentencia.split("separa"));

					// fss(train)
					String filtroPath = datosPrograma + "/4filtro";
					String trainFssPath = datosPrograma + "/4trainfss.arff";
					String devFssPath = datosPrograma + "/4devfss.arff";
					sentencia = "-tsepara" + "" + "-isepara" + trainDestinoPath + "separa" + "-osepara" + filtroPath
							+ "separa" + trainFssPath;
					fss.Main.main(sentencia.split("separa"));

					// fss(dev)
					sentencia = "-rsepara" + " " + "-isepara" + compatibleDevPath + "separa" + filtroPath + "separa-o"
							+ "separa" + devFssPath;
					fss.Main.main(sentencia.split("separa"));

					// paramOptimization
					String paramOptimosPath = datosPrograma + "/5paramOptimos.txt";
					sentencia = "-isepara" + trainFssPath + "separa" + devFssPath + "separa" + "-osepara"
							+ paramOptimosPath;
					paramOptimization.Main.main(sentencia.split("separa"));

					// getModel
					String modeloPath = datosPrograma + "/6modelo.model";
					String calidadEsperadaPath = datosPrograma + "/6calidadEsperada.txt";
					sentencia = "-tsepara" + "-isepara" + trainFssPath + "separa" + devFssPath + "separa"
							+ paramOptimosPath + "separa" + "-osepara" + modeloPath + "separa" + calidadEsperadaPath;
					getModel.Main.main(sentencia.split("separa"));

					// baseline
					String NaiveBayesPath = datosPrograma + "/7NaiveBayes.model";
					String calidadEstimadaPath = datosPrograma + "/7calidadEstimada.txt";
					sentencia = "-tsepara" + "-isepara" + trainFssPath + "separa" + "-osepara" + NaiveBayesPath
							+ "separa" + calidadEstimadaPath;
					getModel.Main.main(sentencia.split("separa"));
				}

			}

		}

	}

	private static void error() {
		System.out.println("ERROR");
	}

}
