package server;

import java.sql.SQLException;

public class StartServer {
    public static void main(String[] args) {

        Server server = new Server();
        try {
            server.StartServer();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
