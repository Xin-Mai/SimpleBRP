package dataDA;

import java.io.IOError;
import java.io.IOException;
import java.sql.*;
import order.*;

public class DataDA {
    // MySQL 8.0 以上版本 - JDBC 驱动名及数据库 URL
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/RUNOOB?useSSL=false&serverTimezone=UTC";

    //数据库的用户名和密码
    static final String USER = "root";
    static final String PASSWORD = "12345";

    //数据库中表格的名字
    static final String ORDER_TABLE = "订单";
    static final String LOGISTICS_TABLE = "物流";
    static final String COST_TABLE = "成本";

    //数据库需要用后关闭的东西
    private Connection connection;
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
    private Connection getConnection() throws SQLException
    {
        connection=DriverManager.getConnection(DB_URL,USER,PASSWORD);
        return connection;
    }

    //获取Statement
    private Statement getStatement() throws SQLException
    {
        return getConnection().createStatement();
    }

    /**
    //订单表头顺序为订单号，下单时间，付款时间，订单金额，商品编码，收货国家，快递单号
    //物流表头为国际物流单号，物流服务名称，订单重量，金额（CNY）
     //成本表头为型号，颜色代码，分销单价
     */
    public boolean add(Order order)
    {
        String sql;
        int flag=0;
        sql="INSERT INTO '"+ORDER_TABLE+"' VALUES"+order.getSqlData();
        try {
            statement=getStatement();
            flag=statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(flag==0)
            return false;
        else
            return true;
    }
}
