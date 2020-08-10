package module;

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
    //畅销单品
    private String[] bestSales;

    public Info(String country){
        this.country=country;
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
        return averMoney;
    }

    public void setAverMoney(float averMoney) {
        this.averMoney = averMoney;
    }

    public float getAverProfit() {
        return averProfit;
    }

    public void setAverProfit(float averProfit) {
        this.averProfit = averProfit;
    }

    public String[] getBestSales() {
        return bestSales;
    }

    public void setBestSales(String[] bestSales) {
        this.bestSales = bestSales;
    }
}
