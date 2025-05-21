package kolokviumska1;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client extends Thread {
    String filePath;
    int serverPort;

    public Client(String filePath, int serverPort) {
        this.filePath = filePath;
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
            socket = new Socket(InetAddress.getByName("127.0.0.1"), serverPort);

            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            logger = new BufferedWriter(new FileWriter(filePath));
            userInput = new BufferedReader(new InputStreamReader(System.in));

            writer.write("LOGIN");
            writer.newLine();
            writer.flush();

            String response = reader.readLine();
            System.out.println(response);

            String num_of_logins = reader.readLine(); // se inkrementira otkako ke se prikluci klientot
            System.out.println(num_of_logins);

            while (true) {
                System.out.println("Enter a number");
                String message = userInput.readLine();

                if (message == null) {
                    System.out.println("Enter a number");
                    break;
                }

                writer.write(message);
                writer.newLine();
                writer.flush();

                String serverResponse = reader.readLine();
                logger.write(serverResponse);
                logger.newLine();
                logger.flush();

                if (message.equals("LOGOUT")) {
                    writer.write(message);
                    writer.newLine();
                    writer.flush();
                    break;
                }

                if (message.equals("END")) {
                    System.out.println(serverResponse);
                    break;
                }
            }
        } catch (IOException exc) {
            System.out.println(exc.getMessage());
        }
    }

    public static void main(String[] args) {
        Client client = new Client("src/kolokviumska1/logs.txt", 7301);
        client.start();
    }
}
