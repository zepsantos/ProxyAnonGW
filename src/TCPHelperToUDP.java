import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;

public class TCPHelperToUDP {

    public static void sendTCPPackagesToUDP(Socket socket, int port) throws IOException {
        InputStream is = socket.getInputStream();
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
                DatagramPacket datagramPacket = new DatagramPacket(bufToSend,bufToSend.length, socket.getInetAddress(),port);
                udpSocket.send(datagramPacket);
                packagesCount++;
            }
        }
    }
}
