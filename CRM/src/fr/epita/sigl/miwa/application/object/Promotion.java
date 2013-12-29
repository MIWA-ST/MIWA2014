package fr.epita.sigl.miwa.application.object;

public class Promotion {

	private int id;
	private int segmentation;
	private int produit;
	private int reduction;
	private int points;
	
	public Promotion (int id, int segmentation, int produit, int reduction, int points)
	{
		this.id = id;
		this.segmentation = segmentation;
		this.produit = produit;
		this.reduction = reduction;
		this.points = points;
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
	 * @return the segmentation
	 */
	public int getSegmentation() {
		return segmentation;
	}

	/**
	 * @param segmentation the segmentation to set
	 */
	public void setSegmentation(int segmentation) {
		this.segmentation = segmentation;
	}

	/**
	 * @return the produit
	 */
	public int getProduit() {
		return produit;
	}

	/**
	 * @param produit the produit to set
	 */
	public void setProduit(int produit) {
		this.produit = produit;
	}

	/**
	 * @return the reduction
	 */
	public int getReduction() {
		return reduction;
	}

	/**
	 * @param reduction the reduction to set
	 */
	public void setReduction(int reduction) {
		this.reduction = reduction;
	}

	/**
	 * @return the points
	 */
	public int getPoints() {
		return points;
	}

	/**
	 * @param points the points to set
	 */
	public void setPoints(int points) {
		this.points = points;
	}
	
}
