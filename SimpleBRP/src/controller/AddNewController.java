package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class AddNewController {
    public TextField orderId;
    private GridPane pane;
    public  AddNewController()
    {

    }
    @FXML
    private void initialize() {
    }


    public AddNewController(MainController m)
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/addNew.fxml"));
        try{
            pane=loader.load();
        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public GridPane getPane() {
        return pane;
    }
}
