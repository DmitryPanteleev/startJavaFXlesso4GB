package server;

import java.sql.*;
import java.util.ArrayList;

public class BlackList {

    private static Connection connection;
    private static Statement stmt;

    public BlackList() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:userDB.db");
            stmt = connection.createStatement();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void addBL(String thisNick, String blackNick) {
        String sql = String.format("INSERT INTO black_List(NN,BN) "
                + "VALUES (%s,%s)", thisNick, blackNick);

        System.out.println(sql);
        try {
            stmt.addBatch(sql);
            stmt.executeLargeBatch();
            System.out.println(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

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
    }

    public static ArrayList<String> getBL(String wNick) {
        String sql = String.format("SELECT BN FROM black_List "
                + "WHERE NN = '%s'", wNick);
        System.out.println(sql);
        try {
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                System.out.println(rs.getString(1));
                ArrayList list = (ArrayList) rs.getArray(1);
                return list;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean checkBL(String wNick, String bNick) {
        String sql = String.format("SELECT BN FROM black_List "
                + "WHERE NN = '%s'", wNick);

        try {
            ResultSet resultSet = stmt.executeQuery(sql);
            ArrayList list = (ArrayList) resultSet.getArray(1);
            if (list.contains(bNick)) return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }

}
