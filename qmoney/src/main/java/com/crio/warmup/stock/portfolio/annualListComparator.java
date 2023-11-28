package com.crio.warmup.stock.portfolio;

import java.util.Comparator;
import com.crio.warmup.stock.dto.AnnualizedReturn;

//public class annualListComparator {
    class annualListComparator implements Comparator <AnnualizedReturn>{
        @Override
      
        public int compare(AnnualizedReturn t1,AnnualizedReturn t2)
        {
          if(t1.getAnnualizedReturn()<t2.getAnnualizedReturn()) return 0;
          else if(t1.getAnnualizedReturn() < t2.getAnnualizedReturn()) return 1;
          return -1;
        }
       }
      


