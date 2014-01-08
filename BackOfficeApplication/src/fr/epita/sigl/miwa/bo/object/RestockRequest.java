package fr.epita.sigl.miwa.bo.object;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RestockRequest
{
	public String number = null;
	public String backOfficeReference = null;
	public String backOfficeAdresse = null;
	public String backOfficePhone = null;
	public Date date = null; // AAAAMMJJ
	public List<Article> articles = null;
	
	public RestockRequest()
	{
		this.articles = new ArrayList<Article>();
	}
	public void print()
	{
		System.out.println("==========RESTOCK=REQUEST=BEGIN==========");
		System.out.println("NUMBER:" + this.number);
		System.out.println("BACK OFFICE REFERENCE:" + this.backOfficeReference);
		System.out.println("BACK OFFICE ADRESSE:" + this.backOfficeAdresse);
		System.out.println("BACK OFFICE REFERENCE:" + this.backOfficePhone);
		if (this.date != null)
		{
			System.out.println("DATE:" + this.date.toString());			
		}
		else
		{
			System.out.println("DATE:");
		}
		System.out.println("==========RESTOCK=REQUEST=>ARTICLES=BEGIN==========");
		for (Article article : this.articles)
		{
			article.print();
		}
		System.out.println("==========RESTOCK=REQUEST=>ARTICLES=END==========");
		System.out.println("==========RESTOCK=REQUEST=END==========");
	}
}
