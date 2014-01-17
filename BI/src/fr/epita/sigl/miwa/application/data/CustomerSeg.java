/***********************************************************************
 * Module:  BIController.java
 * Author:  Elodie NGUYEN THANH NHAN, Laura OLLIVIER & Chloé VASSEUR
 * Purpose: Defines the Class CustomerSeg
 ***********************************************************************/
package fr.epita.sigl.miwa.application.data;

public class CustomerSeg {
	private Integer id;
	private String name;
	private Integer minAge;
	private Integer maxAge;
	private String gender;
	private String maritalSituation;
	private Integer childrenNb;
	private String loyaltyType;
	
	public CustomerSeg() {
	}

	public CustomerSeg(Integer id, String name, Integer minAge, Integer maxAge, String gender,
			String maritalSituation, Integer childrenNb, String loyaltyType) {
		this.id = id;
		this.name = name;
		this.minAge = minAge;
		this.maxAge = maxAge;
		this.gender = gender;
		this.maritalSituation = maritalSituation;
		this.childrenNb = childrenNb;
		this.loyaltyType = loyaltyType;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getMinAge() {
		return minAge;
	}

	public void setMinAge(Integer minAge) {
		this.minAge = minAge;
	}

	public Integer getMaxAge() {
		return maxAge;
	}

	public void setMaxAge(Integer maxAge) {
		this.maxAge = maxAge;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getMaritalSituation() {
		return maritalSituation;
	}

	public void setMaritalSituation(String maritalSituation) {
		this.maritalSituation = maritalSituation;
	}

	public Integer getChildrenNb() {
		return childrenNb;
	}

	public void setChildrenNb(Integer childrenNb) {
		this.childrenNb = childrenNb;
	}

	public String getLoyaltyType() {
		return loyaltyType;
	}

	public void setLoyaltyType(String loyaltyType) {
		this.loyaltyType = loyaltyType;
	}

}