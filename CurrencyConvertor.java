import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class CurrencyConverter {
    private static final String API_KEY = "YOUR_API_KEY"; 
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        
        System.out.print("Enter the base currency (e.g., USD): ");
        String baseCurrency = scanner.next().toUpperCase();

        System.out.print("Enter the target currency (e.g., EUR): ");
        String targetCurrency = scanner.next().toUpperCase();

        
        System.out.print("Enter the amount you want to convert: ");
        double amount = scanner.nextDouble();

        
        try {
            double exchangeRate = fetchExchangeRate(baseCurrency, targetCurrency);
            if (exchangeRate != -1) {
                double convertedAmount = amount * exchangeRate;
                System.out.printf("Converted Amount: %.2f %s\n", convertedAmount, targetCurrency);
            } else {
                System.out.println("Sorry, couldn't fetch the exchange rate.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }

        scanner.close();
    }

   
    private static double fetchExchangeRate(String baseCurrency, String targetCurrency) throws Exception {
        String urlString = API_URL + baseCurrency; // API URL with the base currency

        
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == 200) { // Successful response
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            
            String responseBody = response.toString();
            String searchKey = "\"" + targetCurrency + "\":";
            int index = responseBody.indexOf(searchKey);
            if (index != -1) {
                int start = index + searchKey.length();
                int end = responseBody.indexOf(",", start);
                String rateString = responseBody.substring(start, end);
                return Double.parseDouble(rateString);
            } else {
                System.out.println("Currency not found in the response.");
            }
        } else {
            System.out.println("Failed to get response from the API.");
        }
        
        return -1; // Return -1 if there was an error
    }
}
