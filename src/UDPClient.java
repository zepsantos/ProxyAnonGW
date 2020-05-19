import java.io.IOException;
import java.net.*;
import java.util.logging.Logger;

public class UDPClient implements Runnable {
    private DatagramSocket udpSocket;
    private Socket socket;
    private InetAddress address;
    private int port;
    private Logger log = Logger.getLogger(UDPClient.class.getName());
    public  UDPClient(int port, Socket socket)  {
        this.socket = socket;
        this.port = port;
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
        if(port == 6666) {
            log.info("Broadcasting to 6666 a new connection with a new port");
            port = sendPort();
            System.out.println(port);
            AnonBD.getInstance().addClientData(new ClientData(address.getHostAddress(), port, socket));
                new Thread(new Proxy(socket,port,true)).start();
                new Thread(new ConnectionToClient(port));
                log.info("Starting proxy");

        } else {
            try {
                socket = new Socket(BaseArgsInfo.getInstance().getTarget_ip(),BaseArgsInfo.getInstance().getPort());
                new Thread(new Proxy(socket, port,false)).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private int sendPort() {
        byte[] buf = null;
        UDPPortMessage udpPortMessage = new UDPPortMessage();
        System.out.println("VOU falar na porta " + udpPortMessage.getCustomPort());
        buf = ObjectSerializer.getObjectInByte(udpPortMessage);
        DatagramPacket portPacket = new DatagramPacket(buf,buf.length,address,port);
        try {
            udpSocket.send(portPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return udpPortMessage.getCustomPort();
    }
}
