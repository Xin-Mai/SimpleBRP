package module.order;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Order {
    //订单编号
    private String id;
    //下单日期
    private Date orderTime;
    //付款日期
    private Date payTime;
    //总金额
    private float money;
    //商品列表
    private List<Goods> goods;
    //物流信息
    private Logistics logistics;
    //收货国家
    private String country;

    //佣金比例
    final static float RATE =(float) 0.08;
    //美元汇率
    final static float DOLLARS_RATE = (float)6.9;

    public Order(String id)
    {
        this.id=id;
        this.orderTime= Calendar.getInstance().getTime();
        this.payTime=null;
        this.money=0;
        this.goods=new ArrayList<>();
        this.logistics=new Logistics();
    }
    public Order(String id,List<Goods> goods,float money,Logistics logistics,String country)
    {
        this.country=country;
        this.id=id;
        this.goods=goods;
        this.money=money;
        this.logistics=logistics;
    }

    public Order(String id,String orderDate,String payDate,float money,List<Goods> goods,Logistics logistics,String country)
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        this.country=country;
        this.id=id;
        this.goods=goods;
        this.logistics=logistics;
        try{
            this.orderTime=format.parse(orderDate);
            this.payTime=format.parse(payDate);
        }catch (ParseException e)
        {
            e.printStackTrace();
        }
        this.money=money;
    }

    //添加商品
    public void addGoods(Goods goods)
    {
        this.goods.add(goods);
    }
    //删除商品
    public boolean deleteGoods(Goods goods)
    {
        if(this.goods.contains(goods))
        {
            this.goods.remove(goods);
            return true;
        }
        else
            return false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public void setOrderTime(String time){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-mm-dd hh:mm");
        try {
            this.orderTime=simpleDateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public void setPayTime(String date){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-mm-dd hh:mm");
        try {
            this.payTime=simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public float getMoney()
    {
        return money*DOLLARS_RATE;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public List<Goods> getGoods() {
        return goods;
    }

    public void setGoods(List<Goods> goods) {
        this.goods = goods;
    }

    public Logistics getLogistics() {
        return logistics;
    }

    public void setLogistics(Logistics logistics) {
        this.logistics = logistics;
    }

    public String getSqlData()
    {
        String sql;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        sql="('"+id+"','"+format.format(orderTime)+"','"+format.format(payTime)+"',"
                +getMoney()+",";
        for(Goods g:goods)
        {
            sql=sql+"'"+g.getId()+"',";
        }
        sql=sql+"'"+country+"','"+logistics.getId()+"')";
        return sql;
    }

    /**获取成本*/
    public float getCost()
    {
        float cost=0;
        for(Goods g:goods)
            cost+=g.getCost();
        return cost;
    }
    /**获取利润*/
    public float getProfit()
    {
        //售价减运费和8%的佣金
        float profit=DOLLARS_RATE*(1-RATE)*money-logistics.getMoney();
        //减成本
        profit-=getCost();
        return profit;
    }

    /**路润率*/
    public float getProfitRate()
    {
        return getProfit()/(money*DOLLARS_RATE);
    }
}
