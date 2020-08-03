package controller;

import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import module.order.Order;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class PageManager {
    //private Pagination pagination;
    static final String[] TITLES = {"订单号","订单金额","型号","成本","利润","利润率","收货国家","物流方式"};
    //每页一个的listView
    public JFXListView list;
    private List<Order> orders;
    @FXML
    private VBox pageLoader;
    private List<String[]> content;
    private MainController mainController;

    /**每页10条信息*/
    public int itemPerPage()
    {
        return 10;
    }

    /**初始化资源*/
    public PageManager(){
    }
    public PageManager(List<Order> o)
    {
        this.orders=o;
    }
    public PageManager(List<Order> o,VBox parent){this.orders=o;this.pageLoader=parent;}

    /**用于回调的Page Factory方法*/
    public VBox createPage(int pageIndex) {
        //不知道为啥是Null
        pageLoader.setPrefSize(1067,500);
        int i;
        int page = pageIndex*itemPerPage();
        if(list!=null){
            //输入List的内容
            ObservableList<Order> listContent=FXCollections.observableArrayList();
            for(i=0;i<10&&page+i<orders.size();i++){
                listContent.add(orders.get(page+i));
            }
            list.setItems(listContent);
            list.setCellFactory(new Callback<ListView, ListCell>() {
                @Override
                public ListCell call(ListView param) {
                    OrderCell cell=new OrderCell();
                    return cell;
                }
            });


        }
        return pageLoader;
    }

    /**贵定各项内容长度，规范化*/
    private String lengthFormat(String s,int length)
    {
        String s1=s;
        for(int i=s.length();i<length;i++)
            s1+="  ";
        return s1;
    }

    private void clearBox(int j)
    {
        for(int i=j;i<=10;i++)
        {
            HBox item =(HBox) pageLoader.getChildren().get(i);
            for(Node n:item.getChildren())
            {
                if(n instanceof Label||n instanceof Hyperlink)
                    ((Labeled) n).setText("");
            }
        }
    }

    /**预加载3页*/
    private List<String[]> preLoad(int page)
    {
        List<String[]> content = new ArrayList<>();
        for(int i=page;i<page+30&&i<orders.size();i++)
        {
            Order o=orders.get(i);
            String[] strings = new String[8];
            //单号
            strings[0]=o.getId();
            //价格
            strings[1]="$"+o.getMoney();
            //商品编码
            String s=o.getGoods().get(0).getId();
            if(o.getGoods().size()>=2)
                s+='\n'+o.getGoods().get(1).getId()+"...";
            strings[2]=s;
            //成本
            strings[3]=o.getCost()+"";
            //利润
            s=o.getProfit()+"";
            s=s.substring(0,s.indexOf(".")+3);
            strings[4]=s;
            //利润率
            s=o.getProfitRate()*100+"";
            s=s.substring(0,s.indexOf(".")+3)+"%";
            strings[5]=s;
            //收货国家
            strings[6]=lengthFormat(o.getCountry(),0);
            //物流方式
            strings[7]=o.getLogistics().getServer();
            content.add(strings);
        }
        return content;
    }

    /**查看详情*/
    public void handleOrderDetailAction(ActionEvent actionEvent) {
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public void setPageLoader(VBox pageLoader){
        //this.pageLoader=pageLoader;
    }
}
