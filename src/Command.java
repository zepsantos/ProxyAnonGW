

import java.util.Arrays;
import java.util.List;

public class Command {
    public String[] stringCommands;
    public Command(String[] args) {
        this.stringCommands = Arrays.copyOf(args,args.length);
    }

    public void getCommands() {
        BaseArgsInfo info = BaseArgsInfo.getInstance();
        for(int i=0; i< stringCommands.length; i++) {
            if(stringCommands[i].equals("target-server")) {
                info.setTarget_ip(stringCommands[i+1]);
            } else if(stringCommands[i].equals("port")) {
                info.setPort(stringCommands[i+1]);
            } else if(stringCommands[i].equals("overlay-peers")) {
                getPeers(info,Arrays.copyOfRange(stringCommands,i+1,stringCommands.length));
            }
        }
    }

    private void getPeers(BaseArgsInfo info , String[] peers) {
        for(int i = 0;i<peers.length;i++) {
            info.addPeer(peers[i]);
        }

    }
}