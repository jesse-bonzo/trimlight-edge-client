package cc.jessebonzo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;

public class Broadcast {
    static final byte START_OF_HEADING = 0x01;
    static final byte ACK = 0x06;
    static final byte HORIZONTAL_TAB = 0x09;
    static final byte DATA_LINK_ESCAPE = 0x10;
    static final byte L_PAREN = 0x28;

    public static void main(String[] args) {
        getDevices();
    }

    public record Device(InetAddress address, String hashKey, String code, String name, int category) {
    }

    public static List<Device> getDevices() {
        try (DatagramSocket socket = new DatagramSocket(new InetSocketAddress(8588))) {
            socket.setBroadcast(true);
            byte[] buffer = {0x53, 0x50, 0x54, 0x45, 0x43, 0x48, 0x06, 0x00, 0x00, 0x00};
            socket.send(new DatagramPacket(buffer, buffer.length, InetAddress.getByName("255.255.255.255"), 8589));
            System.out.println("Broadcast message sent!");
            final byte[] receiveBuffer = new byte[100];
            final DatagramPacket datagramPacket = new DatagramPacket(receiveBuffer, 0, receiveBuffer.length);
            socket.receive(datagramPacket);

            System.out.println("Received");
            for (byte b : receiveBuffer) {
                System.out.printf("0x%02x, ", b);
            }
            System.out.println();

            int ackIndex = indexOf(receiveBuffer, ACK);
            if (ackIndex == -1) {
                System.err.println("Not an ACK");
                return List.of();
            }

            // receivedData should start with the buffer we sent ("SPTECH")
            if (Arrays.equals(receiveBuffer, 0, ackIndex, buffer, 0, ackIndex)) {
                int startOfHeadingIndex = indexOf(receiveBuffer, START_OF_HEADING, ackIndex);
                int dataLinkEscapeIndex = indexOf(receiveBuffer, DATA_LINK_ESCAPE, startOfHeadingIndex);
                int horizontalTabIndex = indexOf(receiveBuffer, HORIZONTAL_TAB, dataLinkEscapeIndex);
                int lParenIndex = indexOf(receiveBuffer, L_PAREN, horizontalTabIndex);
                int category = Byte.toUnsignedInt(receiveBuffer[startOfHeadingIndex - 1]);
                String deviceCode = new String(receiveBuffer, dataLinkEscapeIndex + 1, horizontalTabIndex - dataLinkEscapeIndex - 1);
                String name = new String(receiveBuffer, horizontalTabIndex + 1, lParenIndex - horizontalTabIndex - 1);
                String deviceHashKey = new String(receiveBuffer, lParenIndex + 1, receiveBuffer.length - lParenIndex - 1);
                return List.of(new Device(datagramPacket.getAddress(), deviceHashKey, deviceCode, name, category));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return List.of();
    }

    public static DatagramPacket listen() throws IOException {
        DatagramPacket broadcastPacket;
        try (DatagramSocket socket = new DatagramSocket(new InetSocketAddress(8589))) {
            broadcastPacket = new DatagramPacket(new byte[100], 0, 100);
            socket.receive(broadcastPacket);
        }
        return broadcastPacket;
    }


    static int indexOf(byte[] data, byte b) {
        for (int i = 0; i < data.length; i++) {
            if (data[i] == b) {
                return i;
            }
        }
        return -1;
    }

    static int indexOf(byte[] data, byte b, int from) {
        for (int i = from; i < data.length; i++) {
            if (data[i] == b) {
                return i;
            }
        }
        return -1;
    }
}
