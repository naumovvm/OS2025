package tcpChatroom;

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
        BufferedReader reader=null;
        BufferedReader userInput=null;
        BufferedWriter writer=null;
        BufferedWriter logger=null;

        try {
            socket = new Socket(InetAddress.getByName("194.149.135.49"), 9753);

            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            logger = new BufferedWriter(new FileWriter("src/tcpChatroom/chatlog233194.txt", true));
            userInput = new BufferedReader(new InputStreamReader(System.in));

//            String loginMsg = "login:233194";
//            writer.write(loginMsg);
//            writer.newLine();
//            writer.flush();
//
//            logger.write(loginMsg + "\n");
//            logger.flush();
//
//            String response = reader.readLine();
//            System.out.println(response);
//            logger.write(response + "\n");
//            logger.flush();
//
//            if (response == null || !response.toLowerCase().contains("success")) {
//                System.out.println("Login has failed");
//                return;
//            }

            // Ako ne e failed, togash isprakjame hello poraka
            String helloMsg = "hello:233194";
            writer.write(helloMsg);
            writer.newLine();
            writer.flush();

            logger.write(helloMsg + "\n");
            logger.flush();

            String helloResponse = reader.readLine();
            System.out.println(helloResponse);
            logger.write(helloResponse + "\n");
            logger.flush();

            // Kreirame loop za da dopishuvame poraki
            System.out.println("You can now send messages, if you want to exit the chatroom just type /quit");

            while (true) {
                if (reader.ready()) {
                    String serverMsg = reader.readLine();
                    if (serverMsg != null) {
                        System.out.println(serverMsg);
                        logger.write(serverMsg + "\n");
                        logger.flush();
                    }
                }

                if (userInput.ready()) {
                    String userMsg = userInput.readLine();

                    if (userMsg.equals("/quit")) {
                        System.out.println("Quitting...");
                        break;
                    }

                    writer.write(userMsg);
                    writer.newLine();
                    writer.flush();

                    logger.write(userMsg + "\n");
                    logger.flush();
                }
            }
            writer.write("Closing the connection now...");
            writer.newLine();
            writer.flush();
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public static void main(String[] args) {
        Client client = new Client(9753);
        client.start();
    }
}