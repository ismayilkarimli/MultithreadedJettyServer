import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

public class Main {
    public static void main(String[] args) throws Exception {
        // Create a new Jetty server on port 1337
        Server server = new Server(1337);

        // Create a new ServletHandler for handling HTTP requests
        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);

        // Register a new Servlet for handling POST requests
        handler.addServletWithMapping(SumServlet.class.getName(), "/*");

        // Start the server
        server.start();
        server.join();
    }
}

