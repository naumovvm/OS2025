package udp;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPServer {
    private DatagramSocket socket;
    private byte[] buffer;
    public static int counter = 0;

    public UDPServer(int port) {
        try {
            this.socket = new DatagramSocket(port);
        } catch (SocketException exception) {
            exception.printStackTrace();
        }
        this.buffer = new byte[256];
    }

    public void run() {
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        while (true) {
            try {
                socket.receive(packet);
                String receivedPacket = new String(packet.getData(), 0, packet.getLength());
                counter++;
                System.out.println("RECEIVED " + counter + ": " + receivedPacket);

                packet = new DatagramPacket(buffer, buffer.length, packet.getAddress(), packet.getPort());
//                socket.send(packet);

//                String filePath = "test.txt";
//                BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true));
//
//                writer.write("Received: " + counter);
//                writer.newLine();
//                writer.flush();
//                writer.close();

                String returnMsg = "Hello padawan";
                byte[] returnBuffer = returnMsg.getBytes();
                DatagramPacket returningPacket = new DatagramPacket(returnBuffer, returnBuffer.length, packet.getAddress(), packet.getPort());
                socket.send(returningPacket);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {
        UDPServer server = new UDPServer(8080);
        server.run();
    }
}
