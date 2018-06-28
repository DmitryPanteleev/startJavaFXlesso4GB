package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.Vector;

public class Server {

    private Vector<ClientHandler> clients;

    public static void StartServer() {

        ServerSocket serverSocket = null;
        Socket socket = null;

        try {
            serverSocket = new ServerSocket(8189);
            System.out.println("server start, waiting connection...");

            socket = serverSocket.accept();
            System.out.println("Connection");

            final DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            final DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            final Scanner scanner = new Scanner(System.in);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            String str = inputStream.readUTF();
                            if (str.equals("/end")){
                                outputStream.writeUTF("/end");
                                break;
                            }
                            System.out.println("Client: " + str);
                            outputStream.writeUTF(str);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
//            while (true) {
//                Scanner scanner = new Scanner(System.in);
//                String str = scanner.nextLine();
//                if (str.equals("end")) break;
//                outputStream.writeUTF("server: " + str);
//                outputStream.flush();
////                outputStream.writeUTF(inputStream.readUTF() + "\n");
//
//            }

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
}
