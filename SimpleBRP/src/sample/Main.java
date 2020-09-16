package sample;

import controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        URL location=getClass().getResource("/view/main.fxml");
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(location);
        Parent root = loader.load();
        primaryStage.setTitle("卖表专用系统XD");
        Scene scene = new Scene(root,1067,600);
        scene.getStylesheets().add(getClass().getResource("/view/mainStyle.css").toExternalForm());
        MainController mainController=loader.getController();
        mainController.init(scene);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
