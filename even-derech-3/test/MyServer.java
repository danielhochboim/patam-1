package test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class MyServer {
    private final int port;
    private final ClientHandler ch;
    private boolean stop;
    public MyServer(int port, ClientHandler ch) {
        this.port = port;
        this.ch = ch;
        this.stop = false;
    }

    private void runServer() throws Exception{
        ServerSocket server = new ServerSocket(port);
        server.setSoTimeout(1000);
        Socket aClient = null;
        while(!stop){
            try {
                aClient = server.accept();
                ch.handleClient(aClient.getInputStream(), aClient.getOutputStream());
            } catch (SocketTimeoutException e) {
                // Timeout occurred, trying again...
            } catch(IOException e) {
                e.printStackTrace();
            }
            finally{
                if (aClient != null && !aClient.isClosed()) {
                    try {
                        aClient.close();  // Close the socket, which automatically closes streams
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }                   
        }
        server.close();
        
    
    }
     public void start(){
        new Thread(() -> {
            try {
                runServer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
     }
     public void close(){
        stop = true;
     }
	
}
