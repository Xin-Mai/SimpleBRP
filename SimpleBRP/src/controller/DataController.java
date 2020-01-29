package controller;

import dataDA.DataDA;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import order.Order;

import java.io.IOException;
import java.util.*;

public class DataController {
    @FXML
    private ComboBox goodsType;
    @FXML
    private ComboBox server;
    @FXML
    private ComboBox country;
    @FXML
    private VBox dataBox;
    @FXML
    private HBox box1;
    @FXML
    private HBox box2;
    private MainController mainController;
    private HBox back;

    //数据库中表格的名字
    static final String ORDER_TABLE = "订单";
    static final String LOGISTICS_TABLE = "物流";
    static final String COST_TABLE = "成本";

    final static String[] countryList = {"全部","阿富汗  Afghanistan", "奥兰群岛  Aland Islands", "阿尔巴尼亚  Albania","阿尔及利亚  Algeria","美属萨摩亚  American Samoa","安道尔  Andorra","安哥拉  Angola","安圭拉  Anguilla","安提瓜和巴布达  Antigua and Barbuda","阿根廷  Argentina","亚美尼亚  Armenia","阿鲁巴  Aruba","澳大利亚  Australia","奥地利  Austria","阿塞拜疆  Azerbaijan","孟加拉  Bangladesh","巴林  Bahrain","巴哈马  Bahamas", "巴巴多斯  Barbados", "白俄罗斯  Belarus", "比利时  Belgium", "伯利兹  Belize", "贝宁  Benin", "百慕大  Bermuda", "不丹  Bhutan", "玻利维亚  Bolivia", "波斯尼亚和黑塞哥维那  Bosnia and Herzegovina", "博茨瓦纳  Botswana","布维岛  Bouvet Island"
            ,"巴西  Brazil","文莱  Brunei","保加利亚  Bulgaria","布基纳法索  Burkina Faso","布隆迪  Burundi","柬埔寨  Cambodia","喀麦隆  Cameroon","加拿大  Canada","佛得角  Cape Verde","中非  Central African Republic","乍得  Chad","智利  Chile","圣诞岛  Christmas Islands","科科斯（基林）群岛  Cocos (keeling) Islands","哥伦比亚  Colombia","科摩罗  Comoros","刚果（金）  Congo (Congo-Kinshasa)","刚果  Congo"
            ,"库克群岛  Cook Islands","哥斯达黎加  Costa Rica","科特迪瓦  Cote D’Ivoire","中国  China","克罗地亚  Croatia","古巴  Cuba","捷克  Czech","塞浦路斯  Cyprus","丹麦  Denmark"
            ,"吉布提  Djibouti","多米尼加  Dominica","东帝汶  Timor-Leste","厄瓜多尔  Ecuador","埃及  Egypt","赤道几内亚  Equatorial Guinea","厄立特里亚  Eritrea","爱沙尼亚  Estonia","埃塞俄比亚  Ethiopia","法罗群岛  Faroe Islands","斐济  Fiji","Finland  Finland","法国  France","法国大都会  Franch Metropolitan","法属圭亚那  Franch Guiana","法属波利尼西亚  French Polynesia","加蓬  Gabon","冈比亚  Gambia","格鲁吉亚  Georgia"
            ,"德国  Germany","加纳  Ghana","直布罗陀  Gibraltar","希腊  Greece","格林纳达  Grenada","瓜德罗普岛  Guadeloupe","关岛  Guam","危地马拉  Guatemala","根西岛  Guernsey","几内亚比绍  Guinea-Bissau","几内亚  Guinea","圭亚那  Guyana","香港 （中国）  Hong Kong","海地  Haiti","洪都拉斯  Honduras","匈牙利  Hungary","冰岛  Iceland"
            ,"印度  India","印度尼西亚  Indonesia","伊朗  Iran","伊拉克  Iraq","爱尔兰  Ireland","马恩岛  Isle of Man","以色列  Israel","意大利  Italy","牙买加  Jamaica","日本  Japan","泽西岛  Jersey","约旦  Jordan","哈萨克斯坦  Kazakhstan","肯尼亚  Kenya","基里巴斯  Kiribati","韩国  Korea (South)","朝鲜  Korea (North)","科威特  Kuwait","吉尔吉斯斯坦  Kyrgyzstan","老挝  Laos","拉脱维亚  Latvia","黎巴嫩  Lebanon"
            ,"莱索托  Lesotho","利比里亚  Liberia","利比亚  Libya","列支敦士登  Liechtenstein","立陶宛  Lithuania","卢森堡  Luxembourg","澳门（中国）  Macau","马其顿  Macedonia","马拉维  Malawi","马来西亚  Malaysia","马达加斯加  Madagascar","马尔代夫  Maldives","马里  Mali","马耳他  Malta","马绍尔群岛  Marshall Islands","马提尼克岛  Martinique","毛里塔尼亚  Mauritania","毛里求斯  Mauritius","马约特  Mayotte","墨西哥  Mexico","密克罗尼西亚  Micronesia","摩尔多瓦  Moldova","摩纳哥  Monaco","蒙古  Mongolia","黑山  Montenegro","蒙特塞拉特  Montserrat","摩洛哥  Morocco","莫桑比克  Mozambique","缅甸  Myanmar","纳米比亚  Namibia","瑙鲁  Nauru","尼泊尔  Nepal","荷兰  Netherlands","新喀里多尼亚  New Caledonia","新西兰  New Zealand","尼加拉瓜  Nicaragua","尼日尔  Niger","尼日利亚  Nigeria"
            ,"纽埃  Niue","诺福克岛  Norfolk Island","挪威  Norway","阿曼  Oman","巴基斯坦  Pakistan","帕劳  Palau","巴勒斯坦  Palestine","巴拿马  Panama","巴布亚新几内亚  Papua New Guinea","巴拉圭  Paraguay","秘鲁  Peru"
            ,"菲律宾  Philippines","皮特凯恩群岛  Pitcairn Islands","波兰  Poland","葡萄牙  Portugal","波多黎各  Puerto Rico","卡塔尔  Qatar","留尼汪岛  Reunion","罗马尼亚  Romania","卢旺达  Rwanda","俄罗斯联邦  Russian Federation","圣赫勒拿  Saint Helena","圣基茨和尼维斯  Saint Kitts-Nevis"
            ,"圣卢西亚  Saint Lucia","圣文森特和格林纳丁斯  Saint Vincent and the Grenadines","萨尔瓦多  El Salvador","萨摩亚  Samoa","圣马力诺  San Marino","圣多美和普林西比  Sao Tome and Principe","沙特阿拉伯  Saudi Arabia","塞内加尔  Senegal","塞舌尔  Seychelles", "塞拉利昂  Sierra Leone","新加坡  Singapore","塞尔维亚  Serbia","斯洛伐克  Slovakia","斯洛文尼亚  Slovenia", "所罗门群岛  Solomon Islands","索马里  Somalia","南非  South Africa","西班牙  Spain","斯里兰卡  Sri Lanka","苏丹  Sudan","苏里南  Suriname","斯威士兰  Swaziland","瑞典  Sweden","瑞士  Switzerland","叙利亚  Syria","塔吉克斯坦  Tajikistan","坦桑尼亚  Tanzania","台湾 （中国）  Taiwan","泰国  Thailand","特立尼达和多巴哥  Trinidad and Tobago","多哥  Togo","托克劳  Tokelau","汤加  Tonga","突尼斯  Tunisia",
            "土耳其  Turkey","土库曼斯坦  Turkmenistan","图瓦卢  Tuvalu","乌干达  Uganda","乌克兰  Ukraine","阿拉伯联合酋长国  United Arab Emirates","英国  United Kingdom","美国  United States","乌拉圭  Uruguay","乌兹别克斯坦  Uzbekistan","瓦努阿图  Vanuatu","梵蒂冈  Vatican City","委内瑞拉  Venezuela","越南  Vietnam","瓦利斯群岛和富图纳群岛  Wallis and Futuna","西撒哈拉  Western Sahara","也门  Yemen","南斯拉夫  Yugoslavia","赞比亚  Zambia","津巴布韦  Zimbabwe"
    };
    private List<String> goodsId=new ArrayList<>();
    final static String[] logServer = {"全部","菜鸟特货专线-标准","AliExpress 无忧物流-标准","ePacket","4PX新邮挂号小包","DHL",};
    @FXML
    private void initialize() {
    }
    /**必须要有空的构造方法，才能够获得对应的组件！！！*/
    public DataController(){}
    public DataController(MainController m)
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/data.fxml"));
        this.mainController=m;
        this.back=m.getPane();
        try{
            dataBox=loader.load();
            box1=(HBox)dataBox.getChildren().get(0);
            box2=(HBox)dataBox.getChildren().get(1);
            HBox box11=(HBox)box1.getChildren().get(0);
            country=(ComboBox)box11.getChildren().get(1);
            country.getItems().addAll(countryList);
            HBox box12 =(HBox)box1.getChildren().get(1);
            goodsType = (ComboBox)box12.getChildren().get(1);
            HBox box13 = (HBox)box1.getChildren().get(2);
            server=(ComboBox)box13.getChildren().get(1);
            server.getItems().addAll(logServer);
            goodsType.getItems().add("全部");
            setHotSale();
        }catch (IOException e){
            e.printStackTrace();
        }


    }
    /**开始搜索，获取数据*/
    public void handleSearchDataAction(ActionEvent actionEvent) {
        DataDA da=new DataDA();
        String coun =country.getSelectionModel().getSelectedItem().toString();
        if(coun.contains("  "))
            coun=coun.substring(coun.indexOf(" ")+2);
        String goods = goodsType.getSelectionModel().getSelectedItem().toString();
        String logic = server.getSelectionModel().getSelectedItem().toString();
        List<Order> orders = da.search(coun,goods,logic);
        setData(orders);
        da.close();

    }

    /**获取热销国家*/
    private void setHotSale()
    {
        VBox box3=(VBox)dataBox.lookup("#hotSale");
        //获取销量前三的国家
        Label top1=(Label)box3.getChildren().get(1);
        Label top2=(Label)box3.getChildren().get(2);
        Label top3=(Label)box3.getChildren().get(3);
        SaleComparator comparator=new SaleComparator();
        DataDA da = new DataDA();
        Map<String,Integer> map=new HashMap<>();
        List<Order> orders=da.search();
        List<Map.Entry<String,Integer>> tops=new ArrayList<>();
        for(Order o:orders)
        {
            String c=o.getCountry();
            if(map.keySet().contains(c))
            {
                map.replace(c,map.get(c)+1);
            }
            else{
                map.put(c,1);
            }
        }
        for(Map.Entry<String,Integer> e:map.entrySet())
        {
            if(tops.size()<3)
                tops.add(e);
            else if(e.getValue()>tops.get(2).getValue()){
                tops.add(e);
                Collections.sort(tops,comparator);
                tops.remove(3);
            }
        }
        if(tops.get(0)!=null)
            top1.setText(" 1. "+tops.get(0).getKey()+"："+tops.get(0).getValue()+"件");
        else
            top1.setText("NULL");
        if(tops.get(1)!=null)
            top2.setText(" 2. "+tops.get(1).getKey()+"："+tops.get(1).getValue()+"件");
        else
            top2.setText("NULL");
        if(tops.get(2)!=null)
            top3.setText(" 3. "+tops.get(2).getKey()+"："+tops.get(2).getValue()+"件");
        else
            top3.setText("NULL");
    }
    private void setData(List<Order> orders)
    {
        float totalProfit=0;
        float totalMoney=0;
        float toatlProfitRate=0;
        float totalLFee=0;
        VBox box21=(VBox)box2.getChildren().get(0);
        VBox box22=(VBox)box2.getChildren().get(1);
        VBox box23 =(VBox)box2.getChildren().get(2);
        for(Order o:orders)
        {
            totalMoney+=o.getMoney();
            toatlProfitRate+=o.getProfitRate();
            totalProfit+=o.getProfit();
            totalLFee+=o.getLogistics().getMoney();
        }
        //总单数，总金额，总利润
        Label num=(Label)box21.getChildren().get(0);
        num.setText("总单数："+orders.size());
        Label money=(Label)box21.getChildren().get(1);
        String t=""+totalMoney;
        if(t.length()>5)
            t=t.substring(0,6);
        money.setText("总金额：$"+t);
        Label profit=(Label)box21.getChildren().get(2);
        t=""+totalProfit;
        if(t.length()>5)
            t=t.substring(0,5);
        profit.setText("总利润："+t);
        //利润率,平均运费，平均利润
        Label rate=(Label)box22.getChildren().get(0);
        String r=100*toatlProfitRate/orders.size()+"";
        r=r.substring(0,r.indexOf(".")+3);
        rate.setText("平均每单利润率："+r+"%");
        Label fee=(Label)box22.getChildren().get(1);
        r=totalLFee/orders.size()+"";
        if(r.length()>5)
            r=r.substring(0,5);
        fee.setText("平均每单运费："+r);
        Label pro=(Label)box22.getChildren().get(2);
        r=""+totalProfit/orders.size();
        if(r.length()>5)
            r=r.substring(0,5);
        pro.setText("平均每单利润："+r);
    }

    public VBox getDataBox() {
        return dataBox;
    }
}
