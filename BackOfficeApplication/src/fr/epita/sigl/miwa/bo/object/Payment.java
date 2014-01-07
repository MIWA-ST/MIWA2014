package fr.epita.sigl.miwa.bo.object;

public class Payment
{
	public String type = null;
	public String amount = null;
	
	public void print()
	{
		System.out.println("==========PAYMENT=BEGIN==========");
		System.out.println("TYPE:" + this.type);
		System.out.println("AMOUNT:" + this.amount);
		System.out.println("==========PAYMENT=END==========");
	}
}
