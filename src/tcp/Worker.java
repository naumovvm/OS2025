package tcp;

import java.io.*;
import java.net.Socket;

public class Worker extends Thread {
    private Socket socket;

    public Worker(Socket socket) {
        this.socket = socket; // go prezema soketot od mestoto kade sho e povikan
    }

    @Override
    public void run() {
        BufferedReader reader = null;
        BufferedWriter writer = null;
        try {
            reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));

            while (true) {
                // cita poraka od soketot, ako ima
                String msg = reader.readLine();

                if (msg.equals("Closing the connection now...")) {
                    System.out.println("Client has closed the connection...");
                    System.out.println("Worker shutting down");
                    break;
                }

                System.out.println(msg + ", with address: " + socket.getInetAddress().getHostAddress() + " and port: " + socket.getPort());

                // Vrakjame povratna poraka do klientot
                writer.write("Hello back from the server (sent from the worker)");
                writer.newLine();
                writer.flush();
            }
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }
}
