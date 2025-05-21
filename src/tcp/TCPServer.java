package tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer extends Thread {
    public int port;

    public TCPServer(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        System.out.println("Starting the server now...");
        ServerSocket serverSocket;

        try{
            serverSocket=new ServerSocket(port);
        }catch(IOException exc){
            // ako e zafatena portata
            System.out.println(exc.getMessage());
            return;
        }

        System.out.println("Server has started...");
        System.out.println("Waiting for connections...");

        while (true) {
            Socket socketToClient = null;
            try{
                socketToClient=serverSocket.accept();
            }catch(IOException exc){
                System.out.println(exc.getMessage());
            }

            System.out.println();
            System.out.println("New client connected...");
            System.out.println("Starting worker thread...");
//            System.out.println();

            Worker worker = new Worker(socketToClient);
            worker.start();
        }
    }

    public static void main(String[] args) {
        TCPServer tcpServer = new TCPServer(1280);
        tcpServer.start();
    }
}
