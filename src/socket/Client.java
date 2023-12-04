package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client implements AutoCloseable {
    private final Socket socket;

    private final BufferedReader system;
    private final PrintWriter writer;
    private String userName;

    public Socket getSocket() {
        return socket;
    }

    private Client(String host,int port) throws IOException {
        socket = new Socket(host,port);
        system = new BufferedReader(new BufferedReader(new InputStreamReader(System.in)));
        writer  = new PrintWriter(socket.getOutputStream(),true);

    }
    public void close() throws IOException {
        socket.close();
        system.close();
    }
    public void execute() throws Exception {
        System.out.println("Servera baglanılıyor");
        System.out.println("Sohbete bağlanamabilmek için, chat@username biçimde isminizi giriniz");

        ClientConnection clientConnection = ClientConnection.createInstance(this);
        var thread = new Thread(clientConnection);
        thread.start();
        while (true){
            String msg = system.readLine();
            if (userName != null){
                writer.println(msg);
            }
            if (msg.contains("chat@")){
                userName = msg.split("chat@")[1];
                writer.println(msg);
            }
            if (msg.equals("q")){
                writer.println(msg);
                break;
            }
        }
    }
    public synchronized boolean  isClosed(){
        return this.socket.isClosed();
    }

    public static Client getInstance(String host,int port) throws IOException {
        return new Client(host,port);
    }
    public static void main(String[] args) throws Exception {
        Client.getInstance(Utils.HOST,Utils.PORT).execute();
    }
}
