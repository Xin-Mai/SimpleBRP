package sample;

import dataDA.DataDA;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Pagination;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import view.PageManager;

import java.io.IOException;

public class Controller {
    //alt+enter即可把组件映射到这里
    public FlowPane pane;
    public VBox pageLoader;

    //查看全部订单
    public void handleCheckButtonAction(ActionEvent actionEvent) {
        DataDA dataDA = new DataDA();
        PageManager manager = new PageManager(dataDA.search(),pageLoader);
        Pagination pagination = new Pagination();
        pagination.setPadding(new Insets(0,20,10,20));
        pagination.setPageFactory((Integer pageIndex) -> manager.createPage(pageIndex));
        pane.getChildren().add(pagination);
    }
    //管理
    public void handleManageButtonAction(ActionEvent actionEvent) {
    }

    //新建订单
    public void handleAddButtonAction(ActionEvent actionEvent) {
    }

    //搜索订单
    public void handleSearchButtonAction(ActionEvent actionEvent) {
    }

    //查看不同的统计数据
    public void handleDataButtonAction(ActionEvent actionEvent) {
    }
}
