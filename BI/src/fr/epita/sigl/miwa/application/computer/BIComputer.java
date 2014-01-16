/***********************************************************************
 * Module:  BIComputer.java
 * Author:  Elodie NGUYEN THANH NHAN, Laura OLLIVIER & Chloé VASSEUR
 * Purpose: Defines the Class BIComputer
 ***********************************************************************/
package fr.epita.sigl.miwa.application.computer;

import java.util.*;
import fr.epita.sigl.miwa.application.criteres.*;
import fr.epita.sigl.miwa.application.data.*;
import fr.epita.sigl.miwa.application.statistics.*;

public class BIComputer {
   /** @param stocks */
   public List<StockStatistic> computeStockStatistics(List<Stock> stocks) {
      // TODO: implement
      return null;
   }
   
   /** @param stocks */
   public List<SaleStatistic> computeSaleStatistics(List<Sale> stocks) {
      // TODO: implement
      return null;
   }
   
   /** @param clients 
    * @param detailSales */
   public List<FidelityStatistic> computeFidelityStatistic(List<Client> clients, List<DetailSale> detailSales) {
      // TODO: implement
      return null;
   }
   
   /** @param clients 
    * @param detailSales 
    * @param criteria */
   public List<Segmentation> computeSegmentation(List<Client> clients, List<DetailSale> detailSales, List<Critere> critere) {
      // TODO: implement
      return null;
   }
   
   /** @param detailSales */
   public List<PaiementStatistic> computePaymentStatistics(List<DetailSale> detailSales) {
      // TODO: implement
      return null;
   }
}