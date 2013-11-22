package org.metaservice.core.deb;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Comparator;
public class DebianVersionComperator implements Comparator<String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DebianVersionComperator.class);

   @Override
   public int compare(String a, String b) {
       // Strip off the epoch and compare it
       String epochA,epochB;
       if(a.contains(":")){
           epochA = a.substring(0,a.indexOf(":"));
           a = a.substring(a.indexOf(":")+1);
       }else{
           epochA = "";
       }
       if(b.contains(":")){
           epochB = b.substring(0, b.indexOf(":"));
           b = b.substring(b.indexOf(":") + 1);
       }else{
           epochB = "";
       }


          // Special case: a zero epoch is the same as no epoch,
           // so remove it.

       if("0".equals(epochA))
           epochA="";
       if("0".equals(epochB))
           epochB="";



       String debVersionA,debVersionB;
       if(a.contains("-")){
           debVersionA = a.substring(a.lastIndexOf("-")+1);
           a = a.substring(0,a.lastIndexOf("-"));
       }else{
           debVersionA = "0";
       }
       if(b.contains("-")){
           debVersionB = b.substring(b.lastIndexOf("-")+1);
           b = b.substring(0,b.lastIndexOf("-"));
       }else{
           debVersionB = "0";
       }


       int res = cmpFragment(epochA,epochB);
       if(res != 0)
       {
           LOGGER.debug("RES " + res);
           return res;
       }
       // Compare the main version
       res = cmpFragment(a,b);
       if(res != 0){
           LOGGER.debug("RES " + res);
           return res;
       }
       res =  cmpFragment(debVersionA,debVersionB);
       LOGGER.debug("RES " + res);
       return res;
   }

    public int cmpFragment(String a, String b){
        LOGGER.debug("CMP " + a + "/" + b);
        if(a.length() == 0 && b.length() == 0){
            return 0;
        }
        //step over '0'
        if(a.startsWith("00"))
            return cmpFragment(a.substring(1),b);
        if(b.startsWith("00"))
            return cmpFragment(a,b.substring(1));

        if (a.length() ==0 ){
            if (b.startsWith("~")){
                return 1;
            }else{
                return -1;
            }
        }
        if (b.length() ==0 ){
            if (a.startsWith("~")){
               return -1;
           }else{
               return 1;
           }
       }

       if(a.charAt(0) == b.charAt(0) && !Character.isDigit(a.charAt(0))){
           return cmpFragment(a.substring(1),b.substring(1));
       }
       if (a.startsWith("~")){
           return -1;
       }
       if (b.startsWith("~")){
           return 1;
       }

       if(!Character.isDigit(a.charAt(0)) || !Character.isDigit(b.charAt(0))){
           if(Character.isDigit(a.charAt(0)) && !Character.isDigit(b.charAt(0))){
               return -1;
           }
           if(Character.isDigit(b.charAt(0)) && !Character.isDigit(a.charAt(0))){
               return 1;
           }
           int vc = order(a.charAt(0));
           int rc = order(b.charAt(0));
           LOGGER.debug("ORDER " + vc +"/" + rc);
           if (vc != rc)
               return vc - rc;
       }


       NumberFormat nf = new DecimalFormat("0");
       if(Character.isDigit(a.charAt(0)) && Character.isDigit(b.charAt(0)))

           {
               ParsePosition positionA = new ParsePosition(0);
               ParsePosition positionB = new ParsePosition(0);
               long la = nf.parse(a,positionA).longValue();
               long lb =  nf.parse(b,positionB).longValue();
               LOGGER.debug("NUMBER " + la + "/" + lb);
               int res = Long.compare(la,lb);
               if(res != 0)
                   return res;
               else
                   return cmpFragment(a.substring(positionA.getIndex()),b.substring(positionB.getIndex()));
           }
       throw new RuntimeException( a + " - " + b);
   }

   private int order(char c) {
       if(Character.isAlphabetic(c)){
               return ((int)c)-'A'-('z'-'A');
       }
       return c;
   }

    private static DebianVersionComperator INSTANCE;
    public static DebianVersionComperator getInstance(){
        if(INSTANCE == null)
            INSTANCE = new DebianVersionComperator();
        return INSTANCE;
    }
}
