package fr.epita.sigl.miwa.bo.object;

import java.util.ArrayList;
import java.util.List;


public class DeliveryList
{
	public List<Delivery> deliveries = null;
	
	public DeliveryList()
	{
		this.deliveries = new ArrayList<Delivery>();
	}
	
	public void print()
	{
		System.out.println("==========DELIVERY=LIST=BEGIN==========");
		System.out.println("==========DELIVERY=LIST=>DELIVERIES=BEGIN==========");
		for (Delivery delivery : this.deliveries)
		{
			delivery.print();
		}
		System.out.println("==========DELIVERY=LIST=>DELIVERIES=END==========");
		System.out.println("==========DELIVERY=LIST=END==========");
	}
}
