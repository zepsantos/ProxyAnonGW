import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.logging.Logger;

public class UDPThreadListen implements  Runnable {
    private static Logger log = Logger.getLogger(UDPThreadListen.class.getName());
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
            byte[] buf = new byte[BaseArgsInfo.UDP_PACKETSIZE];
            DatagramPacket packet = new DatagramPacket(buf,buf.length);
            try {
                udpSocket.receive(packet);
                UDPPortMessage udpPortMessage = (UDPPortMessage) ObjectSerializer.getObjectFromByte(packet.getData());
                log.info("Receive a Connection(UDP) from " + packet.getAddress().getHostAddress() + " to talk in port " + udpPortMessage.getCustomPort());
                if(udpPortMessage != null)
                new Thread(new AnonGWClient(udpPortMessage.getCustomPort(),packet.getAddress())).start();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }
}
