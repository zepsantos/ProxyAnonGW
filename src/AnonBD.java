import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class AnonBD  {
    private static final AnonBD instance = new AnonBD();
    private Map<Integer,ClientData> currentSessions;

    private AnonBD() {
        currentSessions = new ConcurrentHashMap<>();
    }

    public static AnonBD getInstance() {
        return instance;
    }

    public void addClientData(ClientData clientData) {
        this.currentSessions.put(clientData.getUdpPort(),clientData);
    }

    public ClientData getClient(int port) {
        return currentSessions.get(port);
    }
}
