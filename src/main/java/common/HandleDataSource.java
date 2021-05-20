package common;

public class HandleDataSource {
    public static final String MASTER = "master";
    public static final String SLAVE = "slave";
    public static boolean isMaster(){
        return holder.get().equals(MASTER);
    }
    public static final ThreadLocal<String> holder = new ThreadLocal<String>();
    public static final ThreadLocal<Integer> sqlIdHolder = new ThreadLocal<Integer>();
    public static void putDataSourceId(Integer dataSourceId) {
        sqlIdHolder.set(dataSourceId);
    }
    public static Integer getDataSourceId(){
        return sqlIdHolder.get();
    }
    public static void putDataSource(String dataSource) {
        holder.set(dataSource);
    }
    public static String getDataSource(){
        return holder.get();
    }
}
