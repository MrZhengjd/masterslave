package master;

import common.HandleDataSource;
import common.IDContainer;
import common.RandomUtil;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

public class DynamicDataSource extends AbstractRoutingDataSource {

    private int masterId;

    public int getMasterId() {
        return masterId;
    }

    public void setMasterId(int masterId) {
        this.masterId = masterId;
    }

    /**
     * 配置DataSource, defaultTargetDataSource为主数据库
     */
    public DynamicDataSource(DataSource defaultTargetDataSource, Map<Object, Object> targetDataSources,int masterId) {
        super.setDefaultTargetDataSource(defaultTargetDataSource);
        super.setTargetDataSources(targetDataSources);
        super.afterPropertiesSet();
        setMasterId(masterId);
    }
    @Override
    protected Object determineCurrentLookupKey() {

        if(HandleDataSource.getDataSource() == null){
            return IDContainer.getInstance().getHoldMap().get("master").get(0);
        }
        List<Integer> temHold = IDContainer.getInstance().getHoldMap().get(HandleDataSource.getDataSource());
        if (HandleDataSource.isMaster()){
            return IDContainer.getInstance().getHoldMap().get("master").get(0);
        }

        return temHold.get(RandomUtil.getInstance().getRandom(temHold.size()));


    }
}
