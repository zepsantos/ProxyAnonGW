import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class ConnectionToClient implements Runnable {
    private int port;
    private TreeMap<Integer,UDPData> udpPacketsList;

    public ConnectionToClient(int port) {
        this.port = port;
        udpPacketsList = new TreeMap<>();
    }
    @Override
    public void run() {
        ClientData clientData = AnonBD.getInstance().getClient(port);
        UDPHelperToTcp.sendUDPPackagesToTCP(clientData.getSocket(),port);


    }



}
