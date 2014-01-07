package fr.epita.sigl.miwa.bo.object;

import java.util.Date;
import java.util.List;

public class SalesTicket
{
	public Date date = null; // AAAA-MM-JJ
	public List<Sale> sales = null;

	public void print()
	{
		System.out.println("==========SALE=TICKET=BEGIN==========");
		if (this.date != null)
		{
			System.out.println("DATE:" + this.date.toString());
		}
		else
		{
			System.out.println("DATE:");
		}
		System.out.println("==========SALE=TICKET=>SALES=BEGIN==========");
		if (this.sales != null)
		{
			for (Sale sale : this.sales)
			{
				sale.print();
			}
		}
		System.out.println("==========SALE=TICKET=>SALES=END==========");
		System.out.println("==========SALE=TICKET=END==========");
	}
}
