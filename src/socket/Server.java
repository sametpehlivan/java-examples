package socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static volatile Server instance = null;
    private final ServerSocket serverSocket;

    private Server(int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        clients = new ArrayList<>();
        executorService = Executors.newFixedThreadPool(10);
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public ArrayList<ClientHandler> getClients() {
        return clients;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    private final ArrayList<ClientHandler> clients;
    private final ExecutorService executorService;

    public void execute() throws IOException {
        System.out.println("Client Bekleniyor");
        while (true){

           Socket client =  serverSocket.accept();
           try {
               ClientHandler clientHandler = ClientHandler.newClientHandler(this, client);
               clientHandler.getWriter().println("Bağlantı Başarılı");
               clients.add(clientHandler);
               executorService.execute(clientHandler);
           }catch (Exception e){
               System.out.println("Client'a Bağlanılamadı");
           }

        }
    }
    public void close() throws IOException {
        serverSocket.close();

    }
    public static Server createServer(int port){
        if(instance == null){
            synchronized (Server.class){
                if (instance == null) instance = new Server(port);
            }
        }
        return instance;
    }

    public static void main(String[] args) throws IOException {
        Server server = Server.createServer(Utils.PORT);
        server.execute();
    }
}
