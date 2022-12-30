package ua.stepanchuk.ToDoApp.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

public class RequestSender {

    public static String sendHttpPostRequest(String curr1, String curr2) {
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
