package server;

import sample.Controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ClientHandler {

    private Server server;
    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private String nick;
    private String privareDialogNick;

    public ClientHandler(Server server, Socket socket) {
        try {
            this.server = server;
            this.socket = socket;
            this.inputStream = new DataInputStream(socket.getInputStream());
            this.outputStream = new DataOutputStream(socket.getOutputStream());


            Thread thread = new Thread(() -> {
                System.out.println(",hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
                try {
                    while (true) {
                        String string = inputStream.readUTF();

                        if (string.startsWith("/auth")) {
                            String[] token = string.split(" ");
                            System.out.println(token[1] + " " + token[2]);
                            String newNick = AuthService.getNickByLoginAndPass(token[1], token[2]);
                            if (newNick != null) {
                                if (!server.checkNick(newNick)) {

                                    nick = newNick;
                                    sendMessage("/authOk " + nick);

                                    System.out.println(nick);
                                    server.subscribe(this);
                                    break;
                                } else sendMessage("Login занят");

                            } else sendMessage("не верный логин/пароль");
                        }
                    }
                    while (true) {
                        String string = inputStream.readUTF();
                        if (string.startsWith("/")) {
                            if (string.equals("/end")) {
                                outputStream.writeUTF("/serverClosed");
                                break;
                            }
                            if (string.startsWith("/w ")) {
                                String[] token = string.split(" ", 3);
                                server.sendPersonalMSG(this, token[1], token[2]);
                            }
                            if (string.startsWith("/blacklist")) {
                                String[] token = string.split(" ");
                                BlackList.addBL(getNick(), token[1]);
                                System.out.println(getNick());
                                sendMessage(token[1] + " добавлен в чёрный список");
                            }
                            if (string.equals("/unAuth")) {
                                server.unSubscribe(this);
                                nick = null;
                            }
                            if (string.startsWith("/private ")) {
                                String[] tokens = string.split(" ");
                                privareDialogNick = tokens[1];
                            }
                            if (string.startsWith("/privat")) {
                                String[] token = string.split(" ", 2);
                                server.sendPrivateMsg(this, privareDialogNick, token[2]);
                            }
                            if (string.startsWith("/kill")){
                                String[] tokens = string.split(" ");
                                server.sendPersonalMSG(this, tokens[1], "/CLOSE");
                            }
                        } else {
                            server.broadcastMsg(nick, " |" +
                                    LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:SS")) +
                                    "|>  " + string);
                            System.out.println("Client: " + string);
                        }
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
                    server.unSubscribe(this);
                }
            });
            thread.setDaemon(true);
            thread.start();


        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public Socket getSocket() {
        return socket;
    }


    public String getNick() {
        return nick;
    }

    public void sendMessage(String msg) {
        try {
            outputStream.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendPrivateMsg(String msg) {

    }

}
