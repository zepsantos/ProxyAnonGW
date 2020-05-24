import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.*;
import java.util.logging.Logger;

public class AnonGWClient implements Runnable {
    private int port;
    private Logger log = Logger.getLogger(AnonGWClient.class.getName());
    private Socket socket;
    private InetAddress clientAddress;
    public AnonGWClient(int port, InetAddress clientAddress)  {
        this.port = port;
        this.clientAddress = clientAddress;
    }
    @Override
    public void run() {
            connectToTarget();
        try {
            Thread t = new Thread(new UDPHelperToTcp(this.socket.getOutputStream(),this.port));
            t.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        sendAcknowledgeUDP();

        try {
            TCPHelperToUDP.sendTCPPackagesToUDP(this.socket.getInputStream(),clientAddress,port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void sendAcknowledgeUDP() {
        byte[] buff = "ACK".getBytes();
        DatagramPacket packet = new DatagramPacket(buff,buff.length,clientAddress,port);
        try {
            DatagramSocket udpSocket = new DatagramSocket();
            udpSocket.send(packet);
            udpSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void connectToTarget() {
        try{
            socket = new Socket(BaseArgsInfo.getInstance().getTarget_ip(),BaseArgsInfo.getInstance().getPort());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
