package fr.epita.sigl.miwa.application;

import java.util.List;

public class DemandeNiveauStock {
	
	private String commandNumber;
	private String refbo;
	private String datedemand;
	private String daterep;
	private List<Articles> articles;
	private List<String> quantity;
	
	public String getRefbo() {
		return refbo;
	}
	public void setRefbo(String refbo) {
		this.refbo = refbo;
	}
	
	public String getCommandNumber() {
		return commandNumber;
	}
	public void setCommandNumber(String commandNumber) {
		this.commandNumber = commandNumber;
	}
	public String getDatedemand() {
		return datedemand;
	}
	public void setDatedemand(String datedemand) {
		this.datedemand = datedemand;
	}
	public String getDaterep() {
		return daterep;
	}
	public void setDaterep(String daterep) {
		this.daterep = daterep;
	}
	public List<Articles> getArticles() {
		return articles;
	}
	public void setArticles(List<Articles> articles) {
		this.articles = articles;
	}
	public List<String> getQuantity() {
		return quantity;
	}
	public void setQuantity(List<String> quantity) {
		this.quantity = quantity;
	}
}
