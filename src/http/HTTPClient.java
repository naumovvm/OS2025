package http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HTTPClient {
    public static void main(String[] args) {
        String serverAddress = "localhost";
        int serverPort = 8080;
        String name = "Mihail";
        String surname = "Naumov";

        try {
            String url = "http://" + serverAddress + ":" + serverPort + "/?name=" + name + "&surname=" + surname;
            URL serverURL = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) serverURL.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = "";

            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            connection.disconnect();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
