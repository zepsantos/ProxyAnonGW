import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPThreadListen implements  Runnable {
    private DatagramSocket udpSocket;
    private boolean running = true;
    public UDPThreadListen() {
        try {
            udpSocket = new DatagramSocket(6666);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(running) {
            byte[] buf = new byte[500];
            DatagramPacket packet = new DatagramPacket(buf,buf.length);
            try {
                udpSocket.receive(packet);
                UDPPortMessage udpPortMessage = (UDPPortMessage) ObjectSerializer.getObjectFromByte(packet.getData());
                if(udpPortMessage != null)
                new Thread(new UDPClient(udpPortMessage.getCustomPort())).start();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }
}