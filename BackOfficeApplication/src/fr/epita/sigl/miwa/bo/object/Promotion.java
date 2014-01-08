package fr.epita.sigl.miwa.bo.object;

import java.util.Date;

public class Promotion
{
	public Date beginDate = null;
	public Date endDate = null;
	public String percent = null;
	
	public void print()
	{
		System.out.println("==========PROMOTION=BEGIN==========");
		
		if (this.beginDate != null)
		{
			System.out.println("BEGIN DATE:" + this.beginDate.toString());			
		}
		else
		{
			System.out.println("BEGIN DATE:");
		}
		if (this.endDate != null)
		{
			System.out.println("END DATE:" + this.endDate.toString());			
		}
		else
		{
			System.out.println("END DATE:");
		}
		System.out.println("PERCENT:" + this.percent);
		System.out.println("==========PROMOTION=END==========");
	}
}
