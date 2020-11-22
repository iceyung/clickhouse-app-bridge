package iceyung.app.utils;


import static iceyung.app.utils.JdbcConstant.DATASOURCE_TYPE;

import com.google.common.collect.Maps;
import iceyung.app.datasource.ClickHouseDataType;
import iceyung.app.datasource.DataSourceType;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
import ru.yandex.clickhouse.ClickHouseUtil;
import ru.yandex.clickhouse.util.ClickHouseRowBinaryStream;

public class JdbcParseStringUtil {

    public static Map<String, String> parse(String connectString) {
        String dataSourceType;
        if (connectString.startsWith("jdbc:mysql")) {
            dataSourceType = DataSourceType.MySql.name();
        } else if (connectString.startsWith("jdbc:h2")) {
            dataSourceType = DataSourceType.DB2.name();
        } else if (connectString.startsWith("jdbc:oracle")) {
            dataSourceType = DataSourceType.Oracle.name();
        } else {
            throw new RuntimeException("jdbc not supported!");
        }
        String[] sp = connectString.split("\\?");
        if (sp.length > 1) {
            String[] messages = sp[1].split("\\&");
            if (messages.length > 1) {
                HashMap<String, String> connectMaps = Maps.newHashMap();
                connectMaps.put(DATASOURCE_TYPE, dataSourceType);
                for (String ms : messages) {
                    String[] split = ms.split("\\=");
                    if (split.length > 1) {
                        connectMaps.put(split[0].trim(), split[1].trim());
                    }
                }
                return connectMaps;
            }

        }
        throw new RuntimeException("connect_string error!");
    }

    public static String getColumnsDDL(ResultSetMetaData meta) throws SQLException {
        StringBuilder builder = new StringBuilder("columns format version: 1\n");

        builder.append(meta.getColumnCount());
        builder.append(" columns:\n");
        for (int i = 1; i <= meta.getColumnCount(); i++) {
            int precision = meta.getPrecision(i);
            int type = meta.getColumnType(i);
            int scale = meta.getScale(i);

            boolean nullable = ResultSetMetaData.columnNullable == meta.isNullable(i);
            builder.append(ClickHouseUtil.quoteIdentifier(meta.getColumnName(i)));
            builder.append(" ");
            builder.append(getBySQLType(type, nullable, precision, scale));
            builder.append('\n');
        }
        return builder.toString();
    }

    /**
     * 类型设置
     * @param type
     * @param nullable
     * @param precision
     * @param scale
     * @return
     */
    private static String getBySQLType(int type, boolean nullable, int precision, int scale) {
        switch (type) {
            case Types.INTEGER :
                return ClickHouseDataType.Int32.getName(nullable, 0, 0);
            case Types.VARCHAR:
                return ClickHouseDataType.String.getName(nullable, 0, 0);
            case Types.DECIMAL:
                return ClickHouseDataType.Decimal.getName(nullable, 38, 10);
            default:
                break;
        }
        throw new RuntimeException(String.format("data type %s not supported!", type));
    }

    /**
     * 数据写入/类型设置
     * @param resultSet
     * @param metaData
     * @param stream
     * @throws SQLException
     * @throws IOException
     */
    public static void serialize(ResultSet resultSet,ResultSetMetaData metaData,
            ClickHouseRowBinaryStream stream) throws SQLException, IOException {
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            if (metaData.getColumnType(i) == Types.INTEGER) {
                stream.writeInt32(resultSet.getInt(i));
            }
            if (metaData.getColumnType(i) == Types.VARCHAR) {
                stream.writeString(resultSet.getString(i));
            }
            if (metaData.getColumnType(i) == Types.DECIMAL) {
                stream.writeDecimal128(resultSet.getBigDecimal(i), 10);
            }
        }
    }

}
