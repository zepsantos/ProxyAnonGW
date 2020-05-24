

import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.TreeMap;
import java.util.logging.Logger;

public class UDPHelperToTcp implements Runnable {
    private static Logger log = Logger.getLogger(UDPHelperToTcp.class.getName());
    private OutputStream outputStream;
    private int port;
    private boolean running = true;
    private DatagramSocket udpSocket;
    private InetAddress destAddress;
    private boolean sendAcknowledge;
    public UDPHelperToTcp(OutputStream outputStream , int port, InetAddress destAddress,boolean sendAcknowledge) {
        this.port = port;
        this.outputStream = outputStream;
        this.destAddress = destAddress;
        this.sendAcknowledge =  sendAcknowledge;
        try {
            this.udpSocket = new DatagramSocket(port);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        if(sendAcknowledge)
        sendAcknowledgeUDP();
        do {
            boolean finalPackReceived = false;
        TreeMap<Integer,UDPData> dataTreeMap = new TreeMap<>();
        try {
            byte[] buf = new byte[500];

            DatagramPacket packet = new DatagramPacket(buf,buf.length);
            UDPData data = null;
            udpSocket.receive(packet);
            log.info("Received a udp packet coming from " + packet.getAddress().getHostAddress());
            data = (UDPData) ObjectSerializer.getObjectFromByte(packet.getData());
            if(data != null) {
                dataTreeMap.put(data.getIndex(), data.clone());
                if(finalPackReceived || data.isFinalPacket() ) {
                    finalPackReceived = true;
                    if(checkAllPackagesArrived(dataTreeMap))
                    sendAux(outputStream,dataTreeMap);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
            }while(running);


    }

    private void sendAcknowledgeUDP() {
        byte[] buff = "ACK".getBytes();
        DatagramPacket packet = new DatagramPacket(buff,buff.length,destAddress,port);
        try {
            DatagramSocket udpSocket = new DatagramSocket();
            udpSocket.send(packet);
            udpSocket.close();
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

    private boolean checkAllPackagesArrived(TreeMap<Integer,UDPData> dataTreeMap) {
        int finalIndex = dataTreeMap.lastKey();
        boolean res = true;
        for(int i=0; i<finalIndex;i++) {
            if( !dataTreeMap.containsKey(i)) {
                res = false;
                break;
            }
        }
        return res;
    }


}
