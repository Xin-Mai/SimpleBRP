package dataDA;

import java.io.IOError;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mysql.cj.log.Log;
import order.*;

public class DataDA {
    // MySQL 8.0 以上版本 - JDBC 驱动名及数据库 URL
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/订单信息?useSSL=false&serverTimezone=UTC";

    //数据库的用户名和密码
    static final String USER = "root";
    static final String PASSWORD = "123";

    //数据库中表格的名字
    static final String ORDER_TABLE = "订单";
    static final String LOGISTICS_TABLE = "物流";
    static final String COST_TABLE = "成本";

    //数据库中表头的名字
    static final String[] ORDER_TITLE = {"订单号","下单时间","付款时间","订单金额","商品编码","收货国家","实际发货单号"};
    static final String[] LOGISTICS_TITLE = {"国际物流单号","物流服务名称","订单重量","金额（CNY）"};
    static final String[] COST_TITLE = {"型号","颜色代码","分销单价"};

    //数据库需要用后关闭的东西
    private Connection connection=null;
    private Statement statement;

    public DataDA()
    {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("数据库驱动注册失败");
        }
        System.out.println("数据库驱动注册成功");
    }

    //对数据库的连接
    private void getConnection() throws SQLException
    {
        connection=DriverManager.getConnection(DB_URL,USER,PASSWORD);
    }

    //获取Statement
    private Statement getStatement() throws SQLException
    {
        if(connection==null)
            getConnection();
        return connection.createStatement();
    }

    /**
    //订单表头顺序为订单号，下单时间，付款时间，订单金额，商品编码，收货国家，快递单号
    //物流表头为国际物流单号，物流服务名称，订单重量，金额（CNY）
     //成本表头为型号，颜色代码，分销单价
     */
    public boolean add(Order order)
    {
        String sql;
        String sql2;
        int flag=0;
        /**将信息加入订单表*/
        sql="INSERT INTO "+ORDER_TABLE+" VALUES"+order.getSqlData();
        /**将物流信息加入物流表*/
        sql2="INSERT INTO "+LOGISTICS_TABLE+" VALUES"+order.getLogistics().getSqlData();
        try {
            statement=getStatement();
            statement.executeUpdate(sql);
            statement.executeUpdate(sql2);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }finally {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**查看全部订单*/
    public List<Order> search()
    {
        List<Order> orders = select(ORDER_TITLE[0],"*");
        fillLogistics(orders);
        for(Order o:orders)
        {
            List<Goods> g=o.getGoods();
            setGoodsCost(g);
            System.out.println(o.getSqlData());
        }
        return orders;
    }

    /**补充商品成本*/
    public void setGoodsCost(List<Goods> g)
    {
        for(Goods good:g)
        {
            String id=good.getId();
                try{
                    String[] type = id.split(" ");
                    /*id=id.substring(id.indexOf('-')+1);
                    String color = id.substring(0,id.indexOf('-'));*/
                    good.setCost(good.getQuantity()*searchCost(type[0]));
                }catch (StringIndexOutOfBoundsException e)
                {
                    //找不到对应的就设55
                    System.out.println(id);
                    good.setCost(55);
                }
        }
    }
    /**查询订单*/
    public List<Order> search(String content)
    {
        List<Order> orders = new ArrayList<>();
        for(String title:ORDER_TITLE)
        {
            orders.addAll(select(title,content));
        }
        fillLogistics(orders);
        for(Order o:orders)
        {
            List<Goods> g=o.getGoods();
            setGoodsCost(g);
            System.out.println(o.getSqlData());
        }
        if(orders.size()!=0)
            return orders;
        else
            return null;
    }

    /**查询成本*/
    public float searchCost(String color)
    {
        String sql="SELECT * FROM "+COST_TABLE+
                " WHERE "+COST_TITLE[1]+" LIKE '%"+color+"%'";
        try {
            statement=getStatement();
            ResultSet rs=statement.executeQuery(sql);
            if(rs.next())
            {
                return rs.getFloat(COST_TITLE[2]);
            }
            statement.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }

    /**根据不同表头在订单表中查询，并根据物流单号从物流表中获取logistics对象*/
    public List<Order> select(String row,String content)
    {
        String sql="SELECT * FROM "+ORDER_TABLE+
                " WHERE "+row+" LIKE '%"+content+"%'";
        //查询全部信息
        if(content.equals("*"))
            sql="SELECT * FROM "+ORDER_TABLE;
        List<Order> orders = new ArrayList<>();
        try{
            statement = getStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next())
            {
                String id = resultSet.getString(ORDER_TITLE[0]);
                String orderTime = resultSet.getString(ORDER_TITLE[1]);
                String payTime = resultSet.getString(ORDER_TITLE[2]);
                Float money = resultSet.getFloat(ORDER_TITLE[3]);
                String goods = resultSet.getString(ORDER_TITLE[4]);
                String country= resultSet.getString(ORDER_TITLE[5]);
                String lid=resultSet.getString(ORDER_TITLE[6]);
                lid=lid.substring(lid.indexOf(":")+1);
                if(lid.contains("\n"))
                    lid=lid.substring(0,lid.indexOf('\n'));
                Logistics logistics = new Logistics(lid);
                String[] allgoods = goods.split(";");
                List<Goods> gList = new ArrayList<>();
                for(String g:allgoods)
                {
                    gList.add(new Goods(g));
                }
                orders.add(new Order(id,orderTime,payTime,money,gList,logistics,country));
            }
            resultSet.close();
            statement.close();
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return orders;
    }

    /**补全订单的物流信息*/
    private void fillLogistics(List<Order> orders)
    {
        String sql = "SELECT * FROM "+ LOGISTICS_TABLE+
                " WHERE "+LOGISTICS_TITLE[0]+" LIKE '%";
        for(Order o:orders)
        {
            Logistics logistics = o.getLogistics();
            try{
                statement=getStatement();
                String id=logistics.getId();
                ResultSet rs = statement.executeQuery(sql+id+"%'");
                //刷单的没有真正的物流单号
                if(rs.next())
                {
                    //生成logistics对象并且补全order
                    logistics.setServer(rs.getString(LOGISTICS_TITLE[1]));
                    logistics.setWeight(rs.getFloat(LOGISTICS_TITLE[2]));
                    logistics.setMoney(rs.getFloat(LOGISTICS_TITLE[3]));
                }
                else
                {
                    //把刷单的剔除
                    //在遍历集合的同时对元素进行修改会造成异常
                    //orders.remove(o);
                }
                rs.close();
                if(orders.size()==0)
                    break;
            }catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        //使用迭代器避免遍历集合时报错
        Iterator<Order> iterator = orders.iterator();
        while(iterator.hasNext())
        {
            Order o=iterator.next();
            if(o.getLogistics().getWeight()==0)
                iterator.remove();
        }
        try{
            statement.close();
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void close(){
        try {
            connection.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
