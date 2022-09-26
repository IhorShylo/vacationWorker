package org.bat2.vacationworker.client;

import org.bat2.vacationworker.model.trello.Card;

import java.io.IOException;
import java.net.URISyntaxException;

public interface TrelloClient {

    Card getCardById(String cardId) throws URISyntaxException, IOException, InterruptedException;

}
