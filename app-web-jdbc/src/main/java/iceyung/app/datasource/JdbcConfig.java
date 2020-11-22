package iceyung.app.datasource;

import iceyung.app.entity.ConnectionEntity;
import iceyung.app.utils.ConnectionUtil;
import java.sql.Connection;
import org.springframework.stereotype.Component;

@Component
public class JdbcConfig {

    public Connection getConnection (ConnectionEntity connectionEntity){
        String dataTypeName = connectionEntity.getDataTypeName();
        String driverClassName = DataSourceType.getDriver(dataTypeName) ;
        if (driverClassName == null){
            throw new RuntimeException("no support the jdbc driver!") ;
        }
        connectionEntity.setDriverClassName(driverClassName);
        return ConnectionUtil.getConnect(connectionEntity.getDriverClassName(),
                connectionEntity.getUserName(),
                connectionEntity.getPassWord(),
                connectionEntity.getJdbcUrl()) ;
    }
}
