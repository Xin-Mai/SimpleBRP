package module;

import module.order.Goods;
import module.order.Logistics;
import sun.rmi.runtime.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Info {
    //国家名
    private String country;
    //总单数
    private int ordersNum=0;
    //总金额
    private float money=0;
    //总利润
    private float profit=0;
    //平均金额
    private float averMoney=0;
    //平均利润
    private float averProfit=0;
    //运费
    private List<Logistics> logistics=new ArrayList<>();
    //畅销单品
    private Map<String,Integer> bestSales;
    //不记颜色的畅销单品
    private Map<String,Integer> simpleBestSales;

    public Info(String country){
        this.country=country;
        bestSales=new HashMap<>();
        simpleBestSales=new HashMap<>();
    }
    public String getCountry() {
        return country;
    }

    public void setCountry(String contry) {
        this.country = country;
    }

    public int getOrdersNum() {
        return ordersNum;
    }

    public void setOrdersNum(int ordersNum) {
        this.ordersNum = ordersNum;
    }

    public void addOrdersNum(){
        this.ordersNum+=1;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public void addMoney(float money){
        this.money+=money;
    }

    public void addLog(Logistics l){this.logistics.add(l);}

    public float getTotalLog(){
        float log=0;
        for(Logistics l:logistics)
            log+=l.getMoney();
        return log;
    }

    public float getProfit() {
        return profit;
    }

    public void setProfit(float profit) {
        this.profit = profit;
    }

    public void addProfit(float profit) {
        this.profit += profit;
    }

    public float getAverMoney() {
        if(ordersNum>0)
            averMoney=money/ordersNum;
        return averMoney;
    }

    public void setAverMoney(float averMoney) {
        this.averMoney = averMoney;
    }

    public float getAverProfit() {
        if(ordersNum>0)
            averProfit=profit/ordersNum;
        return averProfit;
    }

    public void setAverProfit(float averProfit) {
        this.averProfit = averProfit;
    }

    public Map<String, Integer> getBestSales() {
        return bestSales;
    }

    public void addBestSales(List<Goods> goods) {
        for(Goods g:goods){
            String id=g.getId();
            String type;
            if(id.length()<=5)
                type=id;
            else if(id.contains("-"))
                type=id.substring(0,id.indexOf("-"));
            else
                type=id;
            if(simpleBestSales.keySet().contains(type))
            {
                simpleBestSales.put(type,simpleBestSales.get(type)+g.getQuantity());
                if(bestSales.keySet().contains(g.getId())){
                    bestSales.put(id,bestSales.get(id)+g.getQuantity());
                }
            }
            else{
                bestSales.put(id,g.getQuantity());
                simpleBestSales.put(type,g.getQuantity());
            }
        }
    }

    public Map<String, Integer> getSimpleBestSales() {
        return simpleBestSales;
    }

    public void setSimpleBestSales(Map<String, Integer> simpleBestSales) {
        this.simpleBestSales = simpleBestSales;
    }
}
