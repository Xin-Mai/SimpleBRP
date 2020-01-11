package order;

public class Goods {
    //编码
    private String id;
    //售价
    private float price;
    //成本
    private float cost;
    //数量
    private int quantity;

    public Goods(){}
    public Goods(String type)
    {
        this.id=type;
        this.quantity=1;
        this.cost=0;
        //在初始化时就应该用Id获取相关的成本和价格
        //用map
    }
    public Goods(String type,int quantity)
    {
        this(type);
        this.quantity=quantity;
    }

    //获取总价
    public float getMoney()
    {
        return price*quantity;
    }
    //添加数量
    public void add(int i)
    {
        this.quantity+=i;
    }
    public void add()
    {
        this.quantity++;
    }


    public String getId() {
        return id;
    }

    public void setId(String type) {
        this.id = type;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
