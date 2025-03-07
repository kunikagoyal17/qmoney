
package com.crio.warmup.stock.portfolio;

///import static java.time.temporal.ChronoUnit.DAYS;
//import static java.time.temporal.ChronoUnit.SECONDS;
import java.io.IOException;
import java.util.*;
import java.net.URISyntaxException;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.SECONDS;

import com.crio.warmup.stock.dto.AnnualizedReturn;
import com.crio.warmup.stock.dto.Candle;
import com.crio.warmup.stock.dto.PortfolioTrade;
import com.crio.warmup.stock.dto.TiingoCandle;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
//import java.util.Comparator;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.springframework.web.client.RestTemplate;













/*class annualListComparator implements Comparator <AnnualizedReturn>{
  @Override

  public int compare(AnnualizedReturn t1,AnnualizedReturn t2)
  {
    if(t1.getAnnualizedReturn()< t2.getAnnualizedReturn()) return 0;
    else if(t1.getAnnualizedReturn() < t2.getAnnualizedReturn()) return 1;
    return -1;
  }
 }*/


public class PortfolioManagerImpl implements PortfolioManager {


 //public PortfolioManagerImpl(RestTemplate restTemplate2) {}



RestTemplate restTemplate ;
private CharSequence[] args;



//public class PortfolioManagerImpl implements PortfolioManager {




  // Caution: Do not delete or modify the constructor, or else your build will break!
  // This is absolutely necessary for backward compatibility
  protected PortfolioManagerImpl(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }
  
 /*  public static List<Candle> fetchCandles(PortfolioTrade trade, LocalDate endDate, String token) {
     
    RestTemplate rt = new RestTemplate();
    String Url = prepareUrl(trade, endDate, token);
    TiingoCandle[] tc = rt.getForObject(Url, TiingoCandle[].class);
    return Arrays.asList(tc); 
    //return null;
    }*/



  public static Double getClosingPriceOnEndDate(List<Candle> candles) 
  {  
        return candles.get(candles.size()-1).getClose();

  }
        static Double getOpeningPriceOnStartDate(List<Candle> candles) {
          return candles.get(0).getOpen();
        }
  //TODO: CRIO_TASK_MODULE_REFACTOR
  // 1. Now we want to convert our code into a module, so we will not call it from main anymore.
  //    Copy your code from Module#3 PortfolioManagerApplication#calculateAnnualizedReturn
  //    into #calculateAnnualizedReturn function here and ensure it follows the method signature.
  // 2. Logic to read Json file and convert them into Objects will not be required further as our
  //    clients will take care of it, going forward.

  // Note:
  // Make sure to exercise the tests inside PortfolioManagerTest using command below:
  // ./gradlew test --tests PortfolioManagerTest

  //CHECKSTYLE:OFF
  public static AnnualizedReturn calculateAnnualizedReturns(LocalDate endDate,
  PortfolioTrade trade, Double buyPrice, Double sellPrice) {
    double totalReturn =0.0;
    totalReturn =(sellPrice - buyPrice) / buyPrice ;
     // double startdate=(long)(trade.getPurchaseDate());
    double total_num_years =ChronoUnit.DAYS.between(trade.getPurchaseDate(),endDate)/365.24;
  //u  LocalDate buying_date=trade.getPurchaseDate();

 // double total_num_years =(endDatbuying_date);
  // Double annualizedReturn =0.0;
  //annualizedReturn ar;
  
   double annualizedreturn = Math.pow ((1+totalReturn),(1/total_num_years))-1;
  // Double db =new Double(double value);
  AnnualizedReturn annualized_Return = new AnnualizedReturn(trade.getSymbol(),annualizedreturn,totalReturn);
  

  return  annualized_Return;

}


/*public static String getToken(){
  return token;
}*/

  

  //CHECKSTYLE:OFF

  // TODO: CRIO_TASK_MODULE_REFACTOR
  //  Extract the logic to call Tiingo third-party APIs to a separate function.
  //  Remember to fill out the buildUri function and use that.


  public List<Candle> getStockQuote(String symbol, LocalDate from, LocalDate to)
      throws JsonProcessingException {
        String tiingoRestURL =buildUri(symbol ,from ,to);
        TiingoCandle[] tiingoCandleArray =restTemplate.getForObject(tiingoRestURL, TiingoCandle[].class);
     if(tiingoCandleArray==null)
     {
      return new ArrayList<Candle>();
     }
     else{
   return Arrays.stream(tiingoCandleArray).collect(Collectors.toList());
     }
        
      
     
  }

  /* protected String buildUri(String symbol, LocalDate startDate, LocalDate endDate) {
       String uriTemplate = "https://api.tiingo.com/tiingo/daily/$SYMBOL/prices?"
            + "startDate=$STARTDATE&EendDate=$ENDDATE&token=$APIKEY";
        String url =uriTemplate.replace("$SYMBOL", symbol).replace("$STARTDATE", startDate.toString()).replace("$ENDDATE", endDate.toString());
           // replaceAll($SYMBOL)
         // String  url = uriTemplate.replace("$SYMBOL" ,symbol).rep("$STARTDATE",startDate);


            return url;
  } */
  protected String buildUri(String symbol, LocalDate startDate, LocalDate endDate) {
    
    String token = "353701880d6d6b6f0bba3ba087885dfa9f669552";
    String uri = "https://api.tiingo.com/tiingo/daily/$SYMBOL/prices?"
        + "startDate=$STARTDATE&endDate=$ENDDATE&token=$APIKEY";
    return uri.replace("$APIKEY", token).replace("$SYMBOL", symbol)
        .replace("$STARTDATE", startDate.toString())
        .replace("$ENDDATE", endDate.toString());   


  }





  //private Comparator<AnnualizedReturn> getComparator() {
   // return Comparator.comparing(AnnualizedReturn::getAnnualizedReturn).reversed();
  //}




  // start from here


  /*private static   String token = "353701880d6d6b6f0bba3ba087885dfa9f669552";
  public static List<String> mainReadFile(String[] args) throws IOException, URISyntaxException {
     File file =resolveFileFromResources( args[0]);
     ObjectMapper ob =getObjectMapper();
     PortfolioTrade [] trades = ob.readValue(file, PortfolioTrade[].class);
    List<String> list=new ArrayList<String>();
     for(PortfolioTrade trade : trades )
     {
     list.add(trade.getSymbol());
    }

     return list;
  }*/
  
  

  //public static String getToken(){
  //  return token;
  //}

  public static String prepareUrl(PortfolioTrade trade, LocalDate endDate, String token) {
    return "https://api.tiingo.com/tiingo/daily/"   + trade.getSymbol() + "/prices?startDate=" + trade.getPurchaseDate() + "&endDate=" + endDate + "&token=" + token;
 }

  public static List<Candle> fetchCandles(PortfolioTrade trade, LocalDate endDate, String token) {
     
    RestTemplate rt = new RestTemplate();
    String Url = prepareUrl(trade, endDate, token);
    TiingoCandle[] tc = rt.getForObject(Url, TiingoCandle[].class);
    return Arrays.asList(tc); 

    }

  @Override
  public List<AnnualizedReturn> calculateAnnualizedReturn(List<PortfolioTrade> portfolioTrades,
      LocalDate endDate) {
        List <AnnualizedReturn> annualList = new ArrayList<AnnualizedReturn>();
       // int args;
      //  LocalDate localDate = LocalDate.parse(args[1]);
       LocalDate localDate = endDate;
        List <Candle> candles = new ArrayList<Candle>();
        Double Openingprice =0.0;
        Double closingprice =0.0;
       // AnnualizedR
        for(PortfolioTrade t :  portfolioTrades)
        {
        
          try {
            candles =  getStockQuote(t.getSymbol(), t.getPurchaseDate(),localDate );
          } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
          
           Openingprice= getOpeningPriceOnStartDate(candles);

           closingprice =getClosingPriceOnEndDate(candles);
           annualList.add(calculateAnnualizedReturns(localDate,t, Openingprice ,closingprice));
           
          
        }
           //  Collections.sort.reverseOrder(annualList);
      Collections.sort(annualList,  new  annualListComparator());
        return annualList;
    // TODO Auto-generated method stub
   // return null;
  }
  


}
