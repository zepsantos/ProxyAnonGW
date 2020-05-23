import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.util.TreeMap;

public class UDPHelperToTcp implements Runnable {
    private OutputStream outputStream;
    private int port;
    private boolean running = true;

    public UDPHelperToTcp(OutputStream outputStream , int port) {
        this.port = port;
        this.outputStream = outputStream;
    }

    @Override
    public void run() {
        TreeMap<Integer,UDPData> dataTreeMap = new TreeMap<>();
        try {
            byte[] buf = new byte[500];
            DatagramSocket udpSocket = new DatagramSocket(port);
            DatagramPacket packet = new DatagramPacket(buf,buf.length);
            UDPData data = null;
            do {
                udpSocket.receive(packet);
                data = (UDPData) ObjectSerializer.getObjectFromByte(packet.getData());
                if(data != null) {
                    dataTreeMap.put(data.getIndex(), data.clone());
                    if(data.isFinalPacket()) sendAux(outputStream,dataTreeMap);
                }
            }while(running);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    private void sendAux(OutputStream outputStream, TreeMap<Integer,UDPData> dataTreeMap) {
        try  {
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
