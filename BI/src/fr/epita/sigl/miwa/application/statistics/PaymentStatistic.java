package fr.epita.sigl.miwa.application.statistics;

import java.util.Date;

import fr.epita.sigl.miwa.application.enums.EPaiementType;

public class PaymentStatistic {
	
	private Date dateTime;
	
	private EPaiementType type;

	private int ca;

	private float caPourcent;

	public PaymentStatistic(Date dateTime, EPaiementType type, int ca,
			float caPourcent) {
		this.dateTime = dateTime;
		this.type = type;
		this.ca = ca;
		this.caPourcent = caPourcent;
	}

	public Date getDateTime() {
		return dateTime;
	}



	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}



	public EPaiementType getType() {
		return type;
	}



	public void setType(EPaiementType type) {
		this.type = type;
	}



	public int getCa() {
		return ca;
	}



	public void setCa(int ca) {
		this.ca = ca;
	}



	public float getCaPourcent() {
		return caPourcent;
	}



	public void setCaPourcent(float caPourcent) {
		this.caPourcent = caPourcent;
	}

	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder();
		builder.append(type);
		builder.append(" : ");
		builder.append(ca);
		builder.append(" € (");
		builder.append(caPourcent);
		builder.append(" %)");
		return builder.toString();
	}
}
