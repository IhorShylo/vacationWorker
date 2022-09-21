package org.bat2.vacationworker.functions;

import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.HttpURLConnection;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VacationWorkerFunctionTest {

    @Mock
    private HttpRequest request;
    @Mock
    private HttpResponse response;

    private static VacationWorkerFunction vacationWorkerFunction;

    @BeforeAll
    static void beforeTest() {
        vacationWorkerFunction = new VacationWorkerFunction();


    }

    @Test
    void successFunctionExecutionTest() throws Exception {
        FileReader requestIn = new FileReader("src/test/resources/req/moveCardToTargetListReq.json");
        BufferedReader readerIn = new BufferedReader(requestIn);
        when(request.getReader()).thenReturn(readerIn);
        when(request.getMethod()).thenReturn("POST");

        vacationWorkerFunction.service(request, response);

        verify(response, times(1)).setStatusCode(HttpURLConnection.HTTP_OK);
    }
}