
package com.crio.warmup.stock.portfolio;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.SECONDS;
import java.io.IOException;
import java.net.URISyntaxException;
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
import java.util.Comparator;
import java.util.List;
import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.springframework.web.client.RestTemplate;

public class PortfolioManagerImpl implements PortfolioManager {


 RestTemplate restTemplate ;

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

  protected String buildUri(String symbol, LocalDate startDate, LocalDate endDate) {
       String uriTemplate = "https:api.tiingo.com/tiingo/daily/$SYMBOL/prices?"
            + "startDate=$STARTDATE&endDate=$ENDDATE&token=$APIKEY";
            return uriTemplate;
  }





  // start from here

  public static String prepareUrl(PortfolioTrade trade, LocalDate endDate, String token) {
    return "https://api.tiingo.com/tiingo/daily/"   + trade.getSymbol() + "/prices?startDate=" + trade.getPurchaseDate() + "&endDate=" + endDate + "&token=" + token;
 }

  public static List<Candle> fetchCandles(PortfolioTrade trade, LocalDate endDate, String token) {
     
    RestTemplate rt = new RestTemplate();
    String Url = prepareUrl(trade, endDate, token);
    TiingoCandle[] tc = rt.getForObject(Url, TiingoCandle[].class);
    return Arrays.asList(tc); 
    //return null;
    }

  @Override
  public List<AnnualizedReturn> calculateAnnualizedReturn(List<PortfolioTrade> portfolioTrades,
      LocalDate endDate) {
        List <AnnualizedReturn> annualList = new ArrayList<>();
        LocalDate localDate = LocalDate.parse(args[1]);
        List <Candle> candle = new ArrayList<Candle>();
        Double Openingprice =0.0;
        Double closingprice =0.0;
       // AnnualizedR
        for(PortfolioTrade t :  portfolioTrades)
        {
        
          candle =  fetchCandles(t, endDate, null);
          
           Openingprice= getOpeningPriceOnStartDate(candle);

           closingprice =getClosingPriceOnEndDate(candle);
           annualList.add(calculateAnnualizedReturns(localDate,t, Openingprice ,closingprice));
           
          
        }
       // Comparator c =Collections.reverseOrder();
        //Collections.sort(annualList,  new  annualListComparator());
        return annualList;
    // TODO Auto-generated method stub
   // return null;
  }
  


}
