import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class TCPHelperToUDP {
    public static void sendTCPPackagesToUDP(InputStream is, InetAddress address, int port) throws IOException {
        Logger.getLogger(TCPHelperToUDP.class.getName()).info("Sending TCP packages to UDP to " + address.getHostAddress() + " on port(UDP):  " + port);

        byte[] buffer = new byte[65535];

        int bytesRead = 0;


        while((bytesRead = is.read(buffer,0,buffer.length)) != -1) {
            List<UDPData> tmp = divideTCPPacketToUDPPackets(buffer,bytesRead);
            dispatchUDPPackages(tmp,address,port);
        }
    }

    private static List<UDPData> divideTCPPacketToUDPPackets(byte[] tcpContent,int bytesRead) {
        int numPackages = (int) Math.ceil(((double) bytesRead/300));
        int bytesProcessed = 0;
        int packagesCount = 0;
        List<UDPData> dataUDPtoSend = new ArrayList<>();
        while(numPackages > 0) {
            int bytesToProcess = Math.min(300,bytesRead-bytesProcessed);
            UDPData tmp = new UDPData(packagesCount,Arrays.copyOfRange(tcpContent,bytesProcessed,bytesProcessed + bytesToProcess),bytesToProcess);
            if(numPackages == 1) tmp.setFinalPacket();
            dataUDPtoSend.add(tmp);
            bytesProcessed += bytesToProcess;
            numPackages--;
            packagesCount++;

        }

        return dataUDPtoSend;
    }

    private static void dispatchUDPPackages(List<UDPData> dataUDPtoSend,InetAddress address, int port) throws IOException {
        for(UDPData data : dataUDPtoSend) {
            byte[] bufToSend = ObjectSerializer.getObjectInByte(data);
            if (bufToSend != null) {
                DatagramSocket udpSocket = new DatagramSocket();
                DatagramPacket datagramPacket = new DatagramPacket(bufToSend, bufToSend.length, address, port);
                udpSocket.send(datagramPacket);
            }
        }
    }
}
