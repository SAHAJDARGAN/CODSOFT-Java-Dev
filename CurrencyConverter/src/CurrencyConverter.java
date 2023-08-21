import java.util.Scanner;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import java.io.IOException;


public class CurrencyConverter {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to Currency Converter!");
        System.out.print("Enter the base currency code (e.g., USD, EUR): ");
        String baseCurrency = scanner.nextLine();

        System.out.print("Enter the target currency code: ");
        String targetCurrency = scanner.nextLine();

        System.out.print("Enter the amount to convert: ");
        double amountToConvert = scanner.nextDouble();

        // Fetch exchange rates from an API (replace with actual API code)
        double exchangeRate = fetchExchangeRate(baseCurrency, targetCurrency);

        if (exchangeRate == -1) {
            System.out.println("Failed to fetch exchange rates. Please try again later.");
        } else {
            double convertedAmount = amountToConvert * exchangeRate;
            System.out.println(amountToConvert + " " + baseCurrency + " is equal to " + convertedAmount + " " + targetCurrency);
        }

        scanner.close();
    }

    private static double fetchExchangeRate(String baseCurrency, String targetCurrency) {
        String apiUrl = "https://v6.exchangerate-api.com/v6/f75c8c603b450a06c40dca91/latest/" + baseCurrency;

        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(apiUrl);

        try {
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            String responseBody = EntityUtils.toString(entity);

            JSONObject jsonResponse = new JSONObject(responseBody);

            if (jsonResponse.getString("result").equals("success")) {
                JSONObject conversionRates = jsonResponse.getJSONObject("conversion_rates");

                if (conversionRates.has(targetCurrency)) {
                    double exchangeRate = conversionRates.getDouble(targetCurrency);
                    return exchangeRate;
                } else {
                    System.out.println("Target currency not found in the conversion rates.");
                    return -1; // Indicate failure
                }
            } else {
                System.out.println("Failed to fetch exchange rates. Please try again later.");
                return -1; // Indicate failure
            }
        } catch (IOException e) {
            e.printStackTrace();
            return -1; // Indicate failure
        }
    }


}
