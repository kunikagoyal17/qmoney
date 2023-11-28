

package com.crio.warmup.stock;


//import java.io.File;
//import java.io.IOException;
//import java.io.ObjectInputStream.GetField;
//import java.net.URISyntaxException;
//import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
//import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import com.crio.warmup.stock.dto.*;
//import com.crio.warmup.stock.log.UncaughtExceptionHandler;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
//import java.nio.file.Files;
import java.nio.file.Paths;
//import java.time.LocalDate;
//import java.time.temporal.ChronoUnit;
//import java.util.Arrays;
//import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import com.crio.warmup.stock.log.UncaughtExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.logging.log4j.ThreadContext;
////import org.springframework.asm.TypeReference;
///import java.util.stream.Stream;
//import org.apache.logging.log4j.ThreadContext;
import org.springframework.web.client.RestTemplate;






class annualListComparator implements Comparator <AnnualizedReturn>{
  @Override

  public int compare(AnnualizedReturn t1,AnnualizedReturn t2)
  {
    if(t1.getAnnualizedReturn()<t2.getAnnualizedReturn()) return 0;
    else if(t1.getAnnualizedReturn() < t2.getAnnualizedReturn()) return 1;
    return -1;
  }
 }

public class PortfolioManagerApplication {

  public static String token = "353701880d6d6b6f0bba3ba087885dfa9f669552";
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
  }


  // TODO: CRIO_TASK_MODULE_REST_API
  //  Find out the closing price of each stock on the end_date and return the list
  //  of all symbols in ascending order by its close value on end date.

  // Note:
  // 1. You may have to register on Tiingo to get the api_token.
  // 2. Look at args parameter and the module instructions carefully.
  // 2. You can copy relevant code from #mainReadFile to parse the Json.
  // 3. Use RestTemplate#getForObject in order to call the API,
  //    and deserialize the results in List<Candle>



  private static void printJsonObject(Object object) throws IOException {
    Logger logger = Logger.getLogger(PortfolioManagerApplication.class.getCanonicalName());
    ObjectMapper mapper = new ObjectMapper();
    logger.info(mapper.writeValueAsString(object));
  }
    

  private static File resolveFileFromResources(String filename) throws URISyntaxException {
    return Paths.get(
        Thread.currentThread().getContextClassLoader().getResource(filename).toURI()).toFile();
  }

  private static ObjectMapper getObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    return objectMapper;
  }


  public static List<String> debugOutputs() {

     String valueOfArgument0 = "trades.json";
     String resultOfResolveFilePathArgs0 = "/home/crio-user/workspace/kunikagoyal97-ME_QMONEY_V2/qmoney/bin/main/trades.json";
     String toStringOfObjectMapper = "com.fasterxml.jackson.databind.ObjectMapper@5542c4ed";
     String functionNameFromTestFileInStackTrace = "PortfolioManagerApplicationTest.mainReadFile()";
     String lineNumberFromTestFileInStackTrace = "28";



    return Arrays.asList(new String[]{valueOfArgument0, resultOfResolveFilePathArgs0,
        toStringOfObjectMapper, functionNameFromTestFileInStackTrace,
        lineNumberFromTestFileInStackTrace});
  }


  public static List<String> mainReadQuotes(String[] args) throws IOException, URISyntaxException {
    // return Collections.emptyList();
    LocalDate localDate= LocalDate.parse(args[1]);
    List<PortfolioTrade> trades=readTradesFromJson(args[0]);
    RestTemplate rt=new RestTemplate();
    List<TotalReturnsDto> totalReturnsDtos =new ArrayList<TotalReturnsDto>();
    for(PortfolioTrade trd : trades)
    {
      String url= prepareUrl(trd, localDate, token);
      TiingoCandle[] tiingoCandles = rt.getForObject(url, TiingoCandle[].class);
      totalReturnsDtos.add(new TotalReturnsDto(trd.getSymbol(), tiingoCandles[tiingoCandles.length-1].getClose()));
    }

    Collections.sort(totalReturnsDtos,(a,b)->(int)(a.getClosingPrice()-b.getClosingPrice()));
    List<String> listofSymbolAfterSorting =totalReturnsDtos.stream().map(x->x.getSymbol()).collect(Collectors.toList());
    return listofSymbolAfterSorting;
  
  }

  // TODO:
  //  After refactor, make sure that the tests pass by using these two commands
  //  ./gradlew test --tests PortfolioManagerApplicationTest.readTradesFromJson
  //  ./gradlew test --tests PortfolioManagerApplicationTest.mainReadFile
  public static List<PortfolioTrade> readTradesFromJson(String filename) throws IOException, URISyntaxException {
    // return Collections.empty();
    File file = resolveFileFromResources(filename);
    PortfolioTrade[] portfolioTrades =getObjectMapper().readValue(file,PortfolioTrade[].class);
   List<PortfolioTrade> list = new ArrayList<>();
   for(PortfolioTrade pft  : portfolioTrades)
   {
    list.add(pft);
   }

  return list;
     
  }


  // TODO:
  //  Build the Url using given parameters and use this function in your code to cann the API.
  public static String prepareUrl(PortfolioTrade trade, LocalDate endDate, String token) {
     return "https://api.tiingo.com/tiingo/daily/"   + trade.getSymbol() + "/prices?startDate=" + trade.getPurchaseDate() + "&endDate=" + endDate + "&token=" + token;
  }

      /*  class annualListComparator implements Comparator <AnnualizedReturn>{
        @Override

        public int compare(AnnualizedReturn t1,AnnualizedReturn t2)
        {
          if(t1.getAnnualizedReturn()<t2.getAnnualizedReturn()) return 0;
          else if(t1.getAnnualizedReturn() < t2.getAnnualizedReturn()) return 1;
          return -1;
        }
       }*/



  // TODO: CRIO_TASK_MODULE_CALCULATIONS
  //  Now that you have the list of PortfolioTrade and their data, calculate annualized returns
  //  for the stocks provided in the Json.
  //  Use the function you just wrote #calculateAnnualizedReturns.
  //  Return the list of AnnualizedReturns sorted by annualizedReturns in descending order.

  // Note:
  // 1. You may need to copy relevant code from #mainReadQuotes to parse the Json.
  // 2. Remember to get the latest quotes from Tiingo API.




  // TODO:
  //  Ensure all tests are passing using below command
  //  ./gradlew test --tests ModuleThreeRefactorTest
  static Double getOpeningPriceOnStartDate(List<Candle> candles) {
        return candles.get(0).getOpen();

    
    
  }


  public static Double getClosingPriceOnEndDate(List<Candle> candles) 
  {  
        return candles.get(candles.size()-1).getClose();

                  
    
  }


  public static List<Candle> fetchCandles(PortfolioTrade trade, LocalDate endDate, String token) {
     
    RestTemplate rt = new RestTemplate();
    String Url = prepareUrl(trade, endDate, token);
    TiingoCandle[] tc = rt.getForObject(Url, TiingoCandle[].class);
    return Arrays.asList(tc); 
    //return null;
    }

  public static List<AnnualizedReturn> mainCalculateSingleReturn(String[] args)
      throws IOException, URISyntaxException {
        File f = resolveFileFromResources(args[0]);
        ObjectMapper om = getObjectMapper();
        List <AnnualizedReturn> annualList = new ArrayList<>();
        PortfolioTrade[] trades = om.readValue(f, PortfolioTrade[].class);
       // List<AnnualizedReturn>  = new ArrayList<>();
        LocalDate localDate = LocalDate.parse(args[1]);
        List <Candle> candle = new ArrayList<Candle>();
        Double Openingprice =0.0;
        Double closingprice =0.0;
       // AnnualizedReturn annualizedReturn = new 
        for(PortfolioTrade t :  trades)
        {
        
          candle =  fetchCandles(t, localDate ,token);
          
           Openingprice= getOpeningPriceOnStartDate(candle);

           closingprice =getClosingPriceOnEndDate(candle);
           annualList.add(calculateAnnualizedReturns(localDate, t, Openingprice ,closingprice));
           
          
        }
       // Comparator c =Collections.reverseOrder();
        Collections.sort(annualList,  new  annualListComparator());
        return annualList;
      }


        
        // after this part loop through the trades and use fetch candles method to fetch the list of candles 
        // add in the arraylist of annualisedReturns        
        // if (listOfCandleResponse == 0 ) { continue; }
      // come out of the loop and then use comparator to sort the annualized returns and then return.   
  


  // TODO: CRIO_TASK_MODULE_CALCULATIONS
  //  Return the populated list of AnnualizedReturn for all stocks.
  //  Annualized returns should be calculated in two steps:
  //   1. Calculate totalReturn = (sell_value - buy_value) / buy_value.
  //      1.1 Store the same as totalReturns
  //   2. Calculate extrapolated annualized returns by scaling the same in years span.
  //      The formula is:
  //      annualized_returns = (1 + total_returns) ^ (1 / total_num_years) - 1
  //      2.1 Store the same as annualized_returns
  //  Test the same using below specified command. The build should be successful.
  //     ./gradlew test --tests PortfolioManagerApplicationTest.testCalculateAnnualizedReturn

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

public static String getToken(){
  return token;
}


  public static void main(String[] args) throws Exception {
    Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler());
    ThreadContext.put("runId", UUID.randomUUID().toString());



    printJsonObject(mainCalculateSingleReturn(args));

  }



}

