package view;

import dataDA.DataDA;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.Pagination;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextAlignment;
import order.Goods;
import order.Order;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static javafx.geometry.Pos.CENTER;


public class PageManager {
    private Pagination pagination;
    static final String[] TITLES = {"订单号","订单金额","型号","成本","利润","利润率","收货国家","物流方式"};
    private List<Order> orders;
    @FXML
    private VBox pageLoader;
    private List<String[]> content;

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
    public PageManager(List<Order> o,VBox parent){this.orders=o;this.pageLoader=parent;}

    /**用于回调的Page Factory方法*/
    public VBox createPage(int pageIndex) {
        if(!pageLoader.isVisible())
            pageLoader.setVisible(true);
        pageLoader.setPrefSize(1067,500);
        int i;
        int page = pageIndex*itemPerPage();
        //每3页加载一次
        if(pageIndex%3==0)
            content=preLoad(page);
        for(i=0;i<itemPerPage()&&(page+i<orders.size());i++)
        {
            HBox item = (HBox)pageLoader.getChildren().get(i+1);
            String[] strings = content.get((page+i)%30);
            List<Node> nodes=item.getChildren();
            //订单号使用超链接
            Hyperlink id = (Hyperlink)nodes.get(0);
            id.setText(strings[0]);
            //订单金额（美元）
            Label money = (Label)nodes.get(1);
            money.setText(strings[1]);
            //商品编码
            //超过一件商品只显示其中两件
            Label goods = (Label)nodes.get(2);
            goods.setText(strings[2]);
            goods.setStyle(" -fx-text-fill: blue;-fx-font-size: 12px;");
            //成本
            Label cost =(Label) nodes.get(3);
            cost.setText(strings[3]);
            //利润
            Label profit = (Label)nodes.get(4);
            profit.setText(strings[4]);
            //利润率
            Label profitRate = (Label)nodes.get(5);
            profitRate.setText(strings[5]);
            //收货国家
            Label country = (Label)nodes.get(6);
            country.setText(strings[6]);
            //物流方式
            Label server = (Label)nodes.get(7);
            server.setText(strings[7]);
        }
        if(i<itemPerPage())
            clearBox(itemPerPage()-i);
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
    /**清空内容*/
    private void clearBox()
    {
        for(int i=1;i<=10;i++)
        {
            HBox item =(HBox) pageLoader.getChildren().get(i);
            for(Node n:item.getChildren())
            {
                if(n instanceof Label||n instanceof Hyperlink)
                    ((Labeled) n).setText("");
            }
        }
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
}
