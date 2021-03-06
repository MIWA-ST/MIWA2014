package fr.epita.sigl.miwa.application.bo;

import java.util.List;

public class CommandeInternet
{
	private String commandNumber;
	private String dateBC;
	private String dateBL;
	private String customerRef;
	private String customerLastname;
	private String customerFirstname;
	private String customerAddress;
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
	public String getCustomerRef() {
		return customerRef;
	}
	public void setCustomerRef(String customerRef) {
		this.customerRef = customerRef;
	}
	public String getCustomerLastname() {
		return customerLastname;
	}
	public void setCustomerLastname(String customerLastname) {
		this.customerLastname = customerLastname;
	}
	public String getCustomerFirstname() {
		return customerFirstname;
	}
	public void setCustomerFirstname(String customerFirstname) {
		this.customerFirstname = customerFirstname;
	}
	public String getCustomerAddress() {
		return customerAddress;
	}
	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}
	public List<Article> getArticles() {
		return articles;
	}
	public void setArticles(List<Article> articles) {
		this.articles = articles;
	}
}
