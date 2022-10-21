package org.bat2.vacationworker.client.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bat2.vacationworker.client.TrelloClient;
import org.bat2.vacationworker.model.trello.Card;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TrelloClientImpl implements TrelloClient {

    private final HttpClient client;
    private static final String BASE_URI = "https://api.trello.com";
    private static final String API_KEY = "d2dfccf7084e0a6b400742de00baa8d0";
    @Value("${sm://trello-token}")
    private String TOKEN;

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
