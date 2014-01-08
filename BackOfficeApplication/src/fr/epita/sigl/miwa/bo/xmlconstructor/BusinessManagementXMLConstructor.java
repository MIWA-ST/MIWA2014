package fr.epita.sigl.miwa.bo.xmlconstructor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import fr.epita.sigl.miwa.bo.object.Article;
import fr.epita.sigl.miwa.bo.object.ArticleAndLocalPriceAndPromotion;
import fr.epita.sigl.miwa.bo.object.NodeAttribute;
import fr.epita.sigl.miwa.bo.object.RestockRequestReception;

public class BusinessManagementXMLConstructor extends XMLConstructor
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
	public String restockRequest(RestockRequestReception restockRequest)
	{
		if (restockRequest == null)
		{
			return null;
		}

		return this.xml;
	}
}
