package LASTWORD;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client extends Thread {
    private int serverPort;

    public Client(int serverPort) {
        this.serverPort = serverPort;
    }

    @Override
    public void run() {
        Socket socket = null;
        BufferedReader reader = null;
        BufferedWriter writer = null;
        BufferedWriter logger = null;
        BufferedReader userInput = null;

        try {
            socket = new Socket(InetAddress.getByName("127.0.0.1"), 7391);

            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            userInput = new BufferedReader(new InputStreamReader(System.in));

            writer.write("HANDSHAKE");
            writer.newLine();
            writer.flush();

            String response = reader.readLine();
            System.out.println(response);

            if (response.startsWith("Logged in")) {
                while (true) {
                    System.out.println("Enter a message: ");
                    String message = userInput.readLine();

                    writer.write(message);
                    writer.newLine();
                    writer.flush();

                    response = reader.readLine();
                    System.out.println(response);
                    if (message.equals("STOP")) {
                        response = reader.readLine();
                        System.out.println(response);
                        break;
                    }
                }
            }
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }finally{
            try {
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) {
        Client client = new Client(7391);
        client.start();
    }
}
