import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;

public class UDPData implements Serializable {
    private int index;
    private boolean finalpacket;
    private byte[] data;

    public UDPData(int packageNumber, byte[] data,int bytesUsed) {
        index = packageNumber;
        this.data = Encryption.encrypt(Arrays.copyOf(data,bytesUsed));
        this.finalpacket = false;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public byte[] getData() {
            return Encryption.decrypt(data);
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public void setFinalPacket() {
        this.finalpacket = true;
    }

    public boolean isFinalPacket() {
        return this.finalpacket;
    }

    @Override
    protected UDPData clone() {
        UDPData tmp = new UDPData(index,data,data.length);
        if(isFinalPacket()) tmp.setFinalPacket();
        return tmp;
    }
}
