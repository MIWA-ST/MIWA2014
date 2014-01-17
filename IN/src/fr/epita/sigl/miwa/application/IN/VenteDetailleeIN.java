package fr.epita.sigl.miwa.application.IN;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.epita.sigl.miwa.application.MDM.PromotionArticleMDM;

public class VenteDetailleeIN {
	private List<VenteIN> ventes = new ArrayList<VenteIN>();

	public List<VenteIN> getVentes() {
		return ventes;
	}

	public void setVentes(List<VenteIN> ventes) {
		this.ventes = ventes;
	}
}
