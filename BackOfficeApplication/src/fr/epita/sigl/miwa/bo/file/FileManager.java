package fr.epita.sigl.miwa.bo.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class FileManager
{
	public static void createFile(String fileName, String content)
	{
//		Writer writer = null;

		try {
			
			File file = new File("C:/git_repository/miwa_env/LoacalRepository/BO/" + fileName);

			// if file exists, then delete it
			if (file.exists()) {
				file.delete();
			}

			file.createNewFile();

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
			
//	        FileOutputStream out = new FileOutputStream(" C:\\git_repository\\miwa_env\\LoacalRepository\\BO\\" + fileName);
//		    PrinterWriter writer = new PrinterWriter(BufferedWriter(new OutputStreamWriter(out, "utf-8")));
//		    System.out.println("poire");
//		    writer.write(content);
		    System.out.println("Fichier " + fileName + " crée");
		} catch (IOException e) {
		  e.printStackTrace();
		} 
	}
}
