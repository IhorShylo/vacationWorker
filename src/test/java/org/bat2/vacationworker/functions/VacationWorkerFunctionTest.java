package org.bat2.vacationworker.functions;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@RunWith(JUnit4.class)
public class VacationWorkerFunctionTest {

    @Mock
    private HttpRequest request;
    @Mock
    private HttpResponse response;

    private BufferedWriter writerOut;
    private StringWriter responseOut;

    @Before
    public void beforeTest() throws IOException {
        MockitoAnnotations.initMocks(this);

        responseOut = new StringWriter();
        writerOut = new BufferedWriter(responseOut);
        when(response.getWriter()).thenReturn(writerOut);
    }

    @Test
    public void helloWorldTest() throws Exception {
        new VacationWorkerFunction().service(request, response);

        writerOut.flush();
        assertThat(responseOut.toString()).contains("Hello World!");
    }
}