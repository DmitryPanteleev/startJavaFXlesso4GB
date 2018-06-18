package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;


public class Controller implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        textArea.setFocusTraversable(false);
    }

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



    public void sendMsg() {
        if (textField.getText() != null && !textField.getText().equals("") && login != null) {
            textArea.appendText(login + "|" + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:SS")) + "|>>  " + textField.getText() + "\n");

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
