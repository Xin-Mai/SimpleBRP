package view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PageManager {
    private Pagination pagination;
    static final String[] TITLES = {"订单号","订单金额","型号","成本","利润","利润率","收货国家","物流方式"};

    /**每页10条信息*/
    public int itemPerPage()
    {
        return 10;
    }

    /**用于回调的Page Factory方法*/
    public VBox createPage(int pageIndex)
    {
        //上下相距5个像素？
        VBox box = new VBox(5);
        box.getChildren().add(getTitle());
        int page = pageIndex*itemPerPage();
        for(int i=page;i<=page+itemPerPage();i++)
        {
            VBox item = new VBox();
        }
        return box;
    }

    /**获取抬头*/
    private HBox getTitle()
    {
        HBox box = new HBox(10);
        box.setPrefHeight(40);
        box.setAlignment(Pos.CENTER);
        for(String s:TITLES)
        {
            box.getChildren().add(new Label(s));
        }
        return box;
    }
}
