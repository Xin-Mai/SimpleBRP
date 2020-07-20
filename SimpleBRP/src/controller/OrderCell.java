package controller;

import com.jfoenix.controls.JFXListCell;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import module.order.Goods;
import module.order.Order;

import java.util.List;

/**
 * 本类用于ListView的cell生成
 */
public class OrderCell extends JFXListCell<Order> {

    @Override
    protected void updateItem(Order item,boolean empty){
        super.updateItem(item,empty);
        if(!empty)
        {
            //下面是自定义的内容
        HBox cell=new HBox();
        //订单号使用超链接
        Hyperlink id = new Hyperlink(item.getId());
        //订单金额（美元）
        Label money = new Label(""+item.getMoney());
        //商品编码
        //超过一件商品只显示其中两件
        List<Goods> goods=item.getGoods();
        String goodString="";
        if(goods.size()<=2){
            for(Goods g:goods)
                goodString+=g.toString();
        }
        else{
            goodString=goods.get(0).toString()+goods.get(1).toString()+"...";
        }
        Label good = new Label(goodString);
        good.setStyle(" -fx-text-fill: blue;-fx-font-size: 12px;");
        //成本
        Label cost =new Label(item.getCost()+"");
        //利润
        Label profit = new Label(item.getProfit()+"");
        //利润率
        Label profitRate = new Label(item.getProfitRate()*100+"%");
        //收货国家
        Label country = new Label(item.getCountry());
        //物流方式
        Label server = new Label(item.getLogistics().getServer());
        cell.getChildren().addAll(id,money,good,cost,profit,profitRate,country,server);
        //设置cell内容
            this.setGraphic(cell);
        }
        else{
            //否则为空
            this.setGraphic(null);
        }
    }
}
