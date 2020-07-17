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

    //数据类
    public DataManageable DA;
    private PageManager pageManager;


    public MainController() {
        VBox pageLoader=null;
        try{
            Parent anchorPane=FXMLLoader.load(getClass().getResource("/view/page.fxml"));
            pageManager=new PageManager();
            pageLoader=(VBox)anchorPane.lookup("pageLoader");
        }catch (IOException e){
            e.printStackTrace();
        }
        //pageManager=new PageManager();
        //pageManager.setPageLoader(pageLoader);

        DA=new DataAssistant();
        //init();
    }

    public void init(){
        LoadThread load=new LoadThread(pageManager,DA);
        Thread loadThread=new Thread(load,"load");
        loadThread.start();
        ordersList.setPageFactory((Integer pageIndex)->pageManager.createPage(pageIndex));
    }
}
