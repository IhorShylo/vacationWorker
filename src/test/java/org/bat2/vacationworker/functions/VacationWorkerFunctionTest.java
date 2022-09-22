package org.bat2.vacationworker.functions;

import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.*;
import java.net.HttpURLConnection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VacationWorkerFunctionTest {

    @Mock
    private HttpRequest request;
    @Mock
    private HttpResponse response;
    private VacationWorkerFunction vacationWorkerFunction;
    private BufferedWriter writerOut;
    private StringWriter responseOut;

    @BeforeEach
    void setUp() throws IOException {
        vacationWorkerFunction = new VacationWorkerFunction();
        responseOut = new StringWriter();
        writerOut = new BufferedWriter(responseOut);
        when(response.getWriter()).thenReturn(writerOut);
    }

    @Test
    void successFunctionExecutionTest() throws Exception {
        FileReader requestIn = new FileReader("src/test/resources/req/moveCardToTargetListReq.json");
        BufferedReader readerIn = new BufferedReader(requestIn);
        when(request.getReader()).thenReturn(readerIn);
        when(request.getMethod()).thenReturn("POST");

        vacationWorkerFunction.service(request, response);
        writerOut.flush();
        String cardName = "[1459] 26: Гришко Василь Михайлович з 20.09.2022 року на 3+1";


        verify(response, times(1)).setStatusCode(HttpURLConnection.HTTP_OK);
        assertThat(responseOut.toString()).contains("Card: " + cardName + "saved successfully");
    }

    @ParameterizedTest
    @ValueSource(strings = {"GET", "DELETE", "PUT", "HEAD", "OPTIONS", "InvalidString"})
    void unsupportedMethodRequestTest(String method) throws Exception {
        FileReader requestIn = new FileReader("src/test/resources/req/moveCardToTargetListReq.json");
        BufferedReader readerIn = new BufferedReader(requestIn);
        when(request.getReader()).thenReturn(readerIn);
        when(request.getMethod()).thenReturn(method);

        vacationWorkerFunction.service(request, response);
        writerOut.flush();
        final String body = request.getReader().lines().collect(Collectors.joining());

        verify(response, times(1)).setStatusCode(HttpURLConnection.HTTP_BAD_REQUEST);
        assertThat(responseOut.toString()).contains("Invalid request. Request method: " + method + "; Request body: " + body);
    }

    @Test
    void emptyRequestBodyTest() throws Exception {
        final String method = "POST";
        final String body = "";
        StringReader requestIn = new StringReader(body);
        BufferedReader readerIn = new BufferedReader(requestIn);
        when(request.getReader()).thenReturn(readerIn);
        when(request.getMethod()).thenReturn(method);

        vacationWorkerFunction.service(request, response);
        writerOut.flush();

        verify(response, times(1)).setStatusCode(HttpURLConnection.HTTP_BAD_REQUEST);
        assertThat(responseOut.toString()).contains("Invalid request. Request method: " + method + "; Request body: " + body);
    }

    @ParameterizedTest
    @MethodSource("provideDataForUnsupportedActions")
    void unsupportedTrelloActionTest(String filePath, String type, String translationKey, String listAfterId) throws IOException {
        FileReader requestIn = new FileReader(filePath);
        BufferedReader readerIn = new BufferedReader(requestIn);
        when(request.getReader()).thenReturn(readerIn);
        final String method = "POST";
        when(request.getMethod()).thenReturn(method);

        vacationWorkerFunction.service(request, response);
        writerOut.flush();

        verify(response, times(1)).setStatusCode(HttpURLConnection.HTTP_UNSUPPORTED_TYPE);
        assertThat(responseOut.toString()).contains("Unsupported trello action. Processing ignored. Action type: " + type +
                ", translation key: " + translationKey + ", listAfterId: " + listAfterId);

    }

    private static Stream<Arguments> provideDataForUnsupportedActions() {
        return Stream.of(
                Arguments.of("src/test/resources/req/markDueDateReq.json", "updateCard",
                        "action_marked_the_due_date_complete", null),
                Arguments.of("src/test/resources/req/moveCardFromTargetListReq.json", "updateCard",
                        "action_move_card_from_list_to_list", "62f227668555a62731adef72"),
                Arguments.of("src/test/resources/req/moveCardHigherReq.json", "updateCard",
                        "action_moved_card_higher", null),
                Arguments.of("src/test/resources/req/renameCardReq.json", "updateCard",
                        "action_renamed_card", null)
        );
    }
}