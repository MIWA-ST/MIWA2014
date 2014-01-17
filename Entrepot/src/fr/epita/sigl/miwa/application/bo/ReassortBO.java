package fr.epita.sigl.miwa.application.bo;

import java.util.List;

public class ReassortBO
{
	private String commandNumber;
	private String dateBC;
	private String dateBL;
	private String BackOfficeRef;
	private String backOfficePhone;
	private String backOfficeAddress;
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
	public String getBackOfficeRef() {
		return BackOfficeRef;
	}
	public void setBackOfficeRef(String backOfficeRef) {
		BackOfficeRef = backOfficeRef;
	}
	public String getBackOfficePhone() {
		return backOfficePhone;
	}
	public void setBackOfficePhone(String backOfficePhone) {
		this.backOfficePhone = backOfficePhone;
	}
	public String getBackOfficeAddress() {
		return backOfficeAddress;
	}
	public void setBackOfficeAddress(String backOfficeAddress) {
		this.backOfficeAddress = backOfficeAddress;
	}
	public List<Article> getArticles() {
		return articles;
	}
	public void setArticles(List<Article> articles) {
		this.articles = articles;
	}
}
