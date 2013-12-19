package fr.epita.sigl.miwa.application;

import java.util.List;

public class LivraisonFournisseur
{
	private String commandNumber;
	private String dateBC;
	private String dateBL;
	private List<Article> articles;
	
	public String getCommandNumber() {
		return commandNumber;
	}
	public void setCommandNumber(String commandNumber) {
		this.commandNumber = commandNumber;
	}
	public String getDateBC() {
		return dateBC;
	}
	public void setDateBC(String dateBC) {
		this.dateBC = dateBC;
	}
	public String getDateBL() {
		return dateBL;
	}
	public void setDateBL(String dateBL) {
		this.dateBL = dateBL;
	}
	public List<Article> getArticles() {
		return articles;
	}
	public void setArticles(List<Article> articles) {
		this.articles = articles;
	}
}
