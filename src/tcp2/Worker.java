package tcp2;

import java.io.*;
import java.net.Socket;

public class Worker extends Thread {
    private Socket socket;

    public Worker(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        BufferedReader reader = null;
        BufferedWriter writer = null;

        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            while (true) {
                String socketMessage = reader.readLine();

                if (socketMessage.equals("Closing client...")) {
                    System.out.println("Worker - Client has closed the connection");
                    System.out.println("Worker shutting down...");
                    break;
                }

                System.out.println(socketMessage + socket.getInetAddress().getHostAddress() + " " + socket.getPort());
                writer.write("Hello from the worker");
                writer.newLine();
                writer.flush();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
