package server;

import java.sql.*;

public class AuthService {

    private static Connection connection;

    private static Statement stmt;

    public static String getResultSet(String sql) {

        try {
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                System.out.println(rs.getString(1));
                return rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void connect() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:userDB.db");
            stmt = connection.createStatement();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static String getNickByLoginAndPass(String login, String pass) {

        String sql = String.format("SELECT nickname FROM main "
                + "WHERE login = '%s' and password = '%s' ", login, pass);
        System.out.println(sql);

        return getResultSet(sql);

    }

    public static void disconnect() {

        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static String getBL(String wNick) {
        String sql = String.format("SELECT BN FROM black_List "
                + "WHERE NN = '%s'", wNick);

        return getResultSet(sql);
    }




}
