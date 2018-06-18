package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Field;

public class Welcome {
    static String login;

    @FXML
    TextField loginField;

    @FXML
    Button btnLogin;

    public void sendLogin(ActionEvent actionEvent) throws IOException {
        if (!loginField.getText().equals("")) {
            login = loginField.getText();

            loginField.clear();
            btnLogin.setDisable(true);
            loginField.setDisable(true);
            openChat();

        } else loginField.requestFocus();
    }

    @FXML
    public void openChat() throws IOException {
        Stage stage = (Stage) loginField.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        stage.getScene().setRoot(root);

        stage.show();
    }
    public void Close(ActionEvent actionEvent) {

        System.exit(1);

    }


}
