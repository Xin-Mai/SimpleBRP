package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextAlignment;
import order.Goods;
import order.Order;

import java.io.IOException;
import java.util.List;

import static javafx.geometry.Pos.CENTER;


public class PageManager {
    private Pagination pagination;
    static final String[] TITLES = {"订单号","订单金额","型号","成本","利润","利润率","收货国家","物流方式"};
    private List<Order> orders;
    private VBox parent;

    /**每页10条信息*/
    public int itemPerPage()
    {
        return 10;
    }

    /**初始化资源*/
    public PageManager(List<Order> o)
    {
        this.orders=o;
    }
    public PageManager(List<Order> o,VBox parent){this.orders=o;this.parent=parent;}

    /**用于回调的Page Factory方法*/
    public VBox createPage(int pageIndex){
        //从fxml映射获取页面布局？
        VBox box = parent;
        //上下相距5个像素？
        //VBox box = new VBox(10);
        //初始化表头
        box.getChildren().add(getTitle());
        int page = pageIndex*itemPerPage();
        for(int i=page;i<=page+itemPerPage()&&i<orders.size();i++)
        {
            HBox item = new HBox();
            item.setSpacing(20);
            Order order = orders.get(i);
            //订单号使用超链接
            Hyperlink id = new Hyperlink();
            id.setText(order.getId());
            id.setOnAction((ActionEvent e)->{
                /**打开详情页面*/
            });
            //订单金额（美元）
            Label money = new Label("$"+order.getMoney());
            //商品编码
            //超过一件商品只显示其中两件
            VBox goodsBox = new VBox();
            if(order.getGoods().size()>1)
            {
                String s=order.getGoods().get(0).getId();
               Label l = new Label(lengthFormat(s,18));
               s=order.getGoods().get(1).getId();
               Label l2 =new Label(lengthFormat(s,18));
               Label l3 = new Label("...");
               l.setStyle(" -fx-text-fill: blue;-fx-font-size: 13px;");
               l2.setStyle(" -fx-text-fill: blue; -fx-font-size: 13px;");
               l3.setStyle(" -fx-text-fill: blue;-fx-font-size: 13px;");
               goodsBox.getChildren().addAll(l,l2,l3);
            }
            Label goods = new Label();
            String s=order.getGoods().get(0).getId();
            goods.setText(lengthFormat(s,18));
            goods.setStyle(" -fx-text-fill: blue;-fx-font-size: 13px;");
            //成本
            Label cost = new Label(""+order.getCost());
            //利润
            String p=order.getProfit()+"";
            p=p.substring(0,p.indexOf(".")+3);
            Label profit = new Label(p);
            //利润率
            p=order.getProfitRate()+"";
            p=p.substring(0,p.indexOf(".")+3);
            Label profitRate = new Label(p+"%");
            //收货国家
            Label country = new Label(lengthFormat(order.getCountry(),20));
            //物流方式
            Label server = new Label(order.getLogistics().getServer());
            if(order.getGoods().size()==1)
                item.getChildren().addAll(id,money,goods,cost,profit,profitRate,country,server);
            else
                item.getChildren().addAll(id,money,goodsBox,cost,profit,profitRate,country,server);
            box.getChildren().add(item);
        }
        box.setPadding(new Insets(10,10,30,10));
        box.setAlignment(CENTER);
        return box;
    }

    /**贵定各项内容长度，规范化*/
    private String lengthFormat(String s,int length)
    {
        String s1=s;
        for(int i=s.length();i<length;i++)
            s1+="  ";
        return s1;
    }
    /**获取抬头*/
    private HBox getTitle()
    {
        HBox box = new HBox(60);
        box.setPrefHeight(20);
        box.prefWidth(1067);
        box.maxHeight(20);
        box.setAlignment(CENTER);
        for(String s:TITLES)
        {
            Label l = new Label(s);
            l.setTextAlignment(TextAlignment.CENTER);
            l.setStyle(" -fx-text-weight: bold;-fx-font-size: 18px;-fx-text-fill: red;");
            box.getChildren().add(l);
        }
        return box;
    }
}
