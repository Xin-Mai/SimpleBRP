package sample;

import dataDA.DataDA;
import javafx.event.ActionEvent;
import javafx.scene.control.Pagination;
import javafx.scene.layout.FlowPane;
import view.PageManager;

public class Controller {
    //alt+enter即可把组件映射到这里
    public FlowPane pane;

    //查看全部订单
    public void handleCheckButtonAction(ActionEvent actionEvent) {
        DataDA dataDA = new DataDA();
        PageManager manager = new PageManager(dataDA.search());
        Pagination pagination = new Pagination();
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
