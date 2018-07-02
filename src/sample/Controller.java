package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import server.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalTime;


public class Controller {

    final String IP_ADRESS = "localhost";
    final int PORT = 8189;
    Socket socket;
    int start;
    DataOutputStream outputStream;
    DataInputStream inputStream;
    Server server;
    @FXML
    TextArea textArea;
    @FXML
    TextField textField;
    @FXML
    Button btn1;
    @FXML
    Button btnClose;
    @FXML
    Button btnLogin;
    @FXML
    ListView clientList;

    //    String login = Welcome.login;
    @FXML
    HBox buttonPanel;
    @FXML
    HBox upperPanel;
    @FXML
    TextField loginField;
    @FXML
    PasswordField passwordField;
    private boolean isAuthorized;

    public void setAuthorized(boolean isAuthorized) {
        this.isAuthorized = isAuthorized;
        System.out.println(isAuthorized);
        if (!isAuthorized) {
            upperPanel.setVisible(true);
            upperPanel.setManaged(true);
            buttonPanel.setVisible(false);
            buttonPanel.setManaged(false);
            clientList.setVisible(false);
            clientList.setManaged(false);
        } else {
            upperPanel.setVisible(false);
            upperPanel.setManaged(false);
            buttonPanel.setVisible(true);
            buttonPanel.setManaged(true);
            clientList.setVisible(true);
            clientList.setManaged(true);
        }
    }


    public void connect() {
//        textArea.setFocusTraversable(false);
//        loginField.setFocusTraversable(true);
        try {
            socket = new Socket(IP_ADRESS, PORT);
//            while (true) {
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

                        while (true) {
                            String string = inputStream.readUTF();
                            if (string.startsWith("/authOk")) {
                                System.out.println("new authOk");
                                setAuthorized(true);
                                new Thread(() -> unLoginTwoMin()).start();
                                break;
                            } else {
                                textArea.appendText(string + "\n");
                            }

                        }


                        while (true) {
                            String string = inputStream.readUTF();
                            if (string.startsWith("/")) {
                                System.out.println(string);
                                if (string.equals("/serverClosed")) {
                                    break;
                                }
                                if (string.startsWith("/clientList")) {
                                    String[] token = string.split(" ");
                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            clientList.getItems().clear();
                                            for (int i = 1; i < token.length; i++) {
                                                clientList.getItems().add(token[i]);
                                            }
                                        }
                                    });

                                }
                            } else {
                                textArea.appendText(string + "\n");
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();

                    } finally {
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
        } finally {
//            try {
//                socket.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }

    }


    public void sendMsg() {
        if (textField.getText() != null && !textField.getText().equals("")) {
            try {
                outputStream.writeUTF(textField.getText());
                start = LocalTime.now().toSecondOfDay();

            } catch (Exception e) {
                e.printStackTrace();
            }
            textField.clear();
            textField.requestFocus();
        } else
            textField.setPromptText(" Привет, напиши что-нибудь");
        textField.requestFocus();
    }

    public void Close(ActionEvent actionEvent) {

        try {
//            if (server.getClients().contains(this)) {
            outputStream.writeUTF("/end");

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(101);

    }

    public void tryToAuth(ActionEvent actionEvent) {
        if (socket == null || socket.isClosed()) {
            connect();
        }

        try {
            outputStream.writeUTF("/auth " + loginField.getText() + " " + passwordField.getText());
            loginField.clear();
            passwordField.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


//    public void sendLogin(ActionEvent actionEvent) {
//        if (!loginField.getText().equals("")) {
//            login = loginField.getText();
//            loginField.clear();
//            btnLogin.setDisable(true);
//            loginField.setDisable(true);
//            textField.requestFocus();
//        } else loginField.requestFocus();
//    }

    public void unLoginTwoMin() {
        System.out.println("unAuthorizzzzStart");
        start = LocalTime.now().toSecondOfDay();
        while (true) {
            int end = LocalTime.now().toSecondOfDay();
//            System.out.println(end - start);
            if ((end - start) >= 120) {
                try {
                    System.out.println("/unAuth");
                    outputStream.writeUTF("/unAuth");
//                    inputStream.close();
                    outputStream.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                setAuthorized(false);
                start = LocalTime.now().toSecondOfDay();
                break;
            }
        }

    }
}