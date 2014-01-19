package fr.epita.sigl.miwa.application.object;

public class CarteFidelite {

	private int limite_m;
	private int limite_tot;
	private int echellon;
	private int solde;
	private String type;
	
	public CarteFidelite(String str)
	{
		setType(str);
	}
	
	
	public String getType() {
		return type;
	}



	public void setType(String type) {
		this.type = type;
	}



	public int getSolde() {
		return solde;
	}
	public void setSolde(int solde) {
		this.solde = solde;
	}
	public int getLimite_m() {
		return limite_m;
	}
	public void setLimite_m(int limite_m) {
		this.limite_m = limite_m;
	}
	public int getLimite_tot() {
		return limite_tot;
	}
	public void setLimite_tot(int limite_tot) {
		this.limite_tot = limite_tot;
	}
	public int getEchellon() {
		return echellon;
	}
	public void setEchellon(int echellon) {
		this.echellon = echellon;
	}
	
	
}
