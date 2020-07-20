package module.dataDA;

import module.Info;
import module.order.Order;

import java.io.File;
import java.util.List;

public interface DataManageable {
    //搜索方法
    List<Order> search();
    //获取全部数据
    List<Order> getAll();
    void getAll(List<Order> list);
    //选择文件
    boolean selectFile(File file);
    //获取综合数据
    Info getInfo();
    //获取数据多少
    Integer getDataSize();
}
