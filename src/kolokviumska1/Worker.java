package kolokviumska1;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.time.LocalDateTime;

public class Worker extends Thread {
    private Socket socket;
    private Server server;

    public Worker(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        BufferedReader reader = null;
        BufferedWriter writer = null;

        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            int sum = 0;

            while (true) {
                String message = reader.readLine();

                if (message.equals("LOGIN")) {
                    System.out.println("Client has logged in");
                    writer.write("Logged in " + socket.getInetAddress().getHostAddress());
                    writer.newLine();
                    writer.flush();

                    writer.write("There are currently " + server.getCount() + " users logged on.");
                    writer.newLine();
                    writer.flush();
                } else if (message.equals("LOGOUT")) {
                    System.out.println("Logging out");
                    break;
                } else if (message.equals("END")) {
                    writer.write("TOTAL: " + sum);
                    writer.newLine();
                    writer.flush();
                    sum = 0;
                } else {
                    int number = Integer.parseInt(message);
                    sum += number;
                    writer.write(number + " " + LocalDateTime.now() + " " + InetAddress.getLocalHost().getHostAddress());
                    writer.newLine();
                    writer.flush();
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            server.decrementCounter();
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
