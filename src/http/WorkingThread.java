package http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class WorkingThread extends Thread {
    private final Socket clientSocket;

    public WorkingThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter printer = new PrintWriter(clientSocket.getOutputStream(), true);

            String request = reader.readLine();
            Map<String, String> result = parseUrl(request);
            String name = result.getOrDefault("name", "default");
            String surname = result.getOrDefault("surname", "default");

            /*
                We intend to return the HTTP response that contains
                the HTML page displaying the student data that
                each client passes to the server.
             */

            printer.println("HTTP/1.1 200 OK");
            printer.println("Content-Type: text/html");
            printer.println();
            printer.println("<html><head><title>Hello</title></head><body>");
            printer.println("<h1>Hello, " + name + " " + surname + "!</h1>");
            printer.println("</body></html>");

            clientSocket.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    //    Method that parses the request url into a map of all its query parameters
    private Map<String, String> parseUrl(String url) {
        Map<String, String> map = new HashMap<>();
        if (!url.contains("?")) return map;

        String[] parts = url.split("\\s+");
        String query = parts[1].split("\\?")[1];
        String[] params = query.split("&");

        for (String param : params) {
            String[] keyValue = param.split("=");
            map.put(keyValue[0], keyValue[1]);
        }
        return map;
    }
}
