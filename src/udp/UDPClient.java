package udp;

import java.io.FileWriter;
import java.io.IOException;
import java.net.*;

public class UDPClient extends Thread {
    private String serverName;
    private int serverPort;

    private DatagramSocket socket;
    private InetAddress address;
    private String message;
    private byte[] buffer;

    public UDPClient(String serverName, int serverPort, String msg) {
        this.serverName = serverName;
        this.serverPort = serverPort;
        this.message = msg;

        try {
            this.socket = new DatagramSocket();
            this.address = InetAddress.getByName(serverName);
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        buffer = message.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, serverPort);

        try {
            socket.send(packet);

            byte[] newBuffer = new byte[256];
            DatagramPacket receivingPacket = new DatagramPacket(newBuffer, newBuffer.length, address, serverPort);

            socket.receive(receivingPacket);
            System.out.println("Return paket: " + new String(receivingPacket.getData(), 0, receivingPacket.getLength()));
//            System.out.println("Adresa: "+receivingPacket.getAddress());
//            System.out.println(" i na hostot: "+receivingPacket.getAddress().getHostAddress());
//            System.out.println("Message: ");
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static void main(String[] args) {
//        for (int i = 1; i <= 10; i++) {
//            UDPClient client = new UDPClient("localhost", 8080, "Poraka od " + i + " klient.");
//            client.start();
//
//            try{
//                client.join();
//            }catch(InterruptedException exception){
//                exception.printStackTrace();
//            }
//        }
        UDPClient client = new UDPClient("localhost", 8080, "Zdravo!!!");
        client.start();
    }
}
