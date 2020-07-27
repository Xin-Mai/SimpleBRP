package controller;

import com.jfoenix.controls.JFXListCell;
import javafx.geometry.Pos;
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

    public OrderCell(){
        super();
    }

    @Override
    protected void updateItem(Order item,boolean empty){
        //super.updateItem(item,empty);
        if(!empty)
        {
            //下面是自定义的内容
        HBox cell=new HBox();
        cell.setAlignment(Pos.CENTER_LEFT);
        cell.setSpacing(15);
        //订单号使用超链接
        Hyperlink id = new Hyperlink(item.getId());
        //订单金额（美元）
        Label money = new Label("$"+item.getMoney());
        money.setPrefWidth(80);
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
        good.setPrefWidth(150);
        //成本
        Label cost =new Label(item.getCost()+"");
        cost.setPrefWidth(80);
        //利润
        Label profit = new Label(String.format("%.2f",item.getProfit()));
        profit.setPrefWidth(80);
        //利润率
        Label profitRate = new Label(String.format("%.2f",item.getProfitRate()*100)+"%");
        profitRate.setPrefWidth(80);
        //收货国家
        Label country = new Label(item.getCountry());
        country.setPrefWidth(150);
        //物流方式
        Label server = new Label(item.getLogistics().getServer());
        server.setPrefWidth(180);
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
