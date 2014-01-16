package fr.epita.sigl.miwa.application.parser;

import java.io.File;
import java.util.List;
import java.util.Map;

import fr.epita.sigl.miwa.application.criteres.Critere;
import fr.epita.sigl.miwa.application.data.Client;
import fr.epita.sigl.miwa.application.data.DetailSale;
import fr.epita.sigl.miwa.application.data.MDMData;
import fr.epita.sigl.miwa.application.data.ProductCategory;
import fr.epita.sigl.miwa.application.data.Sale;
import fr.epita.sigl.miwa.application.data.Stock;
import fr.epita.sigl.miwa.application.enums.EBOMessageType;

public class BIParser {

	/**
	 * Parse le message envoyé par la GC et le BO
	 * @param message
	 * @return La liste des stocks
	 */
	public List<Stock> parseStockMessage(String message){
		return null;
	}

	/**
	 * Parse les messages envoyés par le BO
	 * @param message
	 * @return Une enum pour savoir ce que contient la liste des résultats
	 */
	public Map<EBOMessageType, List<Object>> parseBOMessage(String message){
		return null;
	}

	/**
	 * Parse les critères envoyés par le CRM
	 * @param message
	 * @return La liste des critères
	 */
	public List<Critere> parseCRMMessage(String message) {
		return null;
	}

	/**
	 * Parse les ventes envoyées par internet et le BO
	 * @param message
	 * @return La liste des ventes
	 */
	public List<Sale> parseSaleMessage(String message) {
		return null;		
	}

	/**
	 * Parse le fichier des clients envoyé par le CRM
	 * @param file
	 * @return La liste des clients
	 */
	public List<Client> parseClientFile(File file) {
		return null;		
	}

	/**
	 * Parse le fichier envoyé par le référentiel
	 * @param file Le fichier envoyé par le référentiel
	 * @return La liste des données envoyées par le référentiel (promos + catégorie + produits)
	 */
	public MDMData parseMDMFile(File file) {
		return null;		
	}

	/**
	 * Parse le fichier envoyé par le BO et internet
	 * @param file
	 * @return La liste des ventes détaillées
	 */
	public List<DetailSale> parseDetailSale(File file) {
		return null;		
	}


}
