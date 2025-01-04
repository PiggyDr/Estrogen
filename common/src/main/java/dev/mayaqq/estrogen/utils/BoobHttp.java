package dev.mayaqq.estrogen.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import dev.mayaqq.estrogen.Estrogen;
import dev.mayaqq.estrogen.registry.items.GenderChangePotionItem;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.UUID;

public class BoobHttp {

    private static final String url = "https://mayaqq.dev/files/boob_people.json";
    private static final ArrayList<UUID> boobPeople = new ArrayList<>();

    public static ArrayList<UUID> getBoobPeople(@Nullable Player player) {
        if (boobPeople.isEmpty()) {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();
            try {
                client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                        .thenApply(HttpResponse::body)
                        .thenAccept(body -> {
                            JsonArray jsonArray = JsonParser.parseString(body).getAsJsonArray();
                            jsonArray.forEach(jsonElement -> boobPeople.add(UUID.fromString(jsonElement.getAsString())));
                            if (player != null) {
                                if (boobPeople.contains(player.getUUID())) {
                                    GenderChangePotionItem.changeGender(player.level(), player, 1);
                                }
                            }
                        });
            } catch (Exception e) {
                Estrogen.LOGGER.error("Failed to fetch boob json: ", e);
            }
        }
        return boobPeople;
    }
}