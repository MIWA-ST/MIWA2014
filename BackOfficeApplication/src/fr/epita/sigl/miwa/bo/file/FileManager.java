package fr.epita.sigl.miwa.bo.file;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class FileManager
{
	public static void createFile(String fileName, String content)
	{
		Writer writer = null;

		try {
		    writer = new BufferedWriter(new OutputStreamWriter(
		          new FileOutputStream("../../../LoacalRepository/BO/" + fileName), "utf-8"));
		    writer.write(content);
		    System.out.println("Fichier " + fileName + " crée");
		} catch (IOException e) {
		  e.printStackTrace();
		} finally {
		   try {
			   writer.close();
		   } catch (Exception e) {
			   e.printStackTrace();
		   }
		}
	}
}
