package tcp2;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client extends Thread {
    private int serverPort;
    Scanner input = new Scanner(System.in);

    public Client(int serverPort) {
        this.serverPort = serverPort;
    }

    @Override
    public void run() {
        Socket socket = null;
        BufferedReader reader = null;
        BufferedWriter writer = null;
        BufferedWriter logger = null;

        try {
            socket = new Socket(InetAddress.getByName("127.0.0.1"), 1809);

            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            logger = new BufferedWriter(new FileWriter("tcp2/logs.txt"));

            while (true) {
                String clientMessage = input.nextLine();

                if(clientMessage.equals("exit")){
                    System.out.println("Closing client...");
                    break;
                }

                writer.write(clientMessage);
                writer.newLine();
                writer.flush();

                logger.write(clientMessage);
                logger.newLine();
                logger.flush();

                String returnMessage = reader.readLine();
                System.out.println("Return message: " + returnMessage);
                logger.write(returnMessage);
                logger.newLine();
                logger.flush();
            }
        } catch (IOException exc) {
            System.out.println(exc.getMessage());
        }finally{
            try {
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                reader.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                logger.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) {
        Client client = new Client(1809);
        client.start();
    }
}
