package fr.epita.sigl.miwa.application;

import java.util.List;
import java.util.logging.Logger;

import fr.epita.sigl.miwa.application.clock.ClockClient;
import fr.epita.sigl.miwa.application.messaging.AsyncMessageListener;
import fr.epita.sigl.miwa.application.messaging.SyncMessHandler;
import fr.epita.sigl.miwa.bo.parser.DomParserCashRegister;
import fr.epita.sigl.miwa.bo.parser.DomParserHelper;
import fr.epita.sigl.miwa.st.Conf;
import fr.epita.sigl.miwa.st.EApplication;
import fr.epita.sigl.miwa.st.async.file.exception.AsyncFileException;
import fr.epita.sigl.miwa.st.async.message.AsyncMessageFactory;
import fr.epita.sigl.miwa.st.async.message.exception.AsyncMessageException;
import fr.epita.sigl.miwa.st.sync.SyncMessFactory;

public class Main {
	private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

	public static void main(String[] args) throws AsyncFileException,
			AsyncMessageException {
		/* ST DO NOT REMOVE/MODIFY OR PUT ANYTHING ABOVE */
		Conf.getInstance();
		SyncMessFactory.initSyncMessReceiver();	
		AsyncMessageFactory.getInstance().getAsyncMessageManager()
				.initListener(new AsyncMessageListener());
		/* !ST DO NOT REMOVE/MODIFY OR PUT ANYTHING ABOVE */
		/* CODE HERE */
		
		//SyncMessHandler.getSyncMessSender().sendMessage(EApplication.GESTION_COMMERCIALE, "coucou");
		
		
		String xml = 
			"<ENTETE objet=\"tickets-vente\" source=\"caisse\" date=\"2013-12-18\"/>" +
			"<VENTES>" +
				"<VENTE client=\"Alex\" montanttotal=\"2000\" moyenpaiement=\"carte\" dateheure=\"2013-12-22 19:30:23\">" +
					"<ARTICLE refarticle=\"AZ3245\" quantite=\"14\" prix=\"34\" />" +
					"<ARTICLE refarticle=\"RE3423\" quantite=\"34\" prix=\"67\" />" +
				"</VENTE>" +
			"</VENTES>";
	
		;

		DomParserCashRegister qpcr = new DomParserCashRegister(xml);
		qpcr.salesTicket();

//		try {
//			 
//			Document doc = DomParserHelper.getDocumentFromXMLString(
//				"<company>" +
//					"<staff id=\"1001\">" +
//						"<firstname>yong</firstname>" +
//						"<lastname>mook kim</lastname>" +
//						"<nickname>mkyong</nickname>" +
//						"<salary>100000</salary>" +
//					"</staff>" +
//					"<staff id=\"2001\">" +
//						"<firstname>low</firstname>" +
//						"<lastname>yin fong</lastname>" +
//						"<nickname>fong fong</nickname>" +
//						"<salary>200000</salary>" +
//					"</staff>" +
//				"</company>"
//			);
//		 
//			//optional, but recommended
//			//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
//			doc.getDocumentElement().normalize();
//			
//			
//		 
//			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
//		 
//			NodeList nList = doc.getElementsByTagName("staff");
//		 
//			System.out.println("----------------------------");
//		 
//			for (int temp = 0; temp < nList.getLength(); temp++) {
//		 
//				Node nNode = nList.item(temp);
//		 
//				System.out.println("\nCurrent Element :" + nNode.getNodeName());
//		 
//				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
//		 
//					Element eElement = (Element) nNode;
//		 
//					System.out.println("Staff id : " + eElement.getAttribute("id"));
//					System.out.println("First Name : " + eElement.getElementsByTagName("firstname").item(0).getTextContent());
//					System.out.println("Last Name : " + eElement.getElementsByTagName("lastname").item(0).getTextContent());
//					System.out.println("Nick Name : " + eElement.getElementsByTagName("nickname").item(0).getTextContent());
//					System.out.println("Salary : " + eElement.getElementsByTagName("salary").item(0).getTextContent());
//				}
//			}
//		    } catch (Exception e) {
//		    	e.printStackTrace();
//		    }
		
		/* !CODE HERE */
		/* ST DO NOT REMOVE/MODIFY OR PUT ANYTHING BELOW */
		AsyncMessageFactory.getInstance().getAsyncMessageManager()
				.stopListener();
		/* !ST DO NOT REMOVE/MODIFY OR PUT ANYTHING BELOW */
	}

}
