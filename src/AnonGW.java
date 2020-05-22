public class AnonGW {
    public static void main(String[] args) {
        Command command = new Command(args);
        command.getCommands();
        new Thread(new TCPThreadListen()).start();
        new Thread(new UDPThreadListen()).start();
    }
}
