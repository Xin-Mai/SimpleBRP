package controller;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import module.Info;
import module.dataDA.DataAssistant;
import module.dataDA.DataManageable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DataController {
    //总单数
    public Label orderNums;
    //总金额
    public Label t_money;
    //总利润
    public Label t_profit;
    //平均利润率
    public Label a_profit_rate;
    //平均物流费用
    public Label a_logi;
    //平均利润
    public Label a_profit;
    public PieChart pie_chart;
    public JFXComboBox country;
    public JFXComboBox logis;
    public DataManageable DA;
    public BarChart bar_chart;
    public HBox dataBox;
    public List<Info> minfoList;

    final static String[] countries={"Afghanistan 阿富汗", "Aland Islands 奥兰群岛", "Albania 阿尔巴尼亚", "Algeria 阿尔及利亚",
            "American Samoa 美属萨摩亚",
            "Andorra 安道尔",
            "Angola 安哥拉",
            "Anguilla 安圭拉",
            "Antigua and Barbuda 安提瓜和巴布达",
            "Argentina 阿根廷",
            "Armenia 亚美尼亚",
            "Aruba 阿鲁巴",
            "Australia 澳大利亚",
            "Austria 奥地利",
            "Azerbaijan 阿塞拜疆",
            "Bangladesh 孟加拉",
            "Bahrain 巴林",
            "Bahamas 巴哈马",
            "Barbados 巴巴多斯",
            "Belarus 白俄罗斯",
            "Belgium 比利时",
            "Belize 伯利兹",
            "Benin 贝宁",
            "Bermuda 百慕大",
            "Bhutan 不丹",
            "Bolivia 玻利维亚",
            "Bosnia and Herzegovina 波斯尼亚和黑塞哥维那",
            "Botswana 博茨瓦纳",
            "Bouvet Island 布维岛",
            "Brazil 巴西",
            "Brunei 文莱",
            "Bulgaria 保加利亚",
            "Burkina Faso 布基纳法索",
            "Burundi 布隆迪",
            "Cambodia 柬埔寨",
            "Cameroon 喀麦隆",
            "Canada 加拿大",
            "Cape Verde 佛得角",
            "Central African Republic 中非",
            "Chad 乍得",
            "Chile 智利",
            "Christmas Islands 圣诞岛",
            "Cocos (keeling) Islands 科科斯（基林）群岛",
            "Colombia 哥伦比亚",
            "Comoros 科摩罗",
            "Congo (Congo-Kinshasa) 刚果（金）",
            "Congo 刚果",
            "Cook Islands 库克群岛",
            "Costa Rica 哥斯达黎加",
            "Cote D'Ivoire 科特迪瓦",
            "China 中国",
            "Croatia 克罗地亚",
            "Cuba 古巴",
            "Czech 捷克",
            "Cyprus 塞浦路斯",
            "Denmark 丹麦",
            "Djibouti 吉布提",
            "Dominica 多米尼加",
            "Ecuador 厄瓜多尔",
            "Egypt 埃及",
            "Equatorial Guinea 赤道几内亚",
            "Eritrea 厄立特里亚",
            "Estonia 爱沙尼亚",
            "Ethiopia 埃塞俄比亚",
            "Faroe Islands 法罗群岛",
            "Fiji 斐济",
            "Finland 芬兰",
            "France 法国",
            "MetropolitanFrance 法国大都会",
            "French Guiana 法属圭亚那",
            "French Polynesia 法属波利尼西亚",
            "Gabon 加蓬",
            "Gambia 冈比亚",
            "Georgia 格鲁吉亚",
            "Germany 德国",
            "Ghana 加纳",
            "Gibraltar 直布罗陀",
            "Greece 希腊",
            "Grenada 格林纳达",
            "Guadeloupe 瓜德罗普岛",
            "Guam 关岛",
            "Guatemala 危地马拉",
            "Guernsey 根西岛",
            "Guinea-Bissau 几内亚比绍",
            "Guinea 几内亚",
            "Guyana 圭亚那",
            "Haiti 海地",
            "Honduras 洪都拉斯",
            "Hungary 匈牙利",
            "Iceland 冰岛",
            "India 印度",
            "Indonesia 印度尼西亚",
            "Iran 伊朗",
            "Iraq 伊拉克",
            "Ireland 爱尔兰",
            "Isle of Man 马恩岛",
            "Israel 以色列",
            "Italy 意大利",
            "Jamaica 牙买加",
            "Japan 日本",
            "Jersey 泽西岛",
            "Jordan 约旦",
            "Kazakhstan 哈萨克斯坦",
            "Kenya 肯尼亚",
            "Kiribati 基里巴斯",
            "Korea (South) 韩国",
            "Korea (North) 朝鲜",
            "Kuwait 科威特",
            "Kyrgyzstan 吉尔吉斯斯坦",
            "Laos 老挝",
            "Latvia 拉脱维亚",
            "Lebanon 黎巴嫩",
            "Lesotho 莱索托",
            "Liberia 利比里亚",
            "Libya 利比亚",
            "Liechtenstein 列支敦士登",
            "Lithuania 立陶宛",
            "Luxembourg 卢森堡",
            "Macedonia 马其顿",
            "Malawi 马拉维",
            "Malaysia 马来西亚",
            "Madagascar 马达加斯加",
            "Maldives 马尔代夫",
            "Mali 马里",
            "Malta 马耳他",
            "Marshall Islands 马绍尔群岛",
            "Martinique 马提尼克岛",
            "Mauritania 毛里塔尼亚",
            "Mauritius 毛里求斯",
            "Mayotte 马约特",
            "Mexico 墨西哥",
            "Micronesia 密克罗尼西亚",
            "Moldova 摩尔多瓦",
            "Monaco 摩纳哥",
            "Mongolia 蒙古",
            "Montenegro 黑山",
            "Montserrat 蒙特塞拉特",
            "Morocco 摩洛哥",
            "Mozambique 莫桑比克",
            "Myanmar 缅甸",
            "Namibia 纳米比亚",
            "Nauru 瑙鲁", "Nepal 尼泊尔",
            "Netherlands 荷兰",
            "New Caledonia 新喀里多尼亚",
            "New Zealand 新西兰",
            "Nicaragua 尼加拉瓜",
            "Niger 尼日尔",
            "Nigeria 尼日利亚",
            "Niue 纽埃",
            "Norfolk Island 诺福克岛",
            "Norway 挪威",
            "Oman 阿曼",
            "Pakistan 巴基斯坦",
            "Palau 帕劳",
            "Palestine 巴勒斯坦",
            "Panama 巴拿马",
            "Papua New Guinea 巴布亚新几内亚",
            "Peru 秘鲁",
            "Philippines 菲律宾",
            "Pitcairn Islands 皮特凯恩群岛",
            "Poland 波兰",
            "Portugal 葡萄牙",
            "Puerto Rico 波多黎各",
            "Qatar 卡塔尔",
            "Reunion 留尼汪岛",
            "Romania 罗马尼亚",
            "Rwanda 卢旺达",
            "Russian Federation 俄罗斯联邦",
            "Saint Helena 圣赫勒拿",
            "Saint Kitts-Nevis 圣基茨和尼维斯",
            "Saint Lucia 圣卢西亚",
            "Saint Vincent and the Grenadines 圣文森特和格林纳丁斯",
            "El Salvador 萨尔瓦多",
            "Samoa 萨摩亚",
            "San Marino 圣马力诺",
            "Sao Tome and Principe 圣多美和普林西比",
            "Saudi Arabia 沙特阿拉伯",
            "Senegal 塞内加尔",
            "Seychelles 塞舌尔",
            "Sierra Leone 塞拉利昂",
            "Singapore 新加坡",
            "Serbia 塞尔维亚",
            "Slovakia 斯洛伐克",
            "Slovenia 斯洛文尼亚",
            "Solomon Islands 所罗门群岛",
            "Somalia 索马里",
            "South Africa 南非",
            "Spain 西班牙",
            "Sri Lanka 斯里兰卡",
            "Sudan 苏丹",
            "Suriname 苏里南",
            "Swaziland 斯威士兰",
            "Sweden 瑞典",
            "Switzerland 瑞士",
            "Syria 叙利亚",
            "Tajikistan 塔吉克斯坦",
            "Tanzania 坦桑尼亚",
            "Thailand 泰国",
            "Trinidad and Tobago 特立尼达和多巴哥",
            "Timor-Leste 东帝汶",
            "Togo 多哥",
            "Tokelau 托克劳",
            "Tonga 汤加",
            "Tunisia 突尼斯",
            "Turkey 土耳其",
            "Turkmenistan 土库曼斯坦",
            "Tuvalu 图瓦卢",
            "Uganda 乌干达",
            "Ukraine 乌克兰",
            "United Arab Emirates 阿拉伯联合酋长国",
            "United Kingdom 英国",
            "United States 美国",
            "Uruguay 乌拉圭",
            "Uzbekistan 乌兹别克斯坦",
            "Vanuatu 瓦努阿图",
            "Vatican City 梵蒂冈",
            "Venezuela 委内瑞拉",
            "Vietnam 越南",
            "Wallis and Futuna 瓦利斯群岛和富图纳群岛",
            "Western Sahara 西撒哈拉",
            "Yemen 也门",
            "Yugoslavia 南斯拉夫",
            "Zambia 赞比亚",
            "Zimbabwe 津巴布韦"
    };

    public DataController(){

    }

    public DataController(DataManageable DA){
        this.DA=DA;
        //this.minfoList=DA.getData();
    }

    public DataManageable getDA() {
        return DA;
    }

    public void setDA(DataManageable DA) {
        this.DA = DA;
    }

    @FXML
    private void initialize(){
        //this.DA=new DataAssistant();
        //this.minfoList=DA.getData();
    }

    public void initBarChart(){
        /*if(this.infoList==null)
            this.infoList=this.DA.getData();*/
        double money=0.0;
        double log=0.0;
        double profit=0;
        for(Info i:this.minfoList){
            money+=i.getMoney();
            profit+=i.getProfit();
            log+=i.getTotalLog();
        }
        if(this.bar_chart!=null){
            XYChart.Series series=new XYChart.Series();
            for(int i=0;i<5&&i<minfoList.size();i++){
                Info info=minfoList.get(i);
                series.getData().add(new XYChart.Data<String,Integer>(info.getCountry(),info.getOrdersNum()));
            }
            series.setName("销量");
            bar_chart.getData().add(series);
        }
        if(this.orderNums!=null)
            this.orderNums.setText(this.minfoList.size()+"");
        if(this.t_money!=null){
            this.t_money.setText(round2(money)+"元");
            this.t_money.setStyle("-fx-text-fill:#558fe8;");
        }
        if(this.t_profit!=null){
            this.t_profit.setText(round2(profit)+"元");
        }
        if(this.a_profit_rate!=null)
            this.a_profit_rate.setText(round2(profit/money*100)+"%");
        if(this.a_logi!=null)
            this.a_logi.setText(round2(log/minfoList.size())+"元");
        if(this.a_profit!=null)
            this.a_profit.setText(round2(profit/minfoList.size())+"元");
    }

    public void initPieChart(){
        if(this.pie_chart!=null){
            if(this.country!=null){
                this.country.getItems().add("全部国家");
                for(String c:countries){
                    this.country.getItems().add(c);
                }
                country.setValue("全部国家");
            }
            if(this.logis!=null){
                this.logis.getItems().add("全部物流");
                this.logis.setValue("全部物流");
            }
            updatePieChart(country.getValue().toString(),logis.getValue().toString());
        }
    }

    private void updatePieChart(String country,String log){
        Info info=null;
        String con=country;
        if(minfoList==null)
            minfoList=this.DA.getData();
        for(String s:countries){
            if(s.contains(country))
            {
                this.country.setValue(s);
                con=s.substring(0,s.lastIndexOf(" "));
                break;
            }
        }
        for(Info i:minfoList){
            if(i.getCountry().contains(con)){
                info=i;
                break;
            }
        }
        ObservableList<PieChart.Data> pieChartData=FXCollections.observableArrayList();
        //不是全部
        Map<String,Integer> bestSales;
        if(info!=null)
        {
            //bestSales=info.getBestSales();
            bestSales=info.getSimpleBestSales();
        }
        else{
            bestSales=new HashMap<>();
            for(Info i:minfoList){
                //将i.bestSales合并到bestSales中
                //i.getBestSales().forEach((key,value)->bestSales.merge(key,value,Integer::sum));
                i.getSimpleBestSales().forEach((key,value)->bestSales.merge(key,value,Integer::sum));
            }
        }
        pieChartData.clear();
        //仅售出1、2只的款式
        int other=0;
        int other2=0;
        List<String> one = new ArrayList<>();
        List<String> two=new ArrayList<>();
        for(String id:bestSales.keySet()){
            if(bestSales.get(id)<=1)
            {
                other++;
                one.add(id);
            }
            else if(bestSales.get(id)==2)
            {
                other2++;
                two.add(id);
            }
            else
                pieChartData.add(new PieChart.Data(id,bestSales.get(id)));
        }
        pieChartData.add(new PieChart.Data("其他1",other));
        pieChartData.add(new PieChart.Data("其他2",other2));
        //获取pieCHart数据
        pie_chart.setData(pieChartData);
        pie_chart.getData().forEach(d -> {
            Tooltip tip = new Tooltip();
            String text;
            if(d.getName().equals("其他1")){
                text="以下款式仅售出1件：";
                for(String o:one){
                    text+="\n"+o;
                }
            }
            else if(d.getName().equals("其他2")){
                text="以下款式仅售出2件：";
                for(String o:two){
                    text+="\n"+o;
                }
            }
            else
                text="共售出"+d.getPieValue()+"件";
            tip.setText(text);
            Tooltip.install(d.getNode(), tip);
        });
    }

    //根据选择的内容搜索
    public void onSearch(){
        //country.setValue(country.getItems().get(0));
        updatePieChart(country.getValue().toString(),logis.getValue().toString());
    }
    private String round2(double num){
        String s=String.valueOf(num);
        s=s.substring(0,s.indexOf(".")+2);
        return s;
    }
    public BarChart getBar_chart() {
        return bar_chart;
    }

    public void setBar_chart(BarChart bar_chart) {
        this.bar_chart = bar_chart;
    }

    public HBox getDataBox() {
        return dataBox;
    }

    public void setDataBox(HBox dataBox) {
        this.dataBox = dataBox;
        this.bar_chart=(BarChart<String,Integer>)dataBox.getChildren().get(0);
        VBox vBox=(VBox)dataBox.getChildren().get(1);
        vBox=(VBox)vBox.getChildren().get(0);
        HBox hBox1=(HBox)(vBox.getChildren().get(0));
        HBox hBox2=(HBox)(vBox.getChildren().get(1));
        this.orderNums=(Label)hBox1.getChildren().get(0);
        this.t_money=(Label)hBox1.getChildren().get(1);
        this.t_profit=(Label)hBox1.getChildren().get(2);
        this.a_profit_rate=(Label)hBox2.getChildren().get(0);
        this.a_logi=(Label)hBox2.getChildren().get(1);
        this.a_profit=(Label)hBox2.getChildren().get(2);
    }

    public PieChart getPie_chart() {
        return pie_chart;
    }

    public void setPie_chart(PieChart pie_chart) {
        this.pie_chart = pie_chart;
    }

    public JFXComboBox getCountry() {
        return country;
    }

    public void setCountry(JFXComboBox country) {
        this.country = country;
    }

    public JFXComboBox getLogis() {
        return logis;
    }

    public void setLogis(JFXComboBox logis) {
        this.logis = logis;
    }

    public void setInfoList(List<Info> infos){
        this.minfoList=infos;
    }
    //自动补全
    public void onTextChange(KeyEvent keyEvent) {
        if(country!=null)
        {
            String content;
            if(country.getValue()!=null){
                content=country.getValue().toString();
                country.getItems().clear();
                for(String s:countries){
                    if(s.contains(content))
                        country.getItems().add(s);
                }
            }
            country.getItems().add("全部国家");
            country.show();
        }
    }
}
