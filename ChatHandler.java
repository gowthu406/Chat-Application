import java.io.*;
import java.net.*;
import java.util.*;

public class ChatHandler extends Thread {
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    private Set<ChatHandler> clientHandlers;
    private String clientName;

    public ChatHandler(Socket socket, Set<ChatHandler> clientHandlers) {
        this.socket = socket;
        this.clientHandlers = clientHandlers;
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            System.out.println("Error setting up streams: " + e.getMessage());
        }
    }

    public void run() {
        try {
            output.println("Enter your name:");
            clientName = input.readLine();
            broadcast("💬 " + clientName + " has joined the chat.");

            String message;
            while ((message = input.readLine()) != null) {
                broadcast(clientName + ": " + message);
            }
        } catch (IOException e) {
            System.out.println("Connection error with client " + clientName);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                //
            }
            clientHandlers.remove(this);
            broadcast("❌ " + clientName + " left the chat.");
        }
    }

    private void broadcast(String message) {
        synchronized (clientHandlers) {
            for (ChatHandler handler : clientHandlers) {
                handler.output.println(message);
            }
        }
    }
}