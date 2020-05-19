public class AnonGW {

    public static void main(String[] args) {
        Command command = new Command(args);
        command.getCommands();
        new Thread(new TCPServer()).start();
        new Thread(new UDPServer()).start();
    }
}
