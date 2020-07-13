package module;

public class Info {
    //总单数
    private int ordersNum;
    //总金额
    private float money;
    //总利润
    private float profit;
    //平均金额
    private float averMoney;
    //平均利润
    private float averProfit;
    //畅销国家
    private String[] bestSales;

    public int getOrdersNum() {
        return ordersNum;
    }

    public void setOrdersNum(int ordersNum) {
        this.ordersNum = ordersNum;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public float getProfit() {
        return profit;
    }

    public void setProfit(float profit) {
        this.profit = profit;
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
