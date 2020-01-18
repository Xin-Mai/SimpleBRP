package sample;

import dataDA.DataDA;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.*;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../view/main.fxml"));
        primaryStage.setTitle("卖表专用系统XD");
        Scene scene = new Scene(root,1067,600);
        scene.getStylesheets().add(getClass().getResource("../view/mainStyle.css").toExternalForm());
        HBox box=new HBox();
        javafx.scene.control.Label l=new Label("1");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
