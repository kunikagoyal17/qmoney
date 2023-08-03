
package com.crio.warmup.stock.dto;

//import java.time.LocalDate;

public class AnnualizedReturn {

  private final String symbol;
  private final Double annualizedReturn;
  private final Double totalReturns;

  public AnnualizedReturn(String symbol, Double annualizedReturn, Double totalReturns) {
    this.symbol = symbol;
    this.annualizedReturn = annualizedReturn;
    this.totalReturns = totalReturns;
  }

 // public  AnnualizedReturn(LocalDate localDate, PortfolioTrade[] trades, Double openingprice,
     // Double closingprice) {
       //  return null ;
       // }
         

public String getSymbol() {
    return symbol;
  }

  public Double getAnnualizedReturn() {
    return annualizedReturn;
  }

  public Double getTotalReturns() {
    return totalReturns;
  }
}
