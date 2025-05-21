package tcp;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCPClient extends Thread {
    private int serverPort;

    public TCPClient(int serverPort) {
        this.serverPort = serverPort;
    }

    @Override
    public void run() {
        Socket socket;

        try {
            // Otvarame soket koj ke slusha do dadena ip adresa i porta
            socket = new Socket(InetAddress.getByName("127.0.0.1"), 1280);

            // prikacuvame dva streams na soketot, eden za citanje podatoci, drug za prakjanje na podatoci
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            // Isprakjame poraka so writer-ot preku klientot
            String msg = "Hello there - some client ";
            writer.write(msg);
            writer.newLine();
            writer.flush();

            System.out.println("Message: " + msg + "sent!");

            // primanje na poraka
            String returnMsg = reader.readLine();
            System.out.println("Return message: " + returnMsg);

            // zatvaranje na konekcijata so isprakjanje na poraka uspat
            writer.write("Closing the connection now...");
            writer.newLine();
            writer.flush();

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        TCPClient client = new TCPClient(1280);
        client.start();
    }
}
