package s3_predictions;

import java.io.File;

public class Utilidades {

	private static Utilidades misUtilidades;

	public static Utilidades getMisUtilidades() {
		if (misUtilidades==null) misUtilidades=new Utilidades();
		return misUtilidades;
	}

	public boolean isFolder(String path) {
		File fi = new File(path);
		if(fi.exists()) return fi.isDirectory();
		return false;
	}

	public boolean extensionesIgual(String path1, String path2) {
		if(getFileExtension(path1).contains(getFileExtension(path2))) return true;
		return false;
	}
	
	public String getFileExtension(String pathFile) {
		File file = new File(pathFile);
		String fileName = file.getName();
		if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
			return fileName.substring(fileName.lastIndexOf(".")+1);
		else return "";
	}

	public String getPath(String tipo, String carpetaData) {
		File fi = new File(carpetaData);
		File[] archivosDentroData = fi.listFiles();
		for(File unArchivo : archivosDentroData) {
			if(unArchivo.getName().toLowerCase().contains(tipo)) {
				return unArchivo.getAbsolutePath();
			}
		}
		return "FAIL";
	}
}
