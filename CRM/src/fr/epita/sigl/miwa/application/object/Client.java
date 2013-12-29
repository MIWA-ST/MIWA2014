/**
 * 
 */
package fr.epita.sigl.miwa.application.object;

/**
 * @author clementlavigne
 *
 */
public class Client {
	
	private int matricule;
	private String nom;
	private String prenom;
	
	public Client (int id, String nom, String prenom)
	{
		this.matricule = id;
		this.nom = nom;
		this.prenom = prenom;
	}

	/**
	 * @return the matricule
	 */
	public int getMatricule() {
		return matricule;
	}


	/**
	 * @param matricule the matricule to set
	 */
	public void setMatricule(int matricule) {
		this.matricule = matricule;
	}


	/**
	 * @return the nom
	 */
	public String getNom() {
		return nom;
	}

	/**
	 * @param nom the nom to set
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	 * @return the prenom
	 */
	public String getPrenom() {
		return prenom;
	}

	/**
	 * @param prenom the prenom to set
	 */
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	
	
}
