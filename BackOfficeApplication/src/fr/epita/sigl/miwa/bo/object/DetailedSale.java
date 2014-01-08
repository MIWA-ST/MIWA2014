package fr.epita.sigl.miwa.bo.object;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DetailedSale
{
	public Date date = null;
	public String location = null;
	public List<Sale> sales = null;
	
	public DetailedSale()
	{
		this.sales = new ArrayList<Sale>();
	}
	
	public void print()
	{
		System.out.println("==========DETAILED=SALE=BEGIN==========");
		if (this.date != null)
		{
			System.out.println("DATE:" + this.date.toString());			
		}
		else
		{
			System.out.println("DATE:");
		}
		System.out.println("LOCATION:" + this.location);
		System.out.println("==========DETAILED=SALE=>SALES=BEGIN==========");
		for (Sale sale : this.sales)
		{
			sale.print();
		}
		System.out.println("==========DETAILED=SALE=>SALES=END==========");
		System.out.println("==========DETAILED=SALE=END==========");
	}
}
