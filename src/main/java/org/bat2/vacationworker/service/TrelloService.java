package org.bat2.vacationworker.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class TrelloService {

    private final WebClient webClient;
    private final String API_KEY = "d2dfccf7084e0a6b400742de00baa8d0";
    private final String TOKEN = "9ecab8efd4017e18da8b1a348fe432c1edad668f409cc25a8dcbf5f16a34ae8d";

    private final String VACATION_BOARD_ID = "62f227668555a62731adef6a";

    public TrelloService(WebClient.Builder builder) {
        webClient = builder.baseUrl("https://api.trello.com/").build();
    }

    public String getBoards() {
        return webClient
                .get()
                .uri("1/members/me/boards?key={yourKey}&token={yourToken}", API_KEY, TOKEN)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

}
