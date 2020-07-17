package module.dataDA;

import module.Info;
import module.order.Order;

import java.io.File;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;


public class DataAssistant implements DataManageable{
    private static final String CSV_JDBC_DRIVER = "org.relique.jdbc.csv.CsvDriver";
    private static final String CSV_JDBC_HEADER = "jdbc:relique:csv:";
    private static final String CSV_NAME="ecdict";
    private String path= System.getProperty("user.dir");
    private static final String[] headers={"word","phonetic","definition","translation","tag","exchange"};

    private Connection connection;
    private Statement statement;

    public DataAssistant()
    {
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
        String[] temp=path.split("\\\\");
        String dir="";
        for(String s :temp){
            dir+=s;
            dir+="\\";
        }
        dir+="src\\";
        path=dir;
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
                for(String key:headers)
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

    public void getAll(List<Order> orders){

    }

    @Override
    public boolean selectFile(File file) {
        return false;
    }

    @Override
    public Info getInfo() {
        return null;
    }
}
