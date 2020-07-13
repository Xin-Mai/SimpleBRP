package controller;

import module.dataDA.DataDA;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import module.order.Order;

import java.util.List;

public class MainController {
    //alt+enter即可把组件映射到这里
    public HBox pane;
    public Button search;
    public TextField search_content;
    public VBox page;
    private VBox data;
    private Pagination pagination;
    List<Order> orders;

    public MainController()
    {
        DataDA dataDA = new DataDA();
        orders=dataDA.search();
        dataDA.close();
    }


    //查看全部订单
    public void handleCheckButtonAction(ActionEvent actionEvent) {
        pane.getChildren().remove(data);
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
    }

    //新建订单
    public void handleAddButtonAction(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("添加方式");
        alert.setHeaderText("请使用数据库导入添加订单：");
        alert.setContentText("将原始数据Excel表格导入数据库，数据即可更新");
        alert.show();

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
