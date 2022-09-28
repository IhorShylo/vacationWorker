package org.bat2.vacationworker.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.bat2.vacationworker.client.TrelloClient;
import org.bat2.vacationworker.ecxeptions.InvalidCardNameException;
import org.bat2.vacationworker.functions.VacationWorkerFunction;
import org.bat2.vacationworker.model.google.VacationRecord;
import org.bat2.vacationworker.model.trello.Card;
import org.bat2.vacationworker.model.trello.Label;
import org.bat2.vacationworker.service.CardService;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Arrays;
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

        Integer vacNumber = getVacNumber(cardName);
        String personName = getPersonName(cardName);
        Integer object = getObject(cardName);
        String startDate = getStartDate(cardName);
        Integer days = getDays(cardName);

        return new VacationRecord(personName, vacNumber, null, object, startDate, days);
    }

    private Integer getDays(String cardName) {
        final int result;
        final String daysRaw = StringUtils.substringAfterLast(cardName, "на");
        logger.info("Raw days string: '" + daysRaw + "'");
        final String daysWithoutWhitespaces = StringUtils.deleteWhitespace(daysRaw);
        logger.info("Days without whitespaces string: '" + daysWithoutWhitespaces + "'");

        if (StringUtils.containsAny(daysWithoutWhitespaces, "+")) {
            logger.info("Days without whitespaces contain +. Start splitting...");
            final String[] split = StringUtils.split(daysWithoutWhitespaces, "+");
            return Arrays.stream(split)
                    .map(Integer::valueOf)
                    .mapToInt(Integer::intValue).sum();
        }

        try {
            result = Integer.parseInt(daysWithoutWhitespaces);
        } catch (NumberFormatException e) {
            logger.warning("Can't parse vacation days '" + daysWithoutWhitespaces + "' to Integer.");
            return null;
        }

        return result;
    }

    private String getStartDate(String cardName) {
        final String dateAndDurationRawStr = StringUtils.substringAfterLast(cardName, "з");

        final String rawDate;
        if (StringUtils.contains(dateAndDurationRawStr, "року")) {
            rawDate = StringUtils.substringBeforeLast(dateAndDurationRawStr, "року");
        } else if (StringUtils.contains(dateAndDurationRawStr, "р")) {
            rawDate = StringUtils.substringBeforeLast(dateAndDurationRawStr, "р");
        } else if (StringUtils.contains(dateAndDurationRawStr, "р.")) {
            rawDate = StringUtils.substringBeforeLast(dateAndDurationRawStr, "р.");
        } else {
            rawDate = StringUtils.substringBeforeLast(dateAndDurationRawStr, "на");
        }

        return StringUtils.deleteWhitespace(rawDate);
    }

    private Integer getObject(String cardName) {
        final int result;
        final String objectStr;
        if (StringUtils.contains(cardName, "]") && StringUtils.contains(cardName, ":")) {
            objectStr = StringUtils.substringBetween(cardName, "]", ":");
        } else {

            objectStr = StringUtils.substringBefore(cardName, ":");
        }
        try {
            result = Integer.parseInt(StringUtils.deleteWhitespace(objectStr));
        } catch (NumberFormatException e) {
            logger.warning("Can't parse object '" + objectStr + "' to Integer.");
            return null;
        }
        return result;
    }

    private Integer getVacNumber(String cardName) {
        final int result;
        final String vacNumberStr = StringUtils.substringBetween(cardName, "[", "]");
        try {
            result = Integer.parseInt(StringUtils.deleteWhitespace(vacNumberStr));
        } catch (NumberFormatException e) {
            logger.warning("Can't parse vacation number '" + vacNumberStr + "' to Integer.");
            return null;
        }
        return result;
    }

    private String getPersonName(String cardName) {
        String nameRaw = null;
        if (StringUtils.contains(cardName, " з") && StringUtils.contains(cardName, ":")) {
            logger.info("Card name contain ':' & ' з'");
            nameRaw = StringUtils.substringBetween(cardName, ":", " з");
        } else if (StringUtils.contains(cardName, " з") &&
                !StringUtils.contains(cardName, ":") &&
                StringUtils.contains(cardName, "]")
        ) {
            logger.info("Card name contain ']' & ':' & ' з'");
            nameRaw = StringUtils.substringBetween(cardName, "]", " з");
        } else if (StringUtils.contains(cardName, " з") &&
                !StringUtils.contains(cardName, ":") &&
                !StringUtils.contains(cardName, "]")
        ) {
            logger.info("Card name contain only ' з'");
            nameRaw = StringUtils.substringBeforeLast(cardName, " з");
        }
        logger.info("Person name after substring: '" + nameRaw + "'");

        final String result = StringUtils.strip(nameRaw);

        logger.info("Person name after strip(): '" + result + "'");
        if (StringUtils.isBlank(result)) {
            logger.warning("Can't parse person name from card: '" + cardName + "'");
            return null;
        }
        return result;
    }
}
