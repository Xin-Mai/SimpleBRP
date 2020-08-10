package controller;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.HBox;
import module.Info;
import module.dataDA.DataManageable;

import java.util.List;

public class DataController {
    private DataManageable DA;
    public BarChart bar_chart;
    public HBox dataBox;
    private List<Info> infoList;


    public DataController(){

    }

    public DataController(DataManageable DA){
        this.DA=DA;
        this.infoList=DA.getData();
    }

    public DataManageable getDA() {
        return DA;
    }

    public void setDA(DataManageable DA) {
        this.DA = DA;
    }

    @FXML
    private void initialize(){

    }

    public void init(){
        if(this.infoList==null)
            this.infoList=this.DA.getData();
        if(this.bar_chart!=null){
            XYChart.Series series=new XYChart.Series();
            for(int i=0;i<5&&i<infoList.size();i++){
                Info info=infoList.get(i);
                series.getData().add(new XYChart.Data<String,Integer>(info.getCountry(),info.getOrdersNum()));
            }
            series.setName("销量");
            bar_chart.getData().add(series);
        }
    }

    public BarChart getBar_chart() {
        return bar_chart;
    }

    public void setBar_chart(BarChart bar_chart) {
        this.bar_chart = bar_chart;
    }

    public HBox getDataBox() {
        return dataBox;
    }

    public void setDataBox(HBox dataBox) {
        this.dataBox = dataBox;
        this.bar_chart=(BarChart<String,Integer>)dataBox.getChildren().get(0);
    }

    public void setInfoList(List<Info> infos){
        this.infoList=infos;
    }
}
