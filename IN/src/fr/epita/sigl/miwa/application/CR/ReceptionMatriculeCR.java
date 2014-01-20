package fr.epita.sigl.miwa.application.CR;

public class ReceptionMatriculeCR {
	private EnteteCRM entete;
	private String matricule;
	private String nom;
	private String prenom;
	
	public ReceptionMatriculeCR() {
		// TODO Auto-generated constructor stub
	}
	
	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public EnteteCRM getEntete() {
		return entete;
	}

	public void setEntete(EnteteCRM entete) {
		this.entete = entete;
	}

	public String getMatricule()
	{
		return matricule;
	}
	
	public void setMatricule(String matricule)
	{
		this.matricule = matricule;
	}
	
	public String print_logger()
	{
		StringBuilder result = new StringBuilder();
		
		result.append("***** RECEPTION MATRICULE CLIENT [ \n");
		
		result.append(entete.print_logger());
		
		if (matricule == null || matricule.equals(""))
			result.append("***** 	Matricule [ NULL ]\n");
		else
			result.append("***** 	Matricule : " + matricule + "\n");
		
		if (nom == null || nom.equals(""))
			result.append("***** 	Nom : NULL\n");
		else
			result.append("***** 	Nom : " + nom + "\n");
		
		if (prenom == null || prenom.equals(""))
			result.append("***** 	Prénom : NULL\n");
		else
			result.append("***** 	Prénom : " + prenom + "\n");
		result.append("***** ]");
		
		return result.toString();
	}
	
	public void print()
	{
		entete.print();
		
		System.out.println("Matricule reçu : " + matricule + ";");
		System.out.println("	Nom : " + nom + ";");
		System.out.println("	Prenom : " + prenom + ";");
	}
}
