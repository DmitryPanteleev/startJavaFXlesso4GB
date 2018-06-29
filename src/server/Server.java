package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Vector;

public class Server {

    private Vector<ClientHandler> clients;

    public Vector<ClientHandler> getClients() {
        return clients;
    }

    public void StartServer() throws SQLException {
        clients = new Vector<ClientHandler>();

        ServerSocket serverSocket = null;
        Socket socket = null;

        try {
//            AuthService.connect();
//            String name = AuthService.getNickByLoginAndPass("login1","pass1");
            serverSocket = new ServerSocket(8189);
            System.out.println("server start, waiting connection...");
            while (true) {
                socket = serverSocket.accept();
                System.out.println("Connection");
//                subscribe(new ClientHandler(this, socket));
            }


        } catch (IOException e) {
            System.out.println("Exception server initializer");
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    public void subscribe(ClientHandler client) {
        clients.add(client);
    }

    public void unSubscribe(ClientHandler client) {
        clients.remove(client);
    }

    public void broadcastMsg(String msg) {
        checkValidSocket();
        for (ClientHandler c :
                clients) {
            c.sendMessage(msg);
        }
    }

    public void checkValidSocket() {
        for (ClientHandler c :
                clients) {
            if (c.getSocket().isClosed()) {
                clients.remove(c);
            }
        }
    }

}
