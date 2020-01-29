package controller;

import java.util.Comparator;
import java.util.Map;

public class SaleComparator implements Comparator<Map.Entry<String,Integer>> {
    @Override
    public int compare(Map.Entry<String, Integer> t1, Map.Entry<String, Integer> t2) {
        if(t1.getValue()>t2.getValue())
            return -1;
        else if(t1.getValue()<=t2.getValue())
            return 1;
        return 0;
    }
}
