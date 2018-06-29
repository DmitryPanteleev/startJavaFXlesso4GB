package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import server.ClientHandler;
import server.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;


public class Controller {

    final String IP_ADRESS = "localhost";
    final int PORT = 8189;
    private boolean isAuthorized;
    Socket socket;
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

//    String login = Welcome.login;

    @FXML
    Button btnLogin;

    @FXML
    HBox buttonPanel;

    @FXML
    VBox upperPanel;

    @FXML
    TextField loginField;

    @FXML
    PasswordField passwordField;

    public void setAuthorized(boolean isAuthorized) {
        this.isAuthorized = isAuthorized;
        if (!isAuthorized) {
            upperPanel.setVisible(true);
            upperPanel.setManaged(true);
            buttonPanel.setVisible(false);
            buttonPanel.setManaged(false);
        } else {
            upperPanel.setVisible(false);
            upperPanel.setManaged(false);
            buttonPanel.setVisible(true);
            buttonPanel.setManaged(true);
        }
    }


    public void connect() {
        textArea.setFocusTraversable(false);
        loginField.setFocusTraversable(true);
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
                                setAuthorized(true);
                                break;
                            } else {
                                textArea.appendText(string + "\n");
                            }
                        }


                        while (true) {
                            String string = inputStream.readUTF();
                            if (string.equals("/serverClosed")) {
                                break;
                            }
                            textArea.appendText(string + "\n");
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

//        try {
//            if (server.getClients().contains(this)) {
//                outputStream.writeUTF("/end");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        System.exit(0);

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
}
