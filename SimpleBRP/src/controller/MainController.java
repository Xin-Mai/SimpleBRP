package controller;

import dataDA.DataDA;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.LoadException;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import order.Order;
import view.PageManager;

import java.io.IOException;
import java.util.List;

public class MainController {
    //alt+enter即可把组件映射到这里
    public HBox pane;
    public Button search;
    public TextField search_content;
    public VBox page;
    private VBox data;
    private Pagination pagination;


    //查看全部订单
    public void handleCheckButtonAction(ActionEvent actionEvent) {
        pane.getChildren().remove(data);
        DataDA dataDA = new DataDA();
        List<Order> orders=dataDA.search();
        PageManager manager = new PageManager(orders,page);
        if(pagination==null)
        {
            pagination = new Pagination();
            pagination.setPrefSize(1067,500);
            pagination.setPadding(new Insets(0,20,10,20));
            pane.getChildren().add(pagination);
        }
        pagination.setMaxPageIndicatorCount(5);
        pagination.setPageCount(orders.size()/10+1);
        pagination.setPageFactory((Integer pageIndex) -> manager.createPage(pageIndex));
        pane.setPadding(new Insets(5,5,0,5));
        dataDA.close();
    }
    //管理
    public void handleManageButtonAction(ActionEvent actionEvent) {
    }

    //新建订单
    public void handleAddButtonAction(ActionEvent actionEvent) {
        AddNewController controller = new AddNewController(this);
        Dialog<Order> dialog = new Dialog<>();
        dialog.setTitle("添加新的订单");
        GridPane gridPane=controller.getPane();
        dialog.getDialogPane().setContent(gridPane);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK,ButtonType.CANCEL);
        dialog.setResultConverter(button->{
            if(button==ButtonType.OK)
            {
                TextField t=(TextField)gridPane.lookup("#orderId");
                return new Order(t.getText());
            }
            else
                return null;
        });
        dialog.show();
    }

    //搜索订单
    public void handleSearchButtonAction(ActionEvent actionEvent) {
        pane.getChildren().remove(data);
        String content = search_content.getText();
        DataDA dataDA = new DataDA();
        List<Order> orders=dataDA.search(content);
        PageManager manager = new PageManager(orders,page);
        if(pagination==null)
        {
            pagination = new Pagination();
            pagination.setPrefSize(1067,500);
            pagination.setPadding(new Insets(0,20,10,20));
            pane.getChildren().add(pagination);
        }
        pagination.setMaxPageIndicatorCount(5);
        pagination.setPageCount(orders.size()/10+1);
        pagination.setPageFactory((Integer pageIndex) -> manager.createPage(pageIndex));
        dataDA.close();
    }

    //查看不同的统计数据
    public void handleDataButtonAction(ActionEvent actionEvent) {
        if(pagination!=null)
            pane.getChildren().removeAll(pagination,page);
        else
            pane.getChildren().remove(page);
        DataController controller = new DataController(this);
        data=controller.getDataBox();
        pane.getChildren().add(data);
    }

    public HBox getPane() {
        return pane;
    }
}
