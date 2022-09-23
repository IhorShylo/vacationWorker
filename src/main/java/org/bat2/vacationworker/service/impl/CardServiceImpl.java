package org.bat2.vacationworker.service.impl;

import org.bat2.vacationworker.client.TrelloClient;
import org.bat2.vacationworker.ecxeptions.InvalidCardNameException;
import org.bat2.vacationworker.functions.VacationWorkerFunction;
import org.bat2.vacationworker.model.google.VacationRecord;
import org.bat2.vacationworker.model.trello.Card;
import org.bat2.vacationworker.model.trello.Label;
import org.bat2.vacationworker.service.CardService;

import java.util.logging.Logger;

public class CardServiceImpl implements CardService {
    private static final Logger logger = Logger.getLogger(VacationWorkerFunction.class.getName());
    private final TrelloClient trelloClient;

    public CardServiceImpl(TrelloClient trelloClient) {
        this.trelloClient = trelloClient;
    }

    @Override
    public String getLabelName(String cardId) {
        final Card cardById;
        try {
            cardById = trelloClient.getCardById(cardId);
        } catch (Exception e) {
            logger.warning("Can't get response from Trello get Card API. Return empty string label name");
            return "";
        }
        return cardById
                .getLabels()
                .stream()
                .findFirst()
                .map(Label::getName)
                .orElse("");
    }

    @Override
    public VacationRecord parseName(String cardName) {
        if (cardName == null || cardName.isBlank()) {
            throw new InvalidCardNameException("Invalid card name: " + cardName);
        }

        String name = null;
        Integer vacNumber = null;
        String unit = null;
        Integer object = null;
        String startDate = null;
        Integer days = null;

        //TODO implement parsing logic

        return new VacationRecord(name, vacNumber, unit, object, startDate, days);
    }
}
