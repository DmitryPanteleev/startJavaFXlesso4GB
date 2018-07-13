package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import server.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalTime;

public class PrivateController {

    final String IP_ADRESS = "localhost";
    final int PORT = 8189;


    DataOutputStream outputStream;
    DataInputStream inputStream;
    Server server;
    Socket socket;
    @FXML
    TextArea privatTextArea;
    @FXML
    TextField privateTextField;


    public PrivateController() {
        System.out.println("private controller run");
        try {
            socket = new Socket(IP_ADRESS, PORT);

            outputStream = new DataOutputStream(socket.getOutputStream());
            inputStream = new DataInputStream(socket.getInputStream());


           new Thread(new Runnable() {
               @Override
               public void run() {
                   try{
                   while (true) {
                       String string = inputStream.readUTF();
                       if (string.startsWith("/privateMSG ")) {
                           String[] token = string.split(" ", 2);
                           privatTextArea.appendText(token[1]);
                       }
                   }}catch (IOException ioe){
                       ioe.printStackTrace();
                   }
               }
           }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
//            try {
//                inputStream.close();
//                outputStream.close();
//                socket.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }

    public void sendPrivateMsg() {
        System.out.println();

        if (privateTextField.getText() != null && !privateTextField.getText().equals("")) {
            try {
                outputStream.writeUTF("/privat " + privateTextField.getText());
                System.out.println(privateTextField.getText());


            } catch (Exception e) {
                e.printStackTrace();
            }
            privateTextField.clear();
            privateTextField.requestFocus();
        } else
            privateTextField.setPromptText(" Привет, напиши что-нибудь");
        privateTextField.requestFocus();
    }

}


