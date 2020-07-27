package module.dataDA;

import com.sun.org.apache.xpath.internal.operations.Or;
import module.Info;
import module.order.Logistics;
import module.order.Order;

import javax.xml.crypto.Data;
import java.io.*;
import java.sql.*;
import java.util.*;


public class DataAssistant implements DataManageable{
    private static final String CSV_JDBC_DRIVER = "org.relique.jdbc.csv.CsvDriver";
    private static final String CSV_JDBC_HEADER = "jdbc:relique:csv:";
    private static final String CSV_NAME="ecdict";
    private static final String ORDER_CSV_NAME="订单";
    private static final String LOGISTIC_CSV_NAME="运费";
    private String resourcePath=System.getProperty("user.dir");
    private static String resourceFile="resource.txt";
    private String path= System.getProperty("user.dir");
    private static final String[] orderHeaders={"订单号","下单时间","付款时间","商品编码","订单金额","收货国家"};
    private static final String[] logisticsHeaders={"国际物流单号","物流服务名称","订单重量","金额（CNY）"};

    private Connection connection;
    private Statement statement;
    private Statement log_statement;

    //全部订单列表
    private List<Order> orderList=null;

    private Integer dataSize=0;
    public DataAssistant()
    {
        //System.out.println(path);
        setDirPath();
        initDriver();
    }

    private void initDriver()
    {
        try{
            Class.forName(CSV_JDBC_DRIVER);
            Properties properties=new Properties();
            //设定字符集
            properties.put("charset","UTF-8");
            //创建连接
            connection= DriverManager.getConnection(CSV_JDBC_HEADER+path,properties);
            //创建Statement
            statement=connection.createStatement();
            log_statement=connection.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

    }

    private void setDirPath()
    {
        getResource();
        String[] temp=path.split("\\\\");
        String dir="";
        for(String s :temp){
            dir+=s;
            dir+="\\";
        }
        //dir+="src\\";
        path=dir;
    }

    private void getResource() {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(resourcePath + "\\" + resourceFile));
            path = br.readLine();
            br.close();
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found!");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, String> search(String word)
    {
        char c=word.toLowerCase().charAt(0);
        ResultSet resultSet;
        String temp="";
        String sql="SELECT * FROM "+CSV_NAME+" WHERE word = '"+word+"'";
        Map<String, String> result=new HashMap<>();
        try {
            //ResultSet resultSet=binSearch(word);
            resultSet=statement.executeQuery(sql);
            while(resultSet.next())
            {
                for(String key:orderHeaders)
                    result.put(key,resultSet.getString(key));
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    public void close(){
        try {
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public Statement getStatement() {
        return statement;
    }

    @Override
    public List<Order> search() {
        return null;
    }

    @Override
    public List<Order> getAll() {
        List<Order> orders=new ArrayList<>();
        this.getAll(orders);
        return orders;
    }

    //注意将源文件的编码改为UTF-8,否则无法读取中文表头
    public void getAll(List<Order> orders){
        if(orderList!=null){
            orders=orderList;
            return;
        }
        else{
            String sql="SELECT * FROM "+ORDER_CSV_NAME;

            ResultSet resultSet=null;
            try{
                resultSet=statement.executeQuery(sql);
                while(resultSet.next()){
                    String id=resultSet.getString(orderHeaders[0]);
                    String log_id=resultSet.getString("实际发货单号");
                    //筛查刷单
                    /**ResultSet与Statement是关联的，必须新开一个Statement*/
                    Logistics logistics=isValid(log_id);
                    //不是刷单
                    if(logistics!=null){
                        Order order=new Order(id);
                        order.setLogistics(logistics);
                        order.setOrderTime(resultSet.getString(orderHeaders[1]));
                        order.setPayTime(resultSet.getString(orderHeaders[2]));
                        //goods暂时空着
                        String money=resultSet.getString(orderHeaders[4]).substring(4);
                        order.setMoney(Float.parseFloat(money));
                        order.setCountry(resultSet.getString(orderHeaders[5]));
                        orders.add(order);
                    }
                }
                resultSet.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
    }

    /**
     *传进一个Order，如果其id有对应的物流信息，则返回Logistics对象;否则返回null
     * 订单表与物流表对应的是,订单表中的实际发货单号（取出物流服务名）对应物流的国际物流单号
     */
    private Logistics isValid(String order_log_id) throws SQLException{
            String log_id;
        if(order_log_id.contains(":"))
            log_id=order_log_id.split(":")[1];
        else
            log_id=order_log_id;
        if(log_id.contains("\n")){
            log_id=log_id.substring(0,log_id.indexOf('\n'));
        }
        String sql="SELECT * FROM logistics WHERE id = '"+log_id+"'";
        ResultSet resultSet=log_statement.executeQuery(sql);
        if(resultSet.next()){
            Logistics logistics=new Logistics();
            logistics.setId(log_id);
            logistics.setServer(resultSet.getString(logisticsHeaders[1]));
            logistics.setWeight(resultSet.getFloat(logisticsHeaders[2]));
            logistics.setMoney(resultSet.getFloat(logisticsHeaders[3]));
            return logistics;
        }
        return null;
    }
    @Override
    public boolean selectFile(File file) {
        return false;
    }

    @Override
    public Info getInfo() {
        return null;
    }

    public Integer getDataSize(){
        dataSize=orderList.size();
        return dataSize;
    }
}
