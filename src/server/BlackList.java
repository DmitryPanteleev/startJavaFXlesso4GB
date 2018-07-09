package server;

import java.sql.*;
import java.util.ArrayList;

import static server.AuthService.getResultSet;

public class BlackList {

    private static Connection connection;
    private static Statement stmt;

    public static void blackListConnect() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:userDB.db");
            stmt = connection.createStatement();
            System.out.println("BlackListStarted");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

//    public static void addBL(String thisNick, String blackNick) {
//        String sql = String.format("INSERT INTO black_List (NN,BN) "
//                + "VALUES ('%s', '%s')", thisNick, blackNick);
//
//        System.out.println(sql);
//        try {
//            stmt.executeUpdate(sql);
//            System.out.println(sql);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//        try {
//            ResultSet rs = stmt.executeQuery(sql);
//            if (rs.next()) {
//                System.out.println(rs.getString(1));
//                return rs.getString(1);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    public static void addBL(String thisNick, String blackNick) {
        String sql = String.format("INSERT INTO black_List (NN,BN) "
                + "VALUES ('%s', '%s') ", thisNick, blackNick);
        String sql2 = String.format("INSERT INTO black_List (NN,BN) "
                + "VALUES ('%s', '%s') ", blackNick, thisNick);

        System.out.println(sql);
        try {
            stmt.executeUpdate(sql);
            stmt.executeUpdate(sql2);
            System.out.println(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static boolean checkBL(String wNick, String bNick) {
        String sql = String.format("SELECT BN FROM black_List "
                + "WHERE NN = '%s'", wNick);

        try {
            ResultSet resultSet = stmt.executeQuery(sql);
            if (resultSet.next()) {
                ArrayList list = (ArrayList) resultSet.getArray(1);
                if (list.contains(bNick)) return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static String getBL(String wNick) {
        String sql = String.format("SELECT BN FROM black_List "
                + "WHERE NN = '%s'", wNick);

        return getResultSet(sql);
    }
}
