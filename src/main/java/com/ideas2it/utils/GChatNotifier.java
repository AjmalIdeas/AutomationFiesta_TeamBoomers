package com.ideas2it.utils;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.apache.http.HttpHeaders;

import static io.opentelemetry.semconv.HttpAttributes.HttpRequestMethodValues.POST;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;
import static org.apache.hc.core5.http.HttpStatus.SC_OK;

import static com.ideas2it.utils.Constants.G_CHAT_WEBHOOK_URL;

public class GChatNotifier {

    private GChatNotifier() {
    }

    public static void sendMessage(String message) {
        try {
            URL url = new URL(G_CHAT_WEBHOOK_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod(POST);
            connection.setRequestProperty(HttpHeaders.CONTENT_TYPE, String.valueOf(APPLICATION_JSON));

            String jsonPayload = "{ \"text\": \"" + message + "\" }";

            try (OutputStream outputStream = connection.getOutputStream()) {
                outputStream.write(jsonPayload.getBytes(StandardCharsets.UTF_8));
            }

            int responseCode = connection.getResponseCode();
            if (responseCode != SC_OK) {
                System.err.println("Failed to send message to G-Chat, Response Code: " + responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
