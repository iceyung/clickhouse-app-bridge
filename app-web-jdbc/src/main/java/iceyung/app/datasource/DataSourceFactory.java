package iceyung.app.datasource;


import com.alibaba.druid.pool.DruidDataSource;
import iceyung.app.entity.ConnectionEntity;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DataSourceFactory {
    private volatile Map<Integer, DataSource> dataSourceMap = new HashMap<>();
    @Resource
    private JdbcConfig jdbcConfig ;

    /**
     * 数据源API包装
     */
    private static DataSource getDataSource (ConnectionEntity connectionEntity){
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(connectionEntity.getJdbcUrl());
        datasource.setUsername(connectionEntity.getUserName());
        datasource.setPassword(connectionEntity.getPassWord());
        datasource.setDriverClassName(connectionEntity.getDriverClassName());
        return datasource ;
    }
    /**
     * 获取 JDBC 链接
     */
    public JdbcTemplate getById (Integer id){
        return new JdbcTemplate(dataSourceMap.get(id)) ;
    }
    /**
     * 移除 数据源
     */
    public void removeById (Integer id) {
        dataSourceMap.remove(id) ;
    }

    /**
     * 添加数据源管理
     * 注意这里的方法，连接验证之后直接调用
     */
    public void addDataSource (ConnectionEntity connectionEntity){
        DataSource dataSource = getDataSource(connectionEntity);
        dataSourceMap.put(connectionEntity.getId(),dataSource) ;
    }
}