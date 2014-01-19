package fr.epita.sigl.miwa.application.object;

import java.util.ArrayList;
import java.util.List;

public class Group {
	
	private List<Client> clients;
	private List<Critere> criteres;
	public static List<Group> groups;
	
	public List<Client> getClients() {
		return clients;
	}
	public void setClients(List<Client> clients) {
		this.clients = clients;
	}
	public List<Critere> getCriteres() {
		return criteres;
	}
	public void setCriteres(List<Critere> criteres) {
		this.criteres = criteres;
	}
	
	
}
