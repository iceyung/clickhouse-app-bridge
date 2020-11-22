package iceyung.app.utils;


import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class ConnectionUtil {

    public static synchronized Connection getConnect(String driverClassName, String userName,
            String passWord, String jdbcUrl) {
        Properties prop = new Properties();
        prop.put("user", userName);
        prop.put("password", passWord);
        return connect(driverClassName, prop, jdbcUrl);
    }

    private static synchronized Connection connect(String driverClassName,
            Properties prop, String jdbcUrl) {
        try {
            Class.forName(driverClassName);
            DriverManager.setLoginTimeout(JdbcConstant.LOGIN_TIMEOUT);
            return DriverManager.getConnection(jdbcUrl, prop);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}