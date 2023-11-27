
package com.crio.warmup.stock.portfolio;

//import java.util.*;
import com.crio.warmup.stock.dto.AnnualizedReturn;
import com.crio.warmup.stock.dto.PortfolioTrade;
//import com.fasterxml.jackson.core.JsonParser;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.core.io.JsonEOFException;
//import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.List;
//import java.util.ArrayList;
//import java.time.temporal.ChronoUnit;
 //import java.util.Collections;
//import java.util.Comparator;
//import com.crio.warmup.stock.dto.*;
//import org.springframework.web.client.RestTemplate;
//import com.crio.warmup.stock.PortfolioManagerApplication;


public interface PortfolioManager {

 // List<AnnualizedReturn> calculateAnnualizedReturnParallel(
    //  List<PortfolioTrade> portfolioTrades,
     // LocalDate endDate, int numThreads) ;

  //CHECKSTYLE:OFF


  List<AnnualizedReturn> calculateAnnualizedReturn(List<PortfolioTrade> portfolioTrades,
      LocalDate endDate)
    
  ;
}
//public static String getToken(){
//return token;



  //CHECKSTYLE:ON
