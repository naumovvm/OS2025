package http;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HTTPServer {
    public static void main(String[] args) {
        int port = 8080;

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server has started on port " + port);

            while(true){
                Socket clientSocket = serverSocket.accept();
                WorkingThread worker = new WorkingThread(clientSocket);
                worker.start();
            }
        }catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
