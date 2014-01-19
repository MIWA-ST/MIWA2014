package fr.epita.sigl.miwa.application.object;

import java.util.Date;
import java.util.List;

public class TicketVente {
	
	private String refclient;
	private String moyenpayement;
	private Date date;
	private List<Article> article;
	
	public static List<TicketVente> ticketVentesList;
	
	public String getRefclient() {
		return refclient;
	}
	public void setRefclient(String refclient) {
		this.refclient = refclient;
	}
	public String getMoyenpayement() {
		return moyenpayement;
	}
	public void setMoyenpayement(String moyenpayement) {
		this.moyenpayement = moyenpayement;
	}
	public List<Article> getArticle() {
		return article;
	}
	public void setArticle(List<Article> article) {
		this.article = article;
	}
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	
}
