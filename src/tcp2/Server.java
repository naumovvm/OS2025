package tcp2;

import tcp.Worker;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {
    public int port;

    public Server(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("Starting server...");
        try {
            sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Server has started!");
        System.out.println("Awaiting connections..");

        while(true){
            Socket clientSocket = null;
            try{
                clientSocket=serverSocket.accept();
            }catch(IOException e){
                System.out.println(e.getMessage());
            }

            System.out.println();
            System.out.println("A client has connected!");
            System.out.println("Booting up worker...");

            Worker worker = new Worker(clientSocket);
            worker.start();
        }
    }

    public static void main(String[] args) {
        Server server = new Server(1809);
        server.start();
    }
}
