package fr.epita.sigl.miwa.application.object;

import java.util.ArrayList;
import java.util.Date;

public class Segmentation {

	private int id;
	private Date date;
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}


	private static ArrayList<Client> clients;
	private ArrayList<Critere> criteres;
	
	public Segmentation ()
	{
		this.clients = new ArrayList<Client>();
	}
	
	public void addCritere(Critere critere)
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
	public ArrayList<Critere> getCriteres() {
		return criteres;
	}

	/**
	 * @param criteres the criteres to set
	 */
	public void setCriteres(ArrayList<Critere> criteres) {
		this.criteres = criteres;
	}
	
	
	public static void AssocClientSeq(int id)
	{
		for (Client c : Client.clientsList)
		{
			if (c.getMatricule() == id)
			{
				clients.add(c);
			}
		}
	}
	
	
}
