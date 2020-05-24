import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Logger;

public class TCPHelperToUDP {

    public static void sendTCPPackagesToUDP(InputStream is, InetAddress address, int port) throws IOException {
        Logger.getLogger(TCPHelperToUDP.class.getName()).info("Sending TCP packages to UDP to " + address.getHostAddress() + " on port(UDP):  " + port);

        byte[] buffer = new byte[500];
        int packagesCount = 0;
        int bytesRead = 0;
        while((bytesRead = is.read(buffer,0,buffer.length)) != -1) {
            UDPData data = new UDPData(packagesCount,buffer,bytesRead);
            if(bytesRead < buffer.length) {
                data.setFinalPacket();
            }
            byte[] bufToSend = ObjectSerializer.getObjectInByte(data);
            if(bufToSend != null) {
                DatagramSocket udpSocket = new DatagramSocket();
                DatagramPacket datagramPacket = new DatagramPacket(bufToSend,bufToSend.length, address ,port);
                udpSocket.send(datagramPacket);
                packagesCount++;
            }
        }
    }
}
