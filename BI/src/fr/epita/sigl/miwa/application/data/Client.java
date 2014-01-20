/***********************************************************************
 * Module:  Client.java
 * Author:  Elodie NGUYEN THANH NHAN, Laura OLLIVIER & Chloé VASSEUR
 * Purpose: Defines the Class Client
 ***********************************************************************/
package fr.epita.sigl.miwa.application.data;

import java.util.*;

public class Client {
	private Integer numero;
	private String title;
	private Date birthDate;
	private Integer zipcode;
	private String maritalStatus;
	private Boolean children;
	private String loyaltyType;
   
	public Client() {
	}

	public Client(Integer numero, String title, Date birthDate,
			Integer zipcode, String maritalStatus, Boolean children,
			String loyaltyType) {
		this.numero = numero;
		this.title = title;
		this.birthDate = birthDate;
		this.zipcode = zipcode;
		this.maritalStatus = maritalStatus;
		this.children = children;
		this.loyaltyType = loyaltyType;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public Integer getZipcode() {
		return zipcode;
	}

	public void setZipcode(Integer zipcode) {
		this.zipcode = zipcode;
	}

	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public Boolean isChildren() {
		return children;
	}

	public void setChildren(Boolean children) {
		this.children = children;
	}

	public String getLoyaltyType() {
		return loyaltyType;
	}

	public void setLoyaltyType(String loyaltyType) {
		this.loyaltyType = loyaltyType;
	}

}