package fr.epita.sigl.miwa.application.CR;

public class ReceptionMatriculeCR {
	private String matricule;
	
	public ReceptionMatriculeCR() {
		// TODO Auto-generated constructor stub
	}
	
	public String getMatricule()
	{
		return matricule;
	}
	
	public void setMatricule(String matricule)
	{
		this.matricule = matricule;
	}
	
	public void print()
	{
		System.out.println("Matricule reçu : " + matricule + ";");
	}
}
