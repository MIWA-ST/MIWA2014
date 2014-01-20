package fr.epita.sigl.miwa.application.object;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Segmentation {

	private int id;
	private Date date;
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	private static List<Client> clients;
	private List<Critere> criteres;
	public static ArrayList<Promotion> promotions = new ArrayList<Promotion>();
	
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
	

	public void addPromotion(Promotion promo)
	{
		this.promotions.add(promo);
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
	public List<Client> getClients() {
		return clients;
	}

	/**
	 * @param clients the clients to set
	 */
	public void setClients(List<Client> clients) {
		this.clients = clients;
	}

	/**
	 * @return the criteres
	 */
	public List<Critere> getCriteres() {
		return criteres;
	}

	/**
	 * @param criteres the criteres to set
	 */
	public void setCriteres(List<Critere> criteres) {
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
