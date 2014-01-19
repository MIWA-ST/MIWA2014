package fr.epita.sigl.miwa.application.statistics;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Segmentation {
	
	private Date dateTime;

	private Integer clientNumero;

	private List<CategorieStatistic> categorieStatistics = new ArrayList<CategorieStatistic>();

	public Segmentation(Date dateTime, Integer clientNumero,
			List<CategorieStatistic> categorieStatistics) {
		this.dateTime = dateTime;
		this.clientNumero = clientNumero;
		this.categorieStatistics = categorieStatistics;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
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
