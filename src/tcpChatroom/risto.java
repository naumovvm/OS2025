package tcpChatroom;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class risto extends Thread {
    private static final String LOG_FILE = "chatlog233104.txt";
    private int server_port;

    public risto(int server_port) {
        this.server_port = server_port;
    }

    @Override
    public void run() {
        Socket socket = null;
        BufferedReader br = null;
        BufferedWriter bw = null;
        PrintWriter logWriter = null;
        BufferedReader userInput = null;

        try {
            socket = new Socket(InetAddress.getByName("194.149.135.49"), 9753);

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            logWriter = new PrintWriter(new FileWriter(LOG_FILE, true));
//            Scanner scanner = new Scanner(System.in);
            userInput = new BufferedReader(new InputStreamReader(System.in));

            String message = "hello:233104";
            bw.write(message);
            bw.newLine();
            bw.flush();
            logWriter.println("SENT: " + message + "\n");
            logWriter.flush();

            String response = br.readLine();
            System.out.println(response);
            logWriter.println("RECEIVED: " + response);
            logWriter.flush();

            while (true) {
                if (br.ready()) {
                    String servermessage = br.readLine();
                    if (servermessage != null) {
                        System.out.println(servermessage);
                        logWriter.println(message + "\n");
                        logWriter.flush();
                    }
                }

                if (userInput.ready()) {
                    String input = userInput.readLine();
                    bw.write(input);
                    bw.newLine();
                    bw.flush();
                    logWriter.println(input + "\n");
                    logWriter.flush();
                }

            }

//            bw.write("Closing");
//            bw.newLine();
//            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        risto client = new risto(9753);
        client.start();
    }
}