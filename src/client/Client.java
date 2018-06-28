package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final String SERVER_ADRESS = "localhost";
    private static final int SERVER_PORT = 8189;
    private static Socket socket;
    private static Scanner scanner;
    private static DataInputStream inputStream;
    private static DataOutputStream outputStream;

    public static void startClient() {
        try {
            socket = new Socket(SERVER_ADRESS, SERVER_PORT);
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            String str = inputStream.readUTF();
                            System.out.println(str);
                        }
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }
            }).start();
            while (true) {
                scanner = new Scanner(System.in);
                String string = scanner.nextLine();
                outputStream.writeUTF("Client: " + string);
                outputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
