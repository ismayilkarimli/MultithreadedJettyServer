import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SumServletTest {

    @Test
    public void doPost_whenEnd_returnsSum() throws Exception {
        SumServlet servlet = new SumServlet();
        ExecutorService executor = Executors.newCachedThreadPool();

        Future<Integer> future1 = executor.submit(() -> {
            HttpServletRequest request1 = mock(HttpServletRequest.class);
            HttpServletResponse response1 = mock(HttpServletResponse.class);
            when(request1.getReader()).thenReturn(new BufferedReader(new StringReader("1")));
            PrintWriter writer = new PrintWriter(new StringWriter());
            when(response1.getWriter()).thenReturn(writer);
            servlet.doPost(request1, response1);
            return 1;
        });
        Future<Integer> future2 = executor.submit(() -> {
            HttpServletRequest request2 = mock(HttpServletRequest.class);
            HttpServletResponse response2 = mock(HttpServletResponse.class);
            when(request2.getReader()).thenReturn(new BufferedReader(new StringReader("2")));
            PrintWriter writer = new PrintWriter(new StringWriter());
            when(response2.getWriter()).thenReturn(writer);
            servlet.doPost(request2, response2);
            return 2;
        });
        Future<Integer> future3 = executor.submit(() -> {
            HttpServletRequest request3 = mock(HttpServletRequest.class);
            HttpServletResponse response3 = mock(HttpServletResponse.class);
            when(request3.getReader()).thenReturn(new BufferedReader(new StringReader("end")));
            PrintWriter writer = new PrintWriter(new StringWriter());
            when(response3.getWriter()).thenReturn(writer);
            servlet.doPost(request3, response3);
            return 0;
        });
        long result = future1.get() + future2.get() + future3.get();
        assertEquals(3, result);
    }

}
