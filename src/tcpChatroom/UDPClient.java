package tcpChatroom;

import java.io.IOException;
import java.net.*;

public class UDPClient extends Thread {
    private String serverName;
    private int serverPort;
    private DatagramSocket socket;
    private InetAddress address;
    private String message;
    private byte[] buffer;

    public UDPClient(String serverName, int serverPort, String message) {
        this.serverName = serverName;
        this.serverPort = serverPort;
        this.message = message;

        try {
            this.socket = new DatagramSocket();
            this.address = InetAddress.getByName(serverName);
        } catch (UnknownHostException | SocketException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void run() {
        buffer = message.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, serverPort);

        try{
            socket.send(packet);

            byte[] newBuffer = new byte[256];
            DatagramPacket receivedPacket = new DatagramPacket(newBuffer, newBuffer.length);
            socket.receive(receivedPacket);

            System.out.println(new String(receivedPacket.getData(), 0, receivedPacket.getLength()));
            System.out.println(receivedPacket.getAddress().getHostAddress());
            System.out.println(receivedPacket.getPort());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        UDPClient client = new UDPClient("194.149.135.49", 9753, "233194");
        client.start();
    }
}
