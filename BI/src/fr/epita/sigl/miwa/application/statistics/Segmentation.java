package fr.epita.sigl.miwa.application.statistics;

import java.util.ArrayList;
import java.util.List;

public class Segmentation {

	private Integer clientNumero;

	private List<CategorieStatistic> categorieStatistics = new ArrayList<CategorieStatistic>();

	public Segmentation(Integer clientNumero,
			List<CategorieStatistic> categorieStatistics) {
		this.clientNumero = clientNumero;
		this.categorieStatistics = categorieStatistics;
	}

	public Integer getClientNumero() {
		return clientNumero;
	}

	public void setClientNumero(Integer clientNumero) {
		this.clientNumero = clientNumero;
	}

	public List<CategorieStatistic> getCategorieStatistics() {
		return categorieStatistics;
	}

	public void setCategorieStatistics(List<CategorieStatistic> categorieStatistics) {
		this.categorieStatistics = categorieStatistics;
	}
}
