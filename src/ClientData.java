import java.net.Socket;

public class ClientData {
    private String peer;
    private int udpPort;
    private Socket socket;

    public ClientData(String peer, int udpPort , Socket socket) {
        this.peer = peer;
        this.udpPort = udpPort;
        this.socket = socket;
    }

    public String getPeer() {
        return peer;
    }

    public void setPeer(String peer) {
        this.peer = peer;
    }

    public int getUdpPort() {
        return udpPort;
    }

    public void setUdpPort(int udpPort) {
        this.udpPort = udpPort;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
