package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
            AuthService.connect();
            BlackList.blackListConnect();
//            String name = AuthService.getNickByLoginAndPass("login1","pass1");
            serverSocket = new ServerSocket(8189);
            System.out.println("server start, waiting connection...");
            while (true) {
                socket = serverSocket.accept();
                System.out.println("Connection");
                new ClientHandler(this, socket);
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

            AuthService.disconnect();

        }
    }

    public void subscribe(ClientHandler client) {
        clients.add(client);
        broadcastClientList();
    }

    public void unSubscribe(ClientHandler client) {
        clients.remove(client);
        broadcastClientList();
    }

    public void broadcastMsg(String nick, String msg) {
        checkValidSocket();

        for (ClientHandler c :
                clients) {
            if (!checkBlackList(nick, c.getNick())) {
                c.sendMessage(nick + msg);
            }
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

    public boolean checkNick(String nick) {
        for (ClientHandler client :
                clients) {
            if (client.getNick().equals(nick)) {
                return true;
            }
        }
        return false;
    }

    public void broadcastClientList() {
        StringBuilder sb = new StringBuilder();
        sb.append("/clientList ");
        for (ClientHandler c :
                clients) {
            sb.append(c.getNick() + " ");
        }
        String out = sb.toString();
        for (ClientHandler c :
                clients) {
            c.sendMessage(out);
        }
    }

    public void sendPersonalMSG(ClientHandler from, String nickTo, String msg) {
        for (ClientHandler c :
                clients) {
            if (c.getNick().equals(nickTo)) {
                c.sendMessage("from " + from.getNick() + ": " + msg);
                from.sendMessage("to " + nickTo + ": " + msg);
                return;
            }
        }
        from.sendMessage("NoNick");
    }

    public boolean checkBlackList(String wNick, String BNick) {
        try {
            if (!BlackList.getBL(wNick).isEmpty()) {
                if (BlackList.getBL(wNick).contains(BNick)) {
                    return true;
                } else {
                    return false;
                }
            } else return false;
        } catch (NullPointerException npe) {
            return false;
        }
    }

    public void sendPrivateMsg(ClientHandler from, String nickTo, String msg){
        for (ClientHandler c :
                clients) {
            if (c.getNick().equals(nickTo)) {
                c.sendMessage("/privateMSG " + "from " + from.getNick() + ": " + msg);
                from.sendMessage("/privateMSG " + "to " + nickTo + ": " + msg);
                return;
            }
        }

    }

}
