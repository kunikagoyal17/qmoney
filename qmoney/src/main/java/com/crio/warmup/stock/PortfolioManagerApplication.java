
package com.crio.warmup.stock;


import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import com.crio.warmup.stock.dto.PortfolioTrade;
import com.crio.warmup.stock.dto.TiingoCandle;
import com.crio.warmup.stock.dto.TotalReturnsDto;
import com.crio.warmup.stock.log.UncaughtExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.asm.TypeReference;
import org.springframework.web.client.RestTemplate;


public class PortfolioManagerApplication {

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
    final String token = "353701880d6d6b6f0bba3ba087885dfa9f669552";
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

  public static void main(String[] args) throws Exception {
    Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler());
    ThreadContext.put("runId", UUID.randomUUID().toString());

    printJsonObject(mainReadFile(args));
    printJsonObject(mainReadQuotes(args));


  }
}

