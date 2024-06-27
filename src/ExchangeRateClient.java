package com.example;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ExchangeRateClient {

    private static final String API_URL = "https://v6.exchangerate-api.com/v6/5632287956c5215350691118/latest/USD";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        JsonObject rates = null;

        try {
            String jsonResponse = sendHttpRequest(API_URL);
            if (jsonResponse != null) {
                rates = processJsonResponse(jsonResponse);
            } else {
                System.out.println("Error: No se pudo obtener una respuesta válida de la API.");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        int option = 0;
        while (option != 9) {
            System.out.println("""
                    ** Escriba el número de la opción deseada **
                    1 - Consultar tasa de cambio USD a ARS
                    2 - Consultar tasa de cambio USD a BRL
                    3 - Consultar tasa de cambio USD a COP
                    4 - Convertir monto de USD a ARS
                    5 - Convertir monto de USD a BRL
                    6 - Convertir monto de USD a COP
                    9 - Salir
                    """);

            option = scanner.nextInt();
            switch (option) {
                case 1:
                    System.out.println("USD to ARS: " + rates.get("ARS").getAsDouble());
                    break;
                case 2:
                    System.out.println("USD to BRL: " + rates.get("BRL").getAsDouble());
                    break;
                case 3:
                    System.out.println("USD to COP: " + rates.get("COP").getAsDouble());
                    break;
                case 4:
                    System.out.println("Ingrese el monto en USD a convertir a ARS:");
                    double amountARS = scanner.nextDouble();
                    double convertedARS = convertCurrency(amountARS, "ARS", rates);
                    System.out.println("Monto convertido: " + convertedARS + " ARS");
                    break;
                case 5:
                    System.out.println("Ingrese el monto en USD a convertir a BRL:");
                    double amountBRL = scanner.nextDouble();
                    double convertedBRL = convertCurrency(amountBRL, "BRL", rates);
                    System.out.println("Monto convertido: " + convertedBRL + " BRL");
                    break;
                case 6:
                    System.out.println("Ingrese el monto en USD a convertir a COP:");
                    double amountCOP = scanner.nextDouble();
                    double convertedCOP = convertCurrency(amountCOP, "COP", rates);
                    System.out.println("Monto convertido: " + convertedCOP + " COP");
                    break;
                case 9:
                    System.out.println("Finalizando el programa. Muchas gracias por usar nuestros servicios.");
                    break;
                default:
                    System.out.println("Opción inválida");
                    break;
            }
        }
        scanner.close();
    }

    private static String sendHttpRequest(String url) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return response.body();
        } else {
            System.out.println("Error: " + response.statusCode());
            return null;
        }
    }

    private static JsonObject processJsonResponse(String jsonResponse) {
        System.out.println("Complete Response: " + jsonResponse);
        JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
        JsonObject rates = jsonObject.getAsJsonObject("conversion_rates");
        return rates;
    }

    private static double convertCurrency(double amount, String currency, JsonObject rates) {
        double convertedAmount;
        switch (currency) {
            case "ARS":
                convertedAmount = amount * rates.get("ARS").getAsDouble();
                break;
            case "BRL":
                convertedAmount = amount * rates.get("BRL").getAsDouble();
                break;
            case "COP":
                convertedAmount = amount * rates.get("COP").getAsDouble();
                break;
            default:
                convertedAmount = -1; // Moneda no válida
                break;
        }
        return convertedAmount;
    }
}
