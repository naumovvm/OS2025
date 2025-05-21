package kolokviumska1;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class Server extends Thread {
    private int port;
    private static final AtomicInteger clientCounter = new AtomicInteger(0);
    private static final String counter_file_path;

    static {
        counter_file_path = System.getenv("COUNTER_FILE_PATH");
        if (counter_file_path == null) {
            System.out.println("COUNTER_FILE_PATH ENV VARIABLE NOT SET");
        }
    }

    public Server(int port) {
        this.port = port;
    }

    public int getCount() {
        return clientCounter.get();
    }

    public void incrementCounter() {
        int count = clientCounter.incrementAndGet();
        System.out.println("Incrementing counter from " + count);
        writeToFile(count);
    }

    public void decrementCounter() {
        int count = clientCounter.decrementAndGet();
        System.out.println("Decrementing counter from " + count);
        writeToFile(count);
    }

    // decrement trebit/netrebit? ako ne me mrze

    public void writeToFile(int count) {
        try (RandomAccessFile raf = new RandomAccessFile(counter_file_path, "rw")) {
            raf.setLength(0);
            raf.writeBytes(String.valueOf(count));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void run() {
        System.out.println("Booting server up");
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException exception) {
            exception.printStackTrace();
            return;
        }

        System.out.println("Server has started");
        System.out.println("Awaiting connections");

        while (true) {
            Socket clientSocket = null;

            try {
                clientSocket = serverSocket.accept();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            if (clientSocket != null) {
                System.out.println("A client has connected");
                incrementCounter(); // counter za sekoj vklucen client
                Worker worker = new Worker(clientSocket, this);
                worker.start();
            }
        }
    }

    public static void main(String[] args) {
        Server server = new Server(7301);
        server.start();
    }
}
