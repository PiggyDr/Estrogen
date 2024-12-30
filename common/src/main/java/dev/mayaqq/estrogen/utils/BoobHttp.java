package dev.mayaqq.estrogen.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import dev.mayaqq.estrogen.Estrogen;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.UUID;

public class BoobHttp {

    private static final String url = "https://mayaqq.dev/files/boob_people.json";
    private static final ArrayList<UUID> boobPeople = new ArrayList<>();

    public static ArrayList<UUID> getBoobPeople() {
        if (boobPeople.isEmpty()) {
            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                HttpGet request = new HttpGet(url);
                try (CloseableHttpResponse response = httpClient.execute(request)) {

                    JsonArray jsonArray = JsonParser.parseReader(new InputStreamReader(response.getEntity().getContent())).getAsJsonArray();

                    jsonArray.forEach(jsonElement -> boobPeople.add(UUID.fromString(jsonElement.getAsString())));
                }
            } catch (Exception e) {
                Estrogen.LOGGER.warn("Failed to fetch exclusive people from remote server: {}", e.getMessage());
            }
        }
        return boobPeople;
    }
}
