import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static final int PORT = 12345;
    private static  final Set<ChatHandler> clientHandlers = Collections.synchronizedSet(new HashSet<>());

    public static void main(String[] args)  {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
        System.out.println("Server started on port " + PORT);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("New client connected: " + clientSocket);
            ChatHandler handler = new ChatHandler(clientSocket, clientHandlers);
            clientHandlers.add(handler);
            handler.start();
        }
    }
     catch (IOException e) {
        System.err.println("Server exception :" + e.getMessage());
     }
    }
}
