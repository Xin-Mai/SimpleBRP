package order;

public class Logistics {
    //国际物流单号
    private String id;
    //订单重量
    private float weight;
    //物流服务名称
    private String server;
    //金额（CNY）
    private float money;

    public Logistics(){}
    public Logistics(String id)
    {
        this.id=id;
        this.server=null;
        this.weight=0;
        this.money=0;
    }
    public Logistics(String id,float money)
    {
        this.id=id;
        this.money=money;
        this.weight=0;
        this.server=null;
    }
    public Logistics(String id,float weight,String server,float money)
    {
        this.id=id;
        this.money=money;
        this.server=server;
        this.weight=weight;
    }

    public String getSqlData()
    {
        return "('"+id+"','"+server+"',"+weight+","+money+")";
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

}
