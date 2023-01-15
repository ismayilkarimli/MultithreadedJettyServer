import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

public class SumServlet extends HttpServlet {

    private final AtomicLong sum = new AtomicLong();
    private final ConcurrentLinkedQueue<HttpServletResponse> responses = new ConcurrentLinkedQueue<>();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var body = req.getReader().readLine(); // get submitted request value
        resp.setContentType("text/plain");
        synchronized (this) {
            try {
                if ("end".equals(body)) {
                    var result = sum.getAndSet(0); // reset sum to 0
                    while (!responses.isEmpty()) {
                        HttpServletResponse res = responses.poll(); // get and remove response from queue
                        res.getWriter().println(result); // return result to thread's console
                    }
                    resp.getWriter().println(result); // for the last request containing "end"
                    this.notifyAll(); // wake up all waiting threads
                } else {
                    long number = Long.parseLong(body);
                    sum.addAndGet(number); // add number
                    responses.offer(resp); // add response to concurrent queue
                    this.wait(); // current thread waits
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
