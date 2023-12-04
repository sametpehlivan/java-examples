package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientConnection implements Runnable,AutoCloseable{
    private final Client client;
    private final BufferedReader reader;
    volatile boolean exit = false;
    private ClientConnection(Client client, BufferedReader reader) {
        this.client = client;
        this.reader = reader;
    }
    @Override
    public void run() {

        try(ClientConnection c = this) {

            while (!exit) {
                String response = reader.readLine();
                if (response == null) {
                    close();
                    break;
                }
                if (response.equals("q")){
                    close();
                    break;
                }
                System.err.println(response);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void close() throws Exception {
        exit = true;
        reader.close();
        this.client.close();

    }

    public static ClientConnection createInstance(Client client) throws IOException {
        return new ClientConnection(client,new BufferedReader(new InputStreamReader(client.getSocket().getInputStream())));
    }
}
