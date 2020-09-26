package module.dataDA;

import javafx.scene.control.Alert;
import module.Info;
import module.order.Goods;
import module.order.Logistics;
import module.order.Order;

import java.io.*;
import java.nio.file.Files;
import java.sql.*;
import java.util.*;


public class DataAssistant implements DataManageable{
    private static final String CSV_JDBC_DRIVER = "org.relique.jdbc.csv.CsvDriver";
    private static final String CSV_JDBC_HEADER = "jdbc:relique:csv:";
    //private static final String CSV_NAME="ecdict";
    private static final String ORDER_CSV_NAME="订单";
    private static final String LOGISTIC_CSV_NAME="logistics";
    private static final String GOODS_CSV_NAME="cost";
    private String resourcePath=System.getProperty("user.dir");
    private static String resourceFile="resource.txt";
    private String path= System.getProperty("user.dir");
    private static final String[] orderHeaders={"订单号","下单时间","付款时间","商品编码","订单金额","收货国家"};
    private static final String[] logisticsHeaders={"国际物流单号","物流服务名称","订单重量","金额（CNY）"};
    private static final String[] goodsHeader={"type","color","price"};

    private Connection connection;
    private Statement statement;
    private Statement log_statement;
    private Statement goods_statement;

    //全部订单列表
    private List<Order> orderList=null;
    private Integer dataSize=0;
    //全部国家的综合信息
    private Map<String, Info> conInfoMap=null;

    //获取综合数据的线程
    Thread infoThread=null;

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
            goods_statement=connection.createStatement();
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
        File resourceDir=new File(path);
        //创建存储资源文件的文件夹
        if(!resourceDir.exists())
            //注意与mkdir()区别，如果父路径不存在,mkdir()是不会创建的
            System.out.println(resourceDir.mkdirs());
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


    public void close(){
        try {
            statement.close();
            goods_statement.close();
            log_statement.close();
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
        this.orderList=orders;
        //处理综合数据
        infoThread=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        fillConInfo();
                    }
                }, "info");
        infoThread.start();
        //fillConInfo();
        return orders;
    }

    //获取不同国家的数据
    private void fillConInfo(){
        if(conInfoMap==null)
            conInfoMap=new HashMap<>();
        for(Order o:orderList){
            String country=o.getCountry();
            if(!conInfoMap.containsKey(country)){
                conInfoMap.put(country,new Info(country));
            }
            Info info=conInfoMap.get(country);
            info.addOrdersNum();
            info.addMoney(o.getMoney());
            info.addProfit(o.getProfit());
            info.addLog(o.getLogistics());
            info.addBestSales(o.getGoods());
        }
    }

    //注意将源文件的编码改为UTF-8,否则无法读取中文表头
    public void getAll(List<Order> orders){
        if(orderList!=null){
            orders=orderList;
            return;
        }
        else {
            String sql = "SELECT * FROM " + ORDER_CSV_NAME;
            //首先要判断文件是否存在
            File orderFile = new File(path + "/" + ORDER_CSV_NAME + ".csv");
            File logFile=new File(path+"/"+LOGISTIC_CSV_NAME+".csv");
            File costFile=new File(path+"/"+GOODS_CSV_NAME+".csv");
            if (!(orderFile.exists()&&logFile.exists()&&costFile.exists())) {
                //弹窗警示
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("警告");
                alert.setHeaderText("资源文件缺失！");
                alert.setContentText("请使用更新数据按钮，选择相应的数据文件。否则系统无法正常使用！");
                alert.showAndWait();
            } else {
                ResultSet resultSet = null;
                try {
                    resultSet = statement.executeQuery(sql);
                    while (resultSet.next()) {
                        String id = resultSet.getString(orderHeaders[0]);
                        String log_id = resultSet.getString("实际发货单号");
                        //筛查刷单
                        /**ResultSet与Statement是关联的，必须新开一个Statement*/
                        Logistics logistics = isValid(log_id);
                        //不是刷单
                        if (logistics != null) {
                            Order order = new Order(id);
                            order.setLogistics(logistics);
                            order.setOrderTime(resultSet.getString(orderHeaders[1]));
                            order.setPayTime(resultSet.getString(orderHeaders[2]));
                            //goods暂时空着
                            String good = resultSet.getString(orderHeaders[3]);
                            String[] goods = good.split(";");
                            List<Goods> goodsList = new ArrayList<>();
                            for (String i : goods) {
                                Goods g = new Goods(i);
                                g.setPrice(getGoodCost(g.getId()));
                                goodsList.add(g);
                            }
                            order.setGoods(goodsList);
                            String money = resultSet.getString(orderHeaders[4]).substring(4);
                            order.setMoney(Float.parseFloat(money));
                            order.setCountry(resultSet.getString(orderHeaders[5]));
                            orders.add(order);
                        }
                    }
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("警告");
                    alert.setHeaderText("资源文件错误！");
                    alert.setContentText("请使用更新数据按钮，选择相应的数据文件。否则系统无法正常使用！");
                    alert.showAndWait();
                }
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
        //判断是否存在物流文件
        File logisFile=new File(path+"/"+LOGISTIC_CSV_NAME+".csv");
        if(!logisFile.exists()){

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("警告");
            alert.setHeaderText("物流信息文件不存在");
            alert.setContentText("请使用更新数据按钮导入文件");

            alert.showAndWait();
        }
        else {
            String sql = "SELECT * FROM " + LOGISTIC_CSV_NAME + " WHERE id = '" + log_id + "'";
            ResultSet resultSet = log_statement.executeQuery(sql);
            if (resultSet.next()) {
                Logistics logistics = new Logistics();
                logistics.setId(log_id);
                logistics.setServer(resultSet.getString(logisticsHeaders[1]));
                logistics.setWeight(resultSet.getFloat(logisticsHeaders[2]));
                logistics.setMoney(resultSet.getFloat(logisticsHeaders[3]));
                return logistics;
            }
        }
        return null;
    }
    @Override
    public boolean selectFile(File file) {
        return false;
    }

    @Override
    public Info getInfo(String country) {
        if(infoThread!=null) {
            try {
                infoThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(conInfoMap!=null)
            return conInfoMap.get(country);
        return null;
    }

    @Override
    public List<Info> getData(){
        if(infoThread!=null) {
            try {
                infoThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(conInfoMap!=null){
            List<Info> infoList= new ArrayList<>();
            for(Info info:conInfoMap.values())
                infoList.add(info);
            infoList.sort(new Comparator<Info>() {
                @Override
                public int compare(Info o1, Info o2) {
                    if(o1.getOrdersNum()<o2.getOrdersNum())
                        return 1;
                    else if(o1.getOrdersNum()>o2.getOrdersNum())
                        return -1;
                    return 0;
                }
            });
            return infoList;
        }
        return null;
    }

    public int getDataSize(){
        dataSize=orderList.size();
        return dataSize;
    }

    public float getGoodCost(String type){
        String[] id=type.split("-");
        String sql="SELECT "+goodsHeader[2]+" FROM "+GOODS_CSV_NAME+" WHERE "
                +goodsHeader[0]+" LIKE '"+id[0]+"%'";
        if(id.length>1)
            sql+=" AND "+goodsHeader[1]+" LIKE '"+id[1]+"'";
        ResultSet resultSet= null;
        try {
            resultSet = goods_statement.executeQuery(sql);

            if(!resultSet.next()){
                sql="SELECT "+goodsHeader[2]+" FROM "+GOODS_CSV_NAME+" WHERE "
                        +goodsHeader[0]+" LIKE '"+id[0]+"%'";
                resultSet=goods_statement.executeQuery(sql);
            }
            if(resultSet.next()){
                String price=resultSet.getString(goodsHeader[2]);
                price=price.split(" ")[0];
                float cost=Float.parseFloat(price.substring(1));
                return cost;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;

    }

    //更新数据文件，预计可以更新logistic,order,cost
    @Override
    public void updateResFile(File file, String type) {
        String fileName=null;
        if(type.equals("订单"))
            fileName=ORDER_CSV_NAME;
        else if(type.equals("物流"))
            fileName=LOGISTIC_CSV_NAME;
        else
            fileName=GOODS_CSV_NAME;
        File goal=new File(path+"/"+fileName+".csv");
        //假如就是同一个文件
        if(goal.toPath().equals(file.toPath()))
            return;
        //如果目标路径已经存在旧的数据文件，则先删除
        if(goal.exists())
            goal.delete();
        try
        {
            Files.copy(file.toPath(),goal.toPath());
        }catch (IOException e){
            e.printStackTrace();
        }
        //将原有的数据清空
        this.orderList=null;
    }
}
