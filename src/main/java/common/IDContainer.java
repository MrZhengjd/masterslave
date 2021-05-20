package common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class IDContainer {
    private Map<String,List<Integer>> holdMap;
    private List<Integer> slaveDatas ;;

    public Map<String, List<Integer>> getHoldMap() {
        return holdMap;
    }

    public List<Integer> getSlaveDatas() {
        return slaveDatas;
    }

    public void setSlaveDatas(List<Integer> slaveDatas) {
        this.slaveDatas = slaveDatas;
    }

    private IDContainer(){
        slaveDatas = new ArrayList<Integer>();
        holdMap = new HashMap<String, List<Integer>>();
    }
    public void putData(Integer name){
        slaveDatas.add(name);
    }
    private static class IDHolder{
        private static IDContainer INSTANCE = new IDContainer();
    }
    public static IDContainer getInstance(){
        return IDHolder.INSTANCE;
    }
}
