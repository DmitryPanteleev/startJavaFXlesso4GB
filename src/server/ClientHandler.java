package server;

import sample.Controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Vector;

public class ClientHandler {

    public Socket getSocket() {
        return socket;
    }

    private Server server;
    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private String nick;


    public ClientHandler(Server server, Socket socket) {
        try {
            this.server = server;
            this.socket = socket;
            this.inputStream = new DataInputStream(socket.getInputStream());
            this.outputStream = new DataOutputStream(socket.getOutputStream());

            new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println(",hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
                    try {
                        while (true) {
                            String string = inputStream.readUTF();
                            if (string.startsWith("/auth")) {
                                String[] token = string.split(" ");
                                System.out.println(token[1]+ " " + token[2]);
                                String newNick = AuthService.getNickByLoginAndPass(token[1], token[2]);
                                if (newNick != null) {
                                    sendMessage("/authOk");
                                    nick = newNick;
                                    System.out.println(nick);
                                    server.subscribe(ClientHandler.this);
                                    break;
                                } else System.out.println("не верный логин/пароль");
                                sendMessage("не верный логин/пароль");
                            }
                        }
                        while (true) {
                            String string = inputStream.readUTF();
                            if (string.equals("/end")) {
                                outputStream.writeUTF("/serverClosed");
                                break;
                            }
                            server.broadcastMsg(nick + " |" +
                                    LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:SS"))+
                                    "|>  " +string);
                            System.out.println("Client: " + string);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            outputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();

        } catch (
                IOException e)

        {
            e.printStackTrace();
        }

    }


    public void sendMessage(String msg) {
        try {
            outputStream.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
