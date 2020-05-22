import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.logging.Logger;

public class UDPClient implements Runnable {
    private int port;
    private Logger log = Logger.getLogger(UDPClient.class.getName());
    private Socket socket;
    public  UDPClient(int port)  {
        this.port = port;

    }
    @Override
    public void run() {

            connectToTarget();
        try {
            TCPHelperToUDP.sendTCPPackagesToUDP(socket,port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void connectToTarget() {
        try{
            socket = new Socket(BaseArgsInfo.getInstance().getTarget_ip(),BaseArgsInfo.getInstance().getPort());
            /*InputStream is = socket.getInputStream();
            /*PrintWriter pw = new PrintWriter(clientSocket.getOutputStream());
            pw.println("GET / HTTP/1.0");
            pw.println();
            pw.flush();
            byte[] buffer = new byte[500];
            int read;
            while ((read = is.read(buffer)) != -1) {

            }
            socket.close();*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
