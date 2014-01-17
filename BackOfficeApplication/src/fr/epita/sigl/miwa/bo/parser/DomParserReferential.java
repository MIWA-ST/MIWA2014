package fr.epita.sigl.miwa.bo.parser;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;

import fr.epita.sigl.miwa.bo.object.Article;
import fr.epita.sigl.miwa.bo.object.ArticleList;
import fr.epita.sigl.miwa.bo.object.Promotion;
import fr.epita.sigl.miwa.bo.util.Convert;

public class DomParserReferential extends DomParser
{
	public DomParserReferential()
	{
	}
	
	/* LOG : ***** [Nombre d'article] articles reçu
	Référentiel => BO, BI, Internet
		objet : VARCHAR(256) -> liste-article
		source : VARCHAR(20) -> mdm
		date : date (AAAA-MM-JJ)
		reference : CHAR(32)
		ean : CHAR(13)
		categorie : CHAR(32)
		prix_fournisseur : Numeric(2)
		prix_vente : Numeric(2)
		debut : date (AAAA-MM-JJ)
		fin : date (AAAA-MM-JJ)
		percent : integer
		DESCRIPTION : VARCHAR(2048)
	<XML>
		<ENTETE objet="" source="" date=""/>
		<ARTICLES>
			<ARTICLE reference="" ean="" categorie="" prix_fournisseur="" prix_vente="">
				<DESCRIPTION><![CDATA[<Texte multi lignes de description du produit.>]]> </DESCRIPTION>
				<PROMOTIONS>
					<PROMOTION debut="" fin="" pourcent="" />
				</PROMOTIONS>
			</ARTICLE>
		</ARTICLES>
	</XML>
	*/
	public ArticleList articleList(String xml)
	{
		ArticleList articleList = new ArticleList();
		
		this.setXml(xml);
		this.updateDoc();
		
		Node headerNode = this.doc.getFirstChild().getFirstChild();
		
		articleList.date = Convert.stringToDate(DomParserHelper.getNodeAttr("date", headerNode), "AAAA-MM-JJ");
		
		List<Node> articleNodes = DomParserHelper.getNodes("ARTICLE", this.doc.getFirstChild().getFirstChild().getNextSibling());
		
		for (Node articleNode : articleNodes)
		{
			Article article = new Article();
			
			article.reference = DomParserHelper.getNodeAttr("reference", articleNode);
			article.ean = DomParserHelper.getNodeAttr("ean", articleNode);
			article.category = DomParserHelper.getNodeAttr("categorie", articleNode);
			article.providerPrice = DomParserHelper.getNodeAttr("prix_fournisseur", articleNode);
			article.salesPrice = DomParserHelper.getNodeAttr("prix_vente", articleNode);
			article.description = DomParserHelper.getNode("DESCRIPTION", articleNode).getTextContent();
			
			List<Node> promotionNodes = DomParserHelper.getNodes("PROMOTION", DomParserHelper.getNode("PROMOTIONS", articleNode));
		
			for (Node promotionNode : promotionNodes)
			{
				Promotion promotion = new Promotion();
				
				promotion.beginDate = Convert.stringToDate(DomParserHelper.getNodeAttr("debut", promotionNode), "AAAA-MM-JJ");
				promotion.endDate = Convert.stringToDate(DomParserHelper.getNodeAttr("fin", promotionNode), "AAAA-MM-JJ");
				promotion.percent = DomParserHelper.getNodeAttr("pourcent", promotionNode);
				
				article.promotions.add(promotion);
			}
			
			articleList.articles.add(article);
		}
		
		return articleList;
	}
}
