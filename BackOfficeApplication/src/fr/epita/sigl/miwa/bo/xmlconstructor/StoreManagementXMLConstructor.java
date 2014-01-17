package fr.epita.sigl.miwa.bo.xmlconstructor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import fr.epita.sigl.miwa.bo.object.Article;
import fr.epita.sigl.miwa.bo.object.ArticleAndLocalPriceAndPromotion;
import fr.epita.sigl.miwa.bo.object.NodeAttribute;
import fr.epita.sigl.miwa.bo.object.RestockRequest;
import fr.epita.sigl.miwa.bo.object.RestockRequestReception;

public class StoreManagementXMLConstructor extends XMLConstructor
{
	/* 
	BO => GC (Récéption réassort)
		numéro de commande : integer (id)
		date & heure de livraison : timestamp
		statut de l'envoi pour le bon de réception : boolean
		commentaire : varchar(128)
		référence de l'article : char(32)
		quantité : integer
	<RECEPTIONREASSORT>
		<NUMEROCOMMANDE>CV398719873</NUMEROCOMMANDE>
		<DATELIVRAISON>20131225</DATELIVRAISON> <!-- AAAAMMJJ -->
		<STATUT>TRUE</STATUT> <!-- OU FALSE -->
		<COMMENTAIRE>Rien à signaler</COMMENTAIRE>
		<ARTICLES>
			<ARTICLE>
				<REFERENCE>AU736827</REFERENCE>
				<QUANTITE>265000</QUANTITE>
				<CATEGORIE>001</CATEGORIE>
			</ARTICLE>
			<ARTICLE>
				<REFERENCE>AU736823</REFERENCE>
				<QUANTITE>12</QUANTITE>
				<CATEGORIE>001</CATEGORIE>
			</ARTICLE>
		</ARTICLES>
	</RECEPTIONREASSORT>
	*/
	public String restockRequestReception(RestockRequestReception restockRequestReception)
	{
		if (restockRequestReception == null)
		{
			return null;
		}

		this.openNode("RECEPTIONREASSORT", null, 0);
		
		this.openNodeWithoutNewLine("NUMEROCOMMANDE", null, 1);
		this.printText(restockRequestReception.orderNumber);
		this.closeNode("NUMEROCOMMANDE", 0);
		
		this.openNodeWithoutNewLine("DATELIVRAISON", null, 1);
		this.printText(new SimpleDateFormat("YYYYMMdd").format(restockRequestReception.deliveryDate));
		this.closeNode("DATELIVRAISON", 0);
		
		this.openNodeWithoutNewLine("STATUT", null, 1);
		this.printText(restockRequestReception.status);
		this.closeNode("STATUT", 0);

		this.openNodeWithoutNewLine("COMMENTAIRE", null, 1);
		this.printText(restockRequestReception.comment);
		this.closeNode("COMMENTAIRE", 0);
		
		this.openNode("ARTICLES", null, 1);
		
		for (Article article : restockRequestReception.articles)
		{
			this.openNode("ARTICLE", null, 2);
			
			this.openNodeWithoutNewLine("REFERENCE", null, 3);
			this.printText(article.reference);
			this.closeNode("REFERENCE", 0);
			
			this.openNodeWithoutNewLine("QUANTITE", null, 3);
			this.printText(article.quantity);
			this.closeNode("QUANTITE", 0);
			
			this.openNodeWithoutNewLine("CATEGORIE", null, 3);
			this.printText(article.category);
			this.closeNode("CATEGORIE", 0);
			
			this.closeNode("ARTICLE", 2);
		}
		
		this.closeNode("ARTICLES", 1);
		
		this.closeNode("RECEPTIONREASSORT", 0);
		
		return this.xml;
	}
	
	/* 
	BO => GC (Demande de réassort)
		numero : CHAR(32)
		refbo : CHAR(32)
		DATEBC : Datetime
		reference : CHAR(32)
		quantite : Numeric(10)
	<REASSORT>
		<NUMERO>CV398719873</NUMERO>
		<REFBO>20131225</REFBO>
		<ADRESSEBO>XXXXXX</ADRESSEBO> 
		<TELBO>0133333333</TELBO>
		<DATEBC>20130427</DATEBC> <!-- AAAAMMJJ -->
		<ARTICLES>
			<ARTICLE>
				<REFERENCE>AU736827</REFERENCE>
				<QUANTITE>265000</QUANTITE>
				<CATEGORIE>001</CATEGORIE>
			</ARTICLE>
			<ARTICLE>
				<REFERENCE>AU736823</REFERENCE>
				<QUANTITE>12</QUANTITE>
				<CATEGORIE>001</CATEGORIE>
			</ARTICLE>
		<ARTICLES>
	</REASSORT>
	*/
	public String restockRequest(RestockRequest restockRequest)
	{
		if (restockRequest == null)
		{
			return null;
		}

		this.openNode("REASSORT", null, 0);
		
		this.openNodeWithoutNewLine("NUMERO", null, 1);
		this.printText(restockRequest.number);
		this.closeNode("NUMERO", 0);
		
		this.openNodeWithoutNewLine("REFBO", null, 1);
		this.printText(restockRequest.backOfficeReference);
		this.closeNode("REFBO", 0);
		
		this.openNodeWithoutNewLine("ADRESSEBO", null, 1);
		this.printText(restockRequest.backOfficeAdresse);
		this.closeNode("ADRESSEBO", 0);
		
		this.openNodeWithoutNewLine("TELBO", null, 1);
		this.printText(restockRequest.backOfficePhone);
		this.closeNode("TELBO", 0);
		
		this.openNodeWithoutNewLine("DATEBC", null, 1);
		this.printText(new SimpleDateFormat("YYYYMMdd").format(restockRequest.date));
		this.closeNode("DATEBC", 0);
		
		this.openNode("ARTICLES", null, 1);
		
		for (Article article : restockRequest.articles)
		{
			this.openNode("ARTICLE", null, 2);
			
			this.openNodeWithoutNewLine("REFERENCE", null, 3);
			this.printText(article.reference);
			this.closeNode("REFERENCE", 0);
			
			this.openNodeWithoutNewLine("QUANTITE", null, 3);
			this.printText(article.quantity);
			this.closeNode("QUANTITE", 0);
			
			this.openNodeWithoutNewLine("CATEGORIE", null, 3);
			this.printText(article.category);
			this.closeNode("CATEGORIE", 0);
			
			this.closeNode("ARTICLE", 2);
		}
		
		this.closeNode("ARTICLES", 1);
		
		this.closeNode("REASSORT", 0);
		
		return this.xml;
	}
}
