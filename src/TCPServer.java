import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class TCPServer implements Runnable {
    private ServerSocket serverSocket;
    private Logger log = Logger.getLogger(TCPServer.class.getName());
    public TCPServer() {
        try {
            serverSocket = new ServerSocket(BaseArgsInfo.getInstance().getPort());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {

        while(true) {
            try {
                Socket socket = serverSocket.accept();
                log.info("TCP Connection received: "+ socket.getInetAddress().getHostAddress());
                new Thread(new UDPClient(6666,socket)).start();
            } catch (IOException  e) {
                e.printStackTrace();
            }

        }

    }
}
