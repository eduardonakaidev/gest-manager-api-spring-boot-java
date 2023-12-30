package br.com.nestworld.gestmanagerFinancyProductApi.modules.Store.http.middleware;

public class verifyPriceFormat {
     public static boolean isNumeric(String str) {
        return str.matches("\\d+");
    }

 public static boolean isInteger(String str) {
        return str.matches("\\d+");
    }
}