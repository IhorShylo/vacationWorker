package org.bat2.vacationworker.service.impl;

import org.bat2.vacationworker.client.TrelloClient;
import org.bat2.vacationworker.model.google.VacationRecord;
import org.bat2.vacationworker.model.trello.Card;
import org.bat2.vacationworker.model.trello.Label;
import org.bat2.vacationworker.service.CardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CardServiceImplTest {
    private CardService cardService;

    @Mock
    private TrelloClient trelloClient;

    @BeforeEach
    void SetUp() {
        cardService = new CardServiceImpl(trelloClient);
    }

    @Test
    void getLabelNameTest() throws URISyntaxException, IOException, InterruptedException {
        final String cardId = "cardId";
        final String listId = "listId";
        final String cardName = "[1459] 26: Гришко Василь Михайлович з 20.09.2022 року на 3+1";
        final String labelName = "4 рота";
        final List<Label> labels = List.of(
                new Label("labelId", "boardId", labelName)
        );

        Card card = new Card(listId, cardId, cardName, labels);
        when(trelloClient.getCardById(cardId)).thenReturn(card);

        assertThat(cardService.getLabelName(cardId)).isEqualTo(labelName);
    }

    @Test
    void getLabelNameWhenNoLabelsOnCardTest() throws URISyntaxException, IOException, InterruptedException {
        final String cardId = "cardId";
        final String listId = "listId";
        final String cardName = "[1459] 26: Гришко Василь Михайлович з 20.09.2022 року на 3+1";

        Card card = new Card(listId, cardId, cardName, Collections.emptyList());
        when(trelloClient.getCardById(cardId)).thenReturn(card);

        assertThat(cardService.getLabelName(cardId)).isEmpty();
    }

    @Test
    void getCardApiCallExceptionTest() throws URISyntaxException, IOException, InterruptedException {
        final String cardId = "cardId";
        when(trelloClient.getCardById(cardId)).thenThrow(new RuntimeException());

        assertThat(cardService.getLabelName(cardId)).isEmpty();
    }

    @ParameterizedTest
    @MethodSource("provideDataForParseName")
    void parseNameTest(String cardName, VacationRecord expectedResult) {
        final VacationRecord result = cardService.parseName(cardName);
        assertAll(
                () -> assertEquals(expectedResult.getName(), result.getName()),
                () -> assertEquals(expectedResult.getDays(), result.getDays()),
                () -> assertEquals(expectedResult.getObject(), result.getObject()),
                () -> assertEquals(expectedResult.getUnit(), result.getUnit()),
                () -> assertEquals(expectedResult.getVacNumber(), result.getVacNumber()),
                () -> assertEquals(expectedResult.getStartDate(), result.getStartDate())
        );
    }

    private static Stream<Arguments> provideDataForParseName() {
        return Stream.of(
                Arguments.of("[1459] 26: Гришко Василь Михайлович з 20.09.2022 року на 3+1",
                        new VacationRecord("Гришко Василь Михайлович", 1459,
                                null, 26, "20.09.2022", 4)),
                Arguments.of("[1459 ] 26: Гришко Василь Михайлович з 20.09.2022 року на 3+1",
                        new VacationRecord("Гришко Василь Михайлович", 1459,
                                null, 26, "20.09.2022", 4)),
                Arguments.of("[1459 ]26: Гришко Василь Михайлович з 20.09.2022 року на 3+1",
                        new VacationRecord("Гришко Василь Михайлович", 1459,
                                null, 26, "20.09.2022", 4)),
                Arguments.of(" [1459]26: Гришко Василь Михайлович з 20.09.2022 року на 3+1",
                        new VacationRecord("Гришко Василь Михайлович", 1459,
                                null, 26, "20.09.2022", 4)),
                Arguments.of("[ 1459] 26: Гришко Василь Михайлович з 20.09.2022 року на 3+1",
                        new VacationRecord("Гришко Василь Михайлович", 1459,
                                null, 26, "20.09.2022", 4)),
                Arguments.of("26: Гришко Василь Михайлович з 20.09.2022 року на 3+1",
                        new VacationRecord("Гришко Василь Михайлович", null,
                                null, 26, "20.09.2022", 4)),
                Arguments.of("26:Гришко Василь Михайлович з 20.09.2022 року на 3+1",
                        new VacationRecord("Гришко Василь Михайлович", null,
                                null, 26, "20.09.2022", 4)),
                Arguments.of("[1459] Гришко Василь Михайлович з 20.09.2022 року на 3+1",
                        new VacationRecord("Гришко Василь Михайлович", 1459,
                                null, null, "20.09.2022", 4)),
                Arguments.of("[1459]Гришко Василь Михайлович з 20.09.2022 року на 3+1",
                        new VacationRecord("Гришко Василь Михайлович", 1459,
                                null, null, "20.09.2022", 4)),
                Arguments.of("[1459] 26:Гришко Василь Михайлович  з 20.09.2022 року на 3+1",
                        new VacationRecord("Гришко Василь Михайлович", 1459,
                                null, 26, "20.09.2022", 4)),
                Arguments.of("[1459] 26:Гришко Василь Михайлович з20.09.2022 року на 3+1",
                        new VacationRecord("Гришко Василь Михайлович", 1459,
                                null, 26, "20.09.2022", 4)),
                Arguments.of("[1459] 26:Гришко Василь Михайлович з 20.09.2022року на 3+1",
                        new VacationRecord("Гришко Василь Михайлович", 1459,
                                null, 26, "20.09.2022", 4)),
                Arguments.of("[1459] 26:Гришко Василь Михайлович з 20.09.2022 року на3+1",
                        new VacationRecord("Гришко Василь Михайлович", 1459,
                                null, 26, "20.09.2022", 4)),
                Arguments.of("[1459] 26:Гришко Василь Михайлович з 20.09.2022 року на  3+1",
                        new VacationRecord("Гришко Василь Михайлович", 1459,
                                null, 26, "20.09.2022", 4)),
                Arguments.of("[1459] 26:Гришко Василь Михайлович з 20.09.2022 року на 3+0",
                        new VacationRecord("Гришко Василь Михайлович", 1459,
                                null, 26, "20.09.2022", 3)),
                Arguments.of("[1459] 26:Гришко Василь Михайлович з 20.09.2022 року на 3",
                        new VacationRecord("Гришко Василь Михайлович", 1459,
                                null, 26, "20.09.2022", 3)),
                Arguments.of("[1459] з 20.09.2022 року на 3+1",
                        new VacationRecord(null, 1459,
                                null, null, "20.09.2022", 4)),
                Arguments.of("[1459] 26:Гришко Василь Михайлович з 20 .09.2022 року на 3+1",
                        new VacationRecord("Гришко Василь Михайлович", 1459,
                                null, 26, "20.09.2022", 4)),
                Arguments.of("[1459] 26:Гришко Василь Михайлович з 20/09/2022 року на 3+1",
                        new VacationRecord("Гришко Василь Михайлович", 1459,
                                null, 26, "20/09/2022", 4)),
                Arguments.of("[1459a] 26:Гришко Василь Михайлович з 20.09.2022 року на 3+1",
                        new VacationRecord("Гришко Василь Михайлович", null,
                                null, 26, "20.09.2022", 4)),
                Arguments.of("[1459] 26:Гришко Василь Михайлович з 20.09.2022 на 3+1",
                        new VacationRecord("Гришко Василь Михайлович", 1459,
                                null, 26, "20.09.2022", 4)),
                Arguments.of("[1459] 26:Гришко Василь Михайлович з 20.09.2022р на 3+1",
                        new VacationRecord("Гришко Василь Михайлович", 1459,
                                null, 26, "20.09.2022", 4)),
                Arguments.of("[1459] 26:Гришко Василь Михайлович з 20.09.2022р. на 3+1",
                        new VacationRecord("Гришко Василь Михайлович", 1459,
                                null, 26, "20.09.2022", 4)),
                Arguments.of("[1459] 26:Гришко Василь Михайлович з 20.09.2022 р на 3+1",
                        new VacationRecord("Гришко Василь Михайлович", 1459,
                                null, 26, "20.09.2022", 4))
        );
    }
}