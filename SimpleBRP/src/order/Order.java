package order;

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

    public Order(String id)
    {
        this.id=id;
        this.orderTime= Calendar.getInstance().getTime();
        this.payTime=null;
        this.money=0;
        this.goods=new ArrayList<>();
        this.logistics=new Logistics();
    }
    public Order(String id,float money,List<Goods> goods,Logistics logistics)
    {
        this.id=id;
        this.money=money;
        this.goods=goods;
        this.logistics=logistics;
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

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public float getMoney() {
        money = 0;
        for(Goods g:goods)
            money+=g.getMoney();
        return money;
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
}
