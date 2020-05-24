import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

public class AnonGWServer implements Runnable {
    private int port;
    private Logger log = Logger.getLogger(AnonGWClient.class.getName());
    private DatagramSocket udpSocket;
    private Socket socket;
    private InetAddress address;
    private boolean running = true;
    public AnonGWServer(Socket socket) {
        this.socket = socket;
        this.port = 6666;
        try {
            udpSocket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        while(address == null) {
            try {
                address = InetAddress.getByName(BaseArgsInfo.getInstance().getRandomPeer());
            }catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        initServer();
        Thread t = null;
        waitForAck();
        try {
            t = new Thread(new UDPHelperToTcp(this.socket.getOutputStream(),this.port,null,false));
            t.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        sendTCPPacketsByUDP();
    }

    private void sendTCPPacketsByUDP() {
        try {
            TCPHelperToUDP.sendTCPPackagesToUDP(this.socket.getInputStream(),address, port);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void waitForAck()  {
        boolean waiting = true;
        DatagramSocket tmpSocket = null;
        try {
             tmpSocket = new DatagramSocket(port);
        }catch (SocketException e) {
            e.printStackTrace();
        }
        while(waiting) {
            try {
                byte[] tmpBuf = new byte[500];
                DatagramPacket packet = new DatagramPacket(tmpBuf, tmpBuf.length);

                if(tmpSocket != null) {
                    tmpSocket.receive(packet);
                    byte[] messageBytes = new byte[packet.getLength()];
                    messageBytes = Arrays.copyOf(tmpBuf,packet.getLength());
                    String message = new String(messageBytes);
                    if (message.equals("ACK")) {
                        log.info("Acknowledge received from " + packet.getAddress().getHostAddress());
                        waiting = false;
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        tmpSocket.close();

    }


    private void initServer() {
        log.info("Broadcasting to 6666 a new connection with a new port");
        port = sendPort();
        if(port == -1) port = sendPort();
        log.info(socket.getInetAddress().getHostAddress() + " is talking on port " + port);
        AnonBD.getInstance().addClientData(new ClientData(address.getHostAddress(), port, socket));
        //new Thread(new Proxy(socket,port,true)).start();
        //new Thread(new ConnectionToClient(port));
        log.info("Starting UDP TUNNELING");
    }

    private int sendPort() {
        byte[] buf;
        UDPPortMessage udpPortMessage = new UDPPortMessage();
        buf = ObjectSerializer.getObjectInByte(udpPortMessage);
        DatagramPacket portPacket = null;
        if (buf != null) {
            portPacket = new DatagramPacket(buf, buf.length, address, port);

            try {
                udpSocket.send(portPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return udpPortMessage.getCustomPort();
        } else {
            return -1;
        }

    }
}
