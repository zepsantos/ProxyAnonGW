import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class TCPThreadListen implements Runnable {
    private ServerSocket serverSocket;
    private Logger log = Logger.getLogger(TCPThreadListen.class.getName());
    private boolean running = true;
    public TCPThreadListen() {
        try {
            serverSocket = new ServerSocket(BaseArgsInfo.getInstance().getPort());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {

        while(running) {
            try {
                Socket socket = serverSocket.accept();
                log.info("TCP Connection received: "+ socket.getInetAddress().getHostAddress());

                new Thread(new AnonGWServer(socket)).start();
            } catch (IOException  e) {

                e.printStackTrace();
            }
        }

    }
}


 /* InputStream is = socket.getInputStream();
                PrintWriter pw = new PrintWriter(socket.getOutputStream());
                pw.println();
                pw.flush();
                byte[] buffer = new byte[1024];
                int read;
                while((read = is.read(buffer)) != -1) {
                    String output = new String(buffer, 0, read);
                    System.out.print(output);
                    System.out.flush();
                };
                socket.close(); */