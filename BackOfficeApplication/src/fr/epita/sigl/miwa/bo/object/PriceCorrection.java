package fr.epita.sigl.miwa.bo.object;

import java.util.Date;

public class PriceCorrection
{
	public Article article = null;
	public Date date = null;
	public Date dateAndTimeStart = null;
	public Date dateAndTimeEnd = null;

	public void print()
	{
		System.out.println("==========PRICE=CORRECTION=BEGIN==========");
		System.out.println("==========PRICE=CORRECTION=>ARTICLE=BEGIN==========");
		if (this.article != null)
		{
			this.article.print();
		}
		System.out.println("==========PRICE=CORRECTION=>ARTICLE=END==========");
		if (this.date != null)
		{
			System.out.println("DATE:" + this.date.toString());			
		}
		else
		{
			System.out.println("DATE:");
		}
		if (this.dateAndTimeStart != null)
		{
			System.out.println("DATE AND TIME START:" + this.dateAndTimeStart.toString());			
		}
		else
		{
			System.out.println("DATE AND TIME START:");
		}
		if (this.dateAndTimeEnd != null)
		{
			System.out.println("DATE AND TIME END:" + this.dateAndTimeEnd.toString());			
		}
		else
		{
			System.out.println("DATE AND TIME END:");
		}
		System.out.println("==========PRICE=CORRECTION=END==========");
	}
}
