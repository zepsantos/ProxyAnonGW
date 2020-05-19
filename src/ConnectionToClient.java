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

        try {
            byte[] buf = new byte[200];
            DatagramSocket udpSocket = new DatagramSocket(port);
            DatagramPacket packet = new DatagramPacket(buf,buf.length);
            UDPData data = null;
            do {
                udpSocket.receive(packet);
                data = (UDPData) ObjectSerializer.getObjectFromByte(packet.getData());
                if(data != null)
                udpPacketsList.put(data.getIndex(), data.clone());
            }while(data != null  && !data.isFinalPacket());
        } catch (IOException e) {
            e.printStackTrace();
        }

        sendUDPPacketsToTcp();
    }

    private void sendUDPPacketsToTcp() {
        ClientData client = AnonBD.getInstance().getClient(port);
        try (OutputStream outputStream = client.getSocket().getOutputStream()) {
            for (Integer key : udpPacketsList.keySet()) {
                byte[] buf = udpPacketsList.get(key).getData();
                System.out.println(key);
                outputStream.write(buf);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
