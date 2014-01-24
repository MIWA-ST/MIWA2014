package fr.epita.sigl.miwa.application;

public class Produit {
	private int id = 0;
	private String nom = "";
	private String ref = "";
	private float prix = 0;
	private int promo = 0;
	private int quantite = 0;
	
	public Produit(String string, String string2, String string3, String string4, String string5) {
		this.id = Integer.parseInt(string);
		this.prix = Float.parseFloat(string2);
		this.setRef(string3);
		this.nom = string4;
		this.promo = Integer.parseInt(string5);
	}

	public Produit(int id, float prix, String ref, String nom, int promo)
	{
		this.id = id;
		this.ref = ref;
		this.nom = nom;
		this.prix = prix;
		this.promo = promo;
		
	}

	public Produit() {
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public float getPrix() {
		return prix;
	}

	public void setPrix(float prix) {
		this.prix = prix;
	}

	public int getPromo() {
		return promo;
	}

	public void setPromo(int promo) {
		this.promo = promo;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public int getQuantite() {
		return quantite;
	}

	public void setQuantite(int quantite) {
		this.quantite = quantite;
	}
	
}
