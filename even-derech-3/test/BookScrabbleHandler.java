package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;

public class BookScrabbleHandler implements ClientHandler {
    private InputStream inFromClient;
    private OutputStream outToClient;
    @Override
    public void handleClient(InputStream inFromClient, OutputStream outToClient) {
        this.inFromClient = inFromClient;
        this.outToClient = outToClient;

        BufferedReader input = new BufferedReader(new InputStreamReader(inFromClient));
        PrintWriter output = new PrintWriter(outToClient);
        String line = null;
        
        try {
            line = input.readLine();

            String[] list = line.split(",");
            String queryOrChallenge = list[0];
            String[] books = Arrays.copyOfRange(list, 1, list.length);
            DictionaryManager dictionaryManager = DictionaryManager.get();
            //check if its a query or challenge
            if(queryOrChallenge == "Q"){
                output.println(String.valueOf(dictionaryManager.query(books)));
            }
            else{
                output.println(String.valueOf(dictionaryManager.challenge(books)));
            }
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally{
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (output != null) {
                output.close();
            }
            this.close();
    
        }
    }

    @Override
    public void close() {
        try {
            if (inFromClient != null) {
                inFromClient.close();
            }
            if (outToClient != null) {
                outToClient.close();
            }
        } catch (IOException e) {
            e.printStackTrace(); 
        }
    }

}
