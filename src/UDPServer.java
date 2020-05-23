import java.io.IOException;
import java.net.*;
import java.util.logging.Logger;

public class UDPServer implements Runnable {
    private int port;
    private Logger log = Logger.getLogger(UDPClient.class.getName());
    private DatagramSocket udpSocket;
    private Socket socket;
    private InetAddress address;
    private boolean running = true;
    public UDPServer(Socket socket) {
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
        Thread t = new Thread(this::listenForUDPPackets);
        t.start();
        sendTCPPacketsByUDP();
    }

    private void sendTCPPacketsByUDP() {
        try {
            TCPHelperToUDP.sendTCPPackagesToUDP(this.socket.getInputStream(),this.socket.getInetAddress(), port);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void listenForUDPPackets() {
        while(running) {
            try {
                UDPHelperToTcp.sendUDPPackagesToTCP(this.socket.getOutputStream(), port);
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
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
        log.info("Talking on port " + udpPortMessage.getCustomPort());
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
