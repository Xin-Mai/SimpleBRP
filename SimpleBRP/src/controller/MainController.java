package controller;

import controller.thread.LoadThread;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Pagination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import module.dataDA.DataAssistant;
import module.dataDA.DataManageable;
import module.order.Order;

import javax.security.auth.callback.Callback;
import java.io.IOException;
import java.util.List;

public class MainController {
    //alt+enter即可把组件映射到这里
    @FXML
    public Pagination ordersList;
    @FXML
    public VBox page;
    //如果不起名字叫xxController就会自动生成Controller失败
    @FXML
    private PageManager pageController;

    //数据类
    public DataManageable DA;
    //private PageManager pageManager;


    @FXML
    private void initialize(){

    }
    public MainController() {
        //pageManager=new PageManager();
        DA=new DataAssistant();
        //init();
    }

    public void init(){
        //pageManager.setPageLoader(page);
        LoadThread load=new LoadThread(pageController,DA);
        Thread loadThread=new Thread(load,"load");
        loadThread.start();
        ordersList.setPageFactory((Integer pageIndex)->pageController.createPage(pageIndex));
        ordersList.setPageCount(DA.getDataSize()/10);
    }
}
