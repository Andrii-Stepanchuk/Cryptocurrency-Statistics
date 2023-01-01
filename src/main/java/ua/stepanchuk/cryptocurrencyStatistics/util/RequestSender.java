package ua.stepanchuk.cryptocurrencyStatistics.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * RequestSender is an util class to send requests to other services (like CEX.IO)
 *
 * @author Andrii Stepanchuk
 */

public class RequestSender {

    /**
     * sendHttpPostRequestPriceStatsOnCexIo method sends a post request to the io shop service to get cryptocurrency
     * data statistics for the last 24 hours
     *
     * @param curr1 the first cryptocurrency
     * @param curr2 the second cryptocurrency
     * @return String with data in JSON format
     */

    public static String sendHttpPostRequestPriceStatsOnCexIO(String curr1, String curr2) {
        HttpURLConnection http;

        try {
            URL url = new URL(String.format("https://cex.io/api/price_stats/%s/%s", curr1, curr2));
            http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);

            byte[] out = ("{\"lastHours\":\"24\",\"maxRespArrSize\":\"100\"}").getBytes(UTF_8);

            http.setFixedLengthStreamingMode(out.length);
            http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            http.connect();

            try (OutputStream os = http.getOutputStream()) {
                os.write(out);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return getResponseStringFromHttp(http);
    }

    /**
     * getResponseStringFromHttp method receives data from http and converts to String
     *
     * @param http the first cryptocurrency
     * @return String with data in JSON format
     */

    private static String getResponseStringFromHttp(HttpURLConnection http) {
        String response;

        try (BufferedReader in = new BufferedReader(new InputStreamReader(http.getInputStream()))) {
            response = in.lines().collect(Collectors.joining());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return response;
    }
}
