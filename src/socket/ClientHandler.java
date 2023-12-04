package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ClientHandler implements Runnable,AutoCloseable{
    private static int lastId = 0;
    {

    }
    private int id;
    private String userName = "";
    private final Server server;

    private final Socket client;
    private BufferedReader reader;
    private PrintWriter writer;
    private ClientHandler(Server server,Socket client) throws IOException {
        lastId++;
        this.id = lastId;
        this.server = server;
        this.client = client;
        this.reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
        this.writer = new PrintWriter(client.getOutputStream(),true);
    }


    public Server getServer() {
        return server;
    }
    public int getId(){
        return id;
    }
    public Socket getClient() {
        return client;
    }

    public BufferedReader getReader() {
        return reader;
    }

    public PrintWriter getWriter() {
        return writer;
    }

    @Override
    public void run() {
        try(ClientHandler c = this) {
            while (true){

                String clientRequest = reader.readLine();
                if (clientRequest.equals("q")){
                    sendAllUser("kullanıcı sohebtten ayrıldı",this);
                    break;
                }
                if (!userName.isEmpty()){
                    sendAllUser(clientRequest,this);
                }else {
                   if (clientRequest.contains("chat@")){
                       userName = clientRequest.split("chat@")[1];
                       sendAllUser(userName + "adlı kullanıcı bağlandı",this);
                   }
                }

            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }


    }
    private void sendAllUser(String msg,ClientHandler src){
        var clients = server.getClients()
                .stream()
                .filter(clientHandler -> clientHandler.getId() != src.getId());

        clients .forEach(
                            (clientHandler ) -> clientHandler.getWriter().println(createMessageHeader()+msg)
                    );
    }
    public void close() throws IOException {
        this.server.getClients().removeIf(c-> c.getId() == this.id);
        this.client.close();
        this.reader.close();
        this.writer.close();
    }
    public static ClientHandler newClientHandler(Server server ,Socket client) throws IOException {
        ClientHandler clientHandler;
        synchronized (ClientHandler.class){
            clientHandler = new ClientHandler(server,client);
        }
        return clientHandler;
    }
    public String createMessageHeader(){
        return "[username@"+userName +" - client@"+id+"] "+ LocalDate.now().toString() +":";
    }
}