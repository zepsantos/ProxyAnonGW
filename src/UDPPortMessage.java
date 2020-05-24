import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;

public class UDPPortMessage implements Serializable {
    private int customPort;

    public UDPPortMessage() {
        customPort =  getRandomPort();
    }


    public int getCustomPort() {
        return customPort;
    }


    public int getRandomPort() {
        int random = ThreadLocalRandom.current().nextInt(7000,8000);
        return random;
    }
}
