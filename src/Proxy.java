
import javax.xml.crypto.Data;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.System.Logger;
import java.net.*;

public class Proxy implements Runnable {

    private final Socket tcpSocket;
    private int udpPort;
    private final boolean tcpToUdp;
    //private final Logger logger = Logger.getLogger(Proxy.class.getName());

    public Proxy(Socket tcpSocket, int udpPort , boolean tcpToUdp) {
        this.tcpToUdp = tcpToUdp;
        this.udpPort = udpPort;
        this.tcpSocket = tcpSocket;
    }

    @Override
    public void run() {
        try {
            if (tcpToUdp) {
                sendToUDP();
            } else {
                sendToTCP();
            }
        }catch(IOException e) {
            e.printStackTrace();
        }


    }

    private void sendToUDP() throws IOException {
        byte[] reply = new byte[65535];
        InputStream inputStream = getInputStream();
        int bytesRead;
        int packageCount = 0;
        while (inputStream != null && (bytesRead = inputStream.read(reply,0,200)) != -1) {
            UDPData dataUdp = new UDPData(packageCount,reply,bytesRead);
            if(bytesRead < 200)
                    dataUdp.setFinalPacket();
            byte[] buf = ObjectSerializer.getObjectInByte(dataUdp);
            DatagramSocket udpSocket = new DatagramSocket();
            if(buf != null) {
            DatagramPacket datagramPacket = new DatagramPacket(buf,buf.length, tcpSocket.getInetAddress(),udpPort);
            udpSocket.send(datagramPacket);}
        }

    }


    private void sendToTCP() throws IOException {
        UDPHelperToTcp.sendUDPPackagesToTCP(this.tcpSocket,this.udpPort);
        sendToUDP();
    }

    private InputStream getInputStream() {
        try {
            return tcpSocket.getInputStream();
        } catch (final IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private OutputStream getOutputStream() {
        try {
            return tcpSocket.getOutputStream();
        } catch (final IOException e) {
            e.printStackTrace();
        }

        return null;
    }


}