package fr.epita.sigl.miwa.application.object;

import java.util.ArrayList;

public class Segmentation {

	private int id;
	private ArrayList<Client> clients;
	private ArrayList<String> criteres;
	
	public Segmentation ()
	{
		this.clients = new ArrayList<Client>();
	}
	
	public void addCritere(String critere)
	{
		this.criteres.add(critere);
	}
	
	public void addClient(Client client)
	{
		this.clients.add(client);
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the clients
	 */
	public ArrayList<Client> getClients() {
		return clients;
	}

	/**
	 * @param clients the clients to set
	 */
	public void setClients(ArrayList<Client> clients) {
		this.clients = clients;
	}

	/**
	 * @return the criteres
	 */
	public ArrayList<String> getCriteres() {
		return criteres;
	}

	/**
	 * @param criteres the criteres to set
	 */
	public void setCriteres(ArrayList<String> criteres) {
		this.criteres = criteres;
	}
	
}
