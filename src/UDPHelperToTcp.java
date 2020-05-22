import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.util.TreeMap;

public class UDPHelperToTcp {
    public static void sendUDPPackagesToTCP(Socket socket,int port) {
        TreeMap<Integer,UDPData> dataTreeMap = new TreeMap<>();
        try {
            byte[] buf = new byte[200];
            DatagramSocket udpSocket = new DatagramSocket(port);
            DatagramPacket packet = new DatagramPacket(buf,buf.length);
            UDPData data = null;
            do {
                udpSocket.receive(packet);
                data = (UDPData) ObjectSerializer.getObjectFromByte(packet.getData());
                if(data != null)
                    dataTreeMap.put(data.getIndex(), data.clone());
            }while(data != null  && !data.isFinalPacket());
        } catch (IOException e) {
            e.printStackTrace();
        }
        sendAux(socket,port,dataTreeMap);
    }

    private static void sendAux(Socket socket, int port, TreeMap<Integer,UDPData> dataTreeMap) {
        try (OutputStream outputStream = socket.getOutputStream()) {
            for (Integer key : dataTreeMap.keySet()) {
                byte[] buf = dataTreeMap.get(key).getData();
                System.out.println(key);
                outputStream.write(buf);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
