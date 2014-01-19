package fr.epita.sigl.miwa.application;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Logger;

public class CsvParser {
	
	private String fileName;
	private DbHandler dbHandler;
	
	public void parse() {
		Path path = FileSystems.getDefault().getPath(".", fileName);
		try {
			BufferedReader reader = Files.newBufferedReader(path, Charset.forName("ISO-8859-1"));
			
			String header = reader.readLine();
			if (header == null)
				System.out.println("Erreur : Fichier vide");
			
			Boolean firstligne = true;
			String line = null;
			this.dbHandler.clearProductsForProvider(1);
			while ( (line = reader.readLine()) != null ) {
				String[] fields = line.split(";");
				if (fields.length != 7)
				{
					System.out.println("Erreur : Nombre de colonnes incorrect (8 attendues)");
					continue;
				}
				
				String EAN = fields[0];
				if (EAN.length() != 13)
				{
					System.out.println("Erreur : Mauvais champ EAN");
					continue;
				}
				String description = fields[1];
				Integer buyPriceWithoutVat;
				Integer vat;
				try {
					buyPriceWithoutVat = Integer.parseInt(fields[3].replaceAll(",", "."));
					vat = Integer.parseInt(fields[4].replaceAll(",", "."));
				}
				catch (NumberFormatException e) {
					e.printStackTrace();
					continue;
				}
				
				String buyPrice = Integer.toString(buyPriceWithoutVat + (buyPriceWithoutVat * vat / 100));

				String nbMin = fields[5].replaceAll(",", ".");
				Product p = new Product(EAN, description, buyPrice, nbMin, "", 1);

				if (firstligne) {
					final Logger LOGGER = Logger.getLogger(CsvParser.class.getName());
					LOGGER.severe("***** " + "Parsing du premier produit (Exemple) : EAN du produit=" + EAN + " / Prix fournisseur du produit (TTC)=" + buyPrice);
					firstligne = false;
				}
				this.dbHandler.addNewProduct(p);
				
			}
			
		} catch (NoSuchFileException e) {
			System.out.println("File not found !");
			e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	

	public CsvParser(String fileName) {
		this.fileName = fileName;
		this.dbHandler = new DbHandler("jdbc:mysql://localhost/miwa", "root", "root");
	}

}
