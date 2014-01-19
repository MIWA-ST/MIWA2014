package fr.epita.sigl.miwa.application;

public class ThreadVente extends Thread {
	public void run() {
		Vente vente = new Vente();
		try {
			vente.create();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }
	public ThreadVente() {
	}
}
