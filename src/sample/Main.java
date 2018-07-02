package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {


    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Single chat");

        welcome();




    }



    private void welcome() throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

        Scene sceneWelcome = new Scene(root, 500, 400);

        primaryStage.setScene(sceneWelcome);
        //  scene.getStylesheets().add((getClass().getResource("/css/Styles.css")).toExternalForm());
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
