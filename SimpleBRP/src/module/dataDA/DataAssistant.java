package module.dataDA;

import com.sun.org.apache.xpath.internal.operations.Or;
import module.Info;
import module.order.Order;

import javax.xml.crypto.Data;
import java.io.*;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;


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
        return null;
    }

    //注意将源文件的编码改为UTF-8,否则无法读取中文表头
    public void getAll(List<Order> orders){
        if(orderList!=null){
            orders=orderList;
            return;
        }
        else{
            String sql="SELECT * FROM "+ORDER_CSV_NAME;
            String sql2="SELECT * FROM "+LOGISTIC_CSV_NAME+" WHERE "+logisticsHeaders[0]+" ='";
            ResultSet resultSet=null;
            try{
                resultSet=statement.executeQuery(sql);
                while(resultSet.next()){
                    String id=resultSet.getString(orderHeaders[0]);
                    //筛查刷单
                    //String log_sql=sql2+id+"'";
                    String log_sql = "SELECT * FROM 运费 WHERE 国际物流单号 ='"+id+"'";
                    ResultSet log_resultSet=statement.executeQuery(log_sql);
                    //查得到物流单号，不是刷单
                    if(log_resultSet.next()){
                        Order order=new Order(id);
                        orders.add(order);
                    }
                }
                resultSet.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
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
        return dataSize;
    }
}
