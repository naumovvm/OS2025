package LASTWORD;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class Server extends Thread {
    private int port;
    private static final String log_file_path;
    private static final AtomicInteger counter = new AtomicInteger(0);

    static {
        log_file_path = System.getenv("WORDS_FILE");
        if (log_file_path == null) {
            System.out.println("ERROR GETTING ENV VARIABLE");
        }
    }

    public int getCount(){
        return counter.get();
    }

    public int incrementCounter(){
        return counter.incrementAndGet();
    }

    public String getLog_file_path() {
        return log_file_path;
    }

    public Server(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        System.out.println("Starting server");
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(7391);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("Server is online");

        while (true) {
            Socket clientSocket = null;

            try {
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

            if (clientSocket != null) {
                System.out.println("Client connected");
                System.out.println("Starting worker");
                Worker worker = new Worker(clientSocket, this);
                worker.start();
            }
        }
    }
    public static void main(String[] args) {
        Server server = new Server(7391);
        server.start();
    }
}
