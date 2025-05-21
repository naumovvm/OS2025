package LASTWORD;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Worker extends Thread {
    private Socket socket;
    private Server server;
    private static List<String> words = new ArrayList<String>();
    private static Lock lock = new ReentrantLock();

    public Worker(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        BufferedReader reader = null;
        BufferedWriter writer = null;
        BufferedWriter logger = null;

        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            logger = new BufferedWriter(new FileWriter(server.getLog_file_path()));

            while (true) {
                String message = reader.readLine();
                if (message != null) {
                    if (message.equals("HANDSHAKE")) {
                        writer.write("Logged in " + socket.getInetAddress().getHostAddress());
                        writer.newLine();
                        writer.flush();
                    } else if (message.equals("STOP")) {
                        writer.write("Count: " + server.getCount()); // dodadi count na zborovi
                        writer.newLine();
                        writer.flush();

                        writer.write("LOGGED OUT");
                        writer.newLine();
                        writer.flush();

                        System.out.println("Worker closing...");

                        writer.close();
                        reader.close();
                        socket.close();
                        break;
                    } else {
                        if (!words.contains(message)) {
                            lock.lock();
                            writer.write("NEMA " + message);
                            writer.newLine();
                            writer.flush();

                            logger.write(message + " " + LocalDateTime.now().toString() + " " + socket.getInetAddress().getHostAddress());
                            logger.newLine();
                            logger.flush();

                            server.incrementCounter();

                            words.add(message);
                            lock.unlock();
                        } else {
                            writer.write("IMA " + message);
                            writer.newLine();
                            writer.flush();
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
