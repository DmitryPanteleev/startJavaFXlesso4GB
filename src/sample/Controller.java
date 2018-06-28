package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;


public class Controller implements Initializable {

    final String IP_ADRESS = "localhost";
    final int PORT = 8189;
    Socket socket;
    DataOutputStream outputStream;
    DataInputStream inputStream;

    @FXML
    TextArea textArea;

    @FXML
    TextField textField;

    @FXML
    Button btn1;

    @FXML
    Button btnClose;

    String login = Welcome.login;

    @FXML
    TextField loginField;

    @FXML
    Button btnLogin;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        textArea.setFocusTraversable(false);
        try {
            socket = new Socket(IP_ADRESS, PORT);
            while (true) {
                inputStream = new DataInputStream(socket.getInputStream());
                outputStream = new DataOutputStream(socket.getOutputStream());
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


    public void sendMsg() {
        if (textField.getText() != null && !textField.getText().equals("") && login != null) {

//            textArea.appendText(login + "|" + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:SS")) + "|>>  " + textField.getText() + "\n");

            textField.clear();
            textField.requestFocus();


//            btn1.setVisible(false);
//        btn1.setManaged(false);

        } else
            textField.setPromptText(" Привет, напиши что-нибудь");
        textField.requestFocus();
    }

    public void Close(ActionEvent actionEvent) {

        System.exit(0);

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
