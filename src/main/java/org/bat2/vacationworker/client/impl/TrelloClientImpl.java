package org.bat2.vacationworker.client.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bat2.vacationworker.client.TrelloClient;
import org.bat2.vacationworker.model.trello.Card;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TrelloClientImpl implements TrelloClient {

    private final HttpClient client;
    public static final String BASE_URI = "https://api.trello.com";
    public static final String API_KEY = "d2dfccf7084e0a6b400742de00baa8d0";
    public static final String TOKEN = "9ecab8efd4017e18da8b1a348fe432c1edad668f409cc25a8dcbf5f16a34ae8d";

    public TrelloClientImpl(HttpClient client) {
        this.client = client;
    }

    @Override
    public Card getCardById(String cardId) throws URISyntaxException, IOException, InterruptedException {
        final String path = "/1/cards/" + cardId + "?key=" + API_KEY + "&token=" + TOKEN;
        final HttpRequest request = HttpRequest
                .newBuilder(new URI(BASE_URI + path))
                .GET()
                .build();

        final HttpResponse<Stream<String>> response = client.send(request, HttpResponse.BodyHandlers.ofLines());
        final String body = response.body().collect(Collectors.joining());
        ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(body, Card.class);
    }

}
