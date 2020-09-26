package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import controller.thread.LoadThread;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Dialog;
import javafx.scene.control.Pagination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import module.dataDA.DataAssistant;
import module.dataDA.DataManageable;
import module.order.Order;

import javax.security.auth.callback.Callback;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MainController {
    //alt+enter即可把组件映射到这里
    @FXML
    public Pagination ordersList;
    @FXML
    public VBox page;
    @FXML
    public HBox data;
    public JFXButton updateButton;
    //如果不起名字叫xxController就会自动生成Controller失败
    //规则是fx:id+Controller
    @FXML
    private PageManager pageController;
    @FXML
    private DataController dataController;

    public Scene scene=null;

    //数据类
    public DataManageable DA;
    //private PageManager pageManager;


    @FXML
    private void initialize(){
        DA=new DataAssistant();
    }
    public MainController() {
        //pageManager=new PageManager();
        //DA=new DataAssistant();
    }

    public void init(){
        //pageManager.setPageLoader(page);
         //LoadThread load=new LoadThread(pageController,DA);
        //Thread loadThread=new Thread(load,"load");
        //loadThread.start();
        List<Order> orders=DA.getAll();
        ordersList.setPageFactory((Integer pageIndex)->pageController.createPage(pageIndex));
        ordersList.setPageCount(orders.size()/10);
        pageController.setOrders(orders);
        //dataController=new DataController(DA);
        dataController.setDA(DA);
        dataController.setDataBox(data);
        dataController.setInfoList(DA.getData());
        dataController.initBarChart();

    }

    public void init(Scene scene){
        this.scene=scene;
        this.init();
        PieChart pieChart=(PieChart)scene.lookup("#pie_chart");
        dataController.setPie_chart(pieChart);
        JFXComboBox country=(JFXComboBox)scene.lookup("#country");
        dataController.setCountry(country);
        JFXComboBox logis=(JFXComboBox)scene.lookup("#logis");
        dataController.setLogis(logis);
        dataController.initPieChart();
    }

    //更新数据文件
    public void updateDataResource(ActionEvent actionEvent) {
        //新建对话框
        List<String> choices=new ArrayList<>();
        choices.add("订单");
        choices.add("物流");
        choices.add("成本");
        ChoiceDialog<String> dialog=new ChoiceDialog<>("订单",choices);
        dialog.setTitle("提示框");
        dialog.setContentText("仅支持csv格式文件");
        dialog.setContentText("选择数据更新类型:");
        Optional<String> result=dialog.showAndWait();
        if(result.isPresent()){
            FileChooser fileChooser=new FileChooser();
            fileChooser.setTitle("更新数据文件");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(".csv","*.CSV"));
            //选择的新资源
            File file=fileChooser.showOpenDialog(scene.getWindow());
            if(file!=null) {
                //更新源文件
                DA.updateResFile(file, result.get());
                //更新页面数据
                updateView();
            }
        }
    }

    public void updateView(){
        List<Order> orders=DA.getAll();
        ordersList.setPageCount(orders.size()/10);
        ordersList.setCurrentPageIndex(0);
        pageController.setOrders(orders);

    }
}
