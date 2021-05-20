package master;

import com.alibaba.druid.pool.DruidDataSource;
import common.HandleDataSource;
import common.IDContainer;
import common.IDGenerator;
import common.TempDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@ComponentScan(basePackages = "master")
public class DynamicDataSourceConfig {


    @Bean(name = "master")
    @Primary
    public DataSource master(){
        DruidDataSource dataSource = new DruidDataSource();

        dataSource.setUrl("jdbc\\:mysql\\://192.168.0.172\\:3306/esmajiang?useUnicode\\=true&characterEncoding\\=utf8");
        dataSource.setUsername("chxiao");
        dataSource.setPassword("chxiao");
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setValidationQuery("SELECT 1");//用来检测连接是否有效
        dataSource.setTestOnBorrow(false);//申请连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能
        dataSource.setTestOnReturn(false);//归还连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能
        //申请连接的时候检测，如果空闲时间大于timeBetweenEvictionRunsMillis，执行validationQuery检测连接是否有效。
        dataSource.setTestWhileIdle(true);//如果检测失败，则连接将被从池中去除
        dataSource.setTimeBetweenEvictionRunsMillis(600000);
        dataSource.setMaxActive(20);
        dataSource.setInitialSize(10);
        System.out.println("here is master -----------------");
        return dataSource;
    }
    @Bean(name = "slave")
    public DataSource slave(){
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc\\:mysql\\://127.0.0.1\\:3306/esmajiang?useUnicode\\=true&characterEncoding\\=utf8");
        dataSource.setUsername("root");
        dataSource.setPassword("");
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");

        dataSource.setValidationQuery("SELECT 1");//用来检测连接是否有效
        dataSource.setTestOnBorrow(false);//申请连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能
        dataSource.setTestOnReturn(false);//归还连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能
        //申请连接的时候检测，如果空闲时间大于timeBetweenEvictionRunsMillis，执行validationQuery检测连接是否有效。
        dataSource.setTestWhileIdle(true);//如果检测失败，则连接将被从池中去除
        dataSource.setTimeBetweenEvictionRunsMillis(600000);
        dataSource.setMaxActive(20);
        dataSource.setInitialSize(10);
        System.out.println("here is slave -----------------");
        return dataSource;
    }
    @Primary
    @Bean(name = "masterTemDataSource")
    public TempDataSource masterTemDataSource(@Qualifier("master")DataSource master){
        TempDataSource tempDataSource = new TempDataSource();
        tempDataSource.setId(IDGenerator.ID_GENERATROR.getAndIncrement());
        tempDataSource.setDataSource(master);
        tempDataSource.setName("master");
        tempDataSource.setMaster(true);
        return tempDataSource;
    }

    @Bean(name = "slaveTemDataSource")
    public TempDataSource slaveTemDataSource(@Qualifier("slave") DataSource slave){
        TempDataSource tempDataSource = new TempDataSource();
        tempDataSource.setId(IDGenerator.ID_GENERATROR.getAndIncrement());
        tempDataSource.setDataSource(slave);
        tempDataSource.setName("master");
        tempDataSource.setMaster(false);
        return tempDataSource;
    }

    @Bean
    @Primary
    public DynamicDataSource dataSource(TempDataSource slaveTemDataSource,TempDataSource masterTemDataSource){
        Map<Object,Object> targetDataSources = new HashMap<Object, Object>();
        List<TempDataSource> list = new ArrayList<TempDataSource>();
        list.add(slaveTemDataSource);
        list.add(masterTemDataSource);
        for (TempDataSource dataSource : list){
            targetDataSources.put(dataSource.getId(),dataSource.getDataSource());
            List<Integer> holdData = IDContainer.getInstance().getHoldMap().get(dataSource.getId());
            if (holdData == null ){
                holdData = new ArrayList<Integer>();
            }
            holdData.add(dataSource.getId());
            IDContainer.getInstance().getHoldMap().put(dataSource.getName(),holdData);



        }
        return new DynamicDataSource(master(),targetDataSources,masterTemDataSource.getId());

    }


}
