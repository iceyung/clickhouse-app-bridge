package iceyung.app.datasource;


public enum DataSourceType {
    MySql("MySql", "com.mysql.jdbc.Driver"),
    Oracle("Oracle", "oracle.jdbc.OracleDriver"),
    DB2("DB2", "com.ibm.db2.jcc.DB2Driver");
    private String dataSourceName;
    private String driverClassName;
    public static String getDriver (String dataSourceName) {
        DataSourceType[] types = DataSourceType.values();
        for (DataSourceType type : types) {
            if (type.getDataSourceName().equals(dataSourceName)) {
                return type.getDriverClassName();
            }
        }
        return null;
    }

    private Object getDataSourceName() {
        return dataSourceName;
    }

    private String getDriverClassName() {
        return driverClassName;
    }

    DataSourceType (String dataSourceName,String driverClassName){
        this.dataSourceName = dataSourceName ;
        this.driverClassName = driverClassName ;
    }
}
