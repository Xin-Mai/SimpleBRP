package controller.thread;

import controller.PageManager;
import module.dataDA.DataManageable;
import module.order.Order;

import java.util.ArrayList;
import java.util.List;

public class LoadThread implements Runnable {
    private PageManager pageManager;
    private List<Order> orderList;
    private DataManageable DA;

    public LoadThread(){orderList=new ArrayList<>();}
    public LoadThread(PageManager manager,DataManageable DA){
        this.pageManager=manager;
        this.DA=DA;
        orderList=new ArrayList<>();
    }
    @Override
    public void run(){
        while(DA==null);
        this.pageManager.setOrders(orderList
        );
        DA.getAll(orderList);
    }

    public PageManager getPageManager() {
        return pageManager;
    }

    public void setPageManager(PageManager pageManager) {
        this.pageManager = pageManager;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }

    public DataManageable getDA() {
        return DA;
    }

    public void setDA(DataManageable DA) {
        this.DA = DA;
    }
}
