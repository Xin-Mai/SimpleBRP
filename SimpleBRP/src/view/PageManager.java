package view;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import order.Goods;
import order.Order;

import java.util.List;

public class PageManager {
    private Pagination pagination;
    static final String[] TITLES = {"订单号","订单金额","型号","成本","利润","利润率","收货国家","物流方式"};
    private List<Order> orders;

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

    /**用于回调的Page Factory方法*/
    public VBox createPage(int pageIndex)
    {
        //上下相距5个像素？
        VBox box = new VBox(10);
        //初始化表头
        box.getChildren().add(getTitle());
        int page = pageIndex*itemPerPage();
        for(int i=page;i<=page+itemPerPage()&&i<orders.size();i++)
        {
            HBox item = new HBox();
            item.setSpacing(15);
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
            VBox goodsBox = new VBox();
            if(order.getGoods().size()>1)
            {
                for(Goods g:order.getGoods())
                {
                    goodsBox.getChildren().add(new Label(g.getId()));
                }
            }
            Label goods = new Label();
            goods.setText(order.getGoods().get(0).getId());
            //成本
            Label cost = new Label(""+order.getCost());
            //利润
            Label profit = new Label(""+order.getProfit());
            //利润率
            Label profitRate = new Label(order.getProfitRate()*100+"%");
            //收货国家
            Label country = new Label(order.getCountry());
            //物流方式
            Label server = new Label(order.getLogistics().getServer());
            if(order.getGoods().size()==1)
                item.getChildren().addAll(id,money,goods,cost,profit,profitRate,country,server);
            else
                item.getChildren().addAll(id,money,goodsBox,cost,profit,profitRate,country,server);
            box.getChildren().add(item);
        }
        return box;
    }

    /**获取抬头*/
    private HBox getTitle()
    {
        HBox box = new HBox(10);
        box.setPrefHeight(40);
        box.prefWidth(250);
        box.setAlignment(Pos.CENTER);
        for(String s:TITLES)
        {
            box.getChildren().add(new Label(s));
        }
        return box;
    }
}
