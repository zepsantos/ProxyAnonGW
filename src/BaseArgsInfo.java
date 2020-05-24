import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class BaseArgsInfo {
    private static BaseArgsInfo instance = null;
    private List<String> peers;
    private String target_ip;
    private int port;
    public final static int UDP_PACKETSIZE = 576;
    private BaseArgsInfo() {
        peers = new ArrayList<>();
    }

    public static BaseArgsInfo getInstance() {
        if(instance == null) instance = new BaseArgsInfo();
        return instance;
    }

    public List<String> getPeers() {
        return peers;
    }

    public void setPeers(List<String> peers) {
        this.peers = peers;
    }

    public void addPeer(String peer) {
        this.peers.add(peer);
    }

    public String getTarget_ip() {
        return target_ip;
    }

    public void setTarget_ip(String target_ip) {
        this.target_ip = target_ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = Integer.parseInt(port);
    }

    public String getRandomPeer() {
        int randomIndex = ThreadLocalRandom.current().nextInt(0,getPeers().size());
        return getPeers().get(randomIndex);
    }
}