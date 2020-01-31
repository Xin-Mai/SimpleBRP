package controller;

import dataDA.DataDA;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import order.Goods;
import order.Order;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

public class ListController {
    public HBox title;
    private GridPane gridPane;

    @FXML
    private void initialize() {
    }

    /**用dialog展示详细信息*/
    public void handleOrderDetailAction(ActionEvent actionEvent) {
        Hyperlink link=(Hyperlink)actionEvent.getSource();
        String id=link.getText();
        loadPane();
        initPane(id);
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("添加新的订单");
        dialog.getDialogPane().setContent(gridPane);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);
        dialog.setResultConverter(button->{
            if(button==ButtonType.CLOSE)
            {
                return true;
            }
            else
                return null;
        });
        dialog.show();
    }

    private void loadPane()
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/addNew.fxml"));
        try{
            gridPane=loader.load();
        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void initPane(String id)
    {
        DataDA da=new DataDA();
        List<Order> orders=da.select("订单号",id);
        Order o=orders.get(0);
        da.fillLogistics(o);
        //设置id
        TextField t=(TextField)gridPane.lookup("#orderId");
        t.setText(id);
        //设置金额
        TextField m=(TextField)gridPane.lookup("#money");
        m.setText("$"+o.getMoney());
        //设置下单时间
        TextField time1=(TextField)gridPane.lookup("#time1");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        time1.setText(format.format(o.getOrderTime()));
        //设置付款时间
        TextField time2=(TextField)gridPane.lookup("#time2");
        time2.setText(format.format(o.getPayTime()));
        //商品编码
        TextField goodsId=(TextField)gridPane.lookup("#goodsId");
        List<Goods> goods=o.getGoods();
        String content="";
        for(Goods g:goods)
        {
            content=content+g.getId()+"\n";
        }
        goodsId.setText(content);
        //收货国家
        TextField country=(TextField)gridPane.lookup("#country");
        country.setText(o.getCountry());
        //物流服务
        ComboBox logServer =(ComboBox)gridPane.lookup("#logServer");
        logServer.setEditable(false);
        logServer.setValue(o.getLogistics().getServer());
        //物流单号
        TextField logId=(TextField)gridPane.lookup("#logId");
        logId.setText(o.getLogistics().getId());
        //订单重量
        TextField weight=(TextField)gridPane.lookup("#weight");
        weight.setText(o.getLogistics().getWeight()+"");
        //金额
        TextField logFee=(TextField)gridPane.lookup("#logFee");
        logFee.setText(o.getLogistics().getMoney()+"");
    }
}
