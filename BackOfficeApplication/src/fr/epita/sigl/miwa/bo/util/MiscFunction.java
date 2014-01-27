package fr.epita.sigl.miwa.bo.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import fr.epita.sigl.miwa.bo.db.Mapper;
import fr.epita.sigl.miwa.bo.object.Article;
import fr.epita.sigl.miwa.bo.object.RestockRequest;

public class MiscFunction {
	public static RestockRequest getArticlesForRestockRequest()
	{
		int limit = 50;
		String quantityasked = "20";
	
		RestockRequest rr = new RestockRequest();
		rr.date = new Date();
		
		ResultSet res = Mapper.get("id_article,reference,name_article,category_article,quantity,provider_price,sales_price,ean,description", "article", "true");
		
		if (res != null)
		{
			try {
				
				Article ar1 = new Article();
				
				ar1.reference = res.getString(2);
				ar1.name = res.getString(3);
				ar1.category = res.getString(4);
				ar1.quantity = res.getString(5);
				ar1.providerPrice = res.getString(6);
				ar1.salesPrice = res.getString(7);
				ar1.ean = res.getString(8);
				ar1.description = res.getString(9);

				try{
					Integer.parseInt(ar1.quantity);
				} catch (Exception e){
					ar1.quantity = "0";
				}
				
				if (Integer.parseInt(ar1.quantity) < limit){
					ar1.quantity = quantityasked;
					rr.articles.add(ar1);
				}
					
				while(res.next())
				{
					Article ar2 = new Article();

					ar2.reference = res.getString(2);
					ar2.name = res.getString(3);
					ar2.category = res.getString(4);
					ar2.quantity = res.getString(5);
					ar2.providerPrice = res.getString(6);
					ar2.salesPrice = res.getString(7);
					ar2.ean = res.getString(8);
					ar2.description = res.getString(9);
				
					try{
						Integer.parseInt(ar2.quantity);
					} catch (Exception e){
						ar2.quantity = "0";
					}
					
					if (Integer.parseInt(ar2.quantity) < limit){
						ar2.quantity = quantityasked;
						rr.articles.add(ar2);
					}
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return rr;
	}
}
