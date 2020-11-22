package iceyung.app.service;


import iceyung.app.AppService;
import iceyung.app.datasource.JdbcConfig;
import iceyung.app.entity.ConnectionEntity;
import iceyung.app.utils.JdbcConstant;
import iceyung.app.utils.JdbcParseStringUtil;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.clickhouse.settings.ClickHouseProperties;
import ru.yandex.clickhouse.util.ClickHouseRowBinaryStream;
import ru.yandex.clickhouse.util.guava.StreamUtils;

@Slf4j
@Service
public class AppServiceImpl implements AppService {

    @Autowired
    private JdbcConfig jdbcConfig;

    @SneakyThrows
    @Override
    public void queryHandler(String query, String connectionString, HttpServletRequest request,
            HttpServletResponse response) {
        if (StringUtils.isBlank(query)) {
            String requestBody = StreamUtils.toString(request.getInputStream());
            String[] parts = requestBody.split("query=", 2);
            if (parts.length == 2) {
                query = parts[1];
            }
        }
        log.info("query {}", query);

        if (StringUtils.isBlank(query)) {
            throw new IllegalArgumentException("Query is blank or empty");
        }

        try (Connection connection = getConnectByString(connectionString);
                Statement sth = connection.createStatement()) {
            ResultSet resultSet = sth.executeQuery(query);
            ResultSetMetaData meta = resultSet.getMetaData();

            ClickHouseRowBinaryStream stream = new ClickHouseRowBinaryStream(
                    response.getOutputStream(), null, new ClickHouseProperties());

            response.setContentType("application/octet-stream");

            while (resultSet.next()) {
                JdbcParseStringUtil.serialize(resultSet, meta, stream);
            }
            log.info("meta.getColumnCount() {}", meta.getColumnCount());
        }catch (Exception ex) {
            throw new RuntimeException(ex.getMessage(),ex);
        }

    }

    @Override
    public void getColumnsInfo(String connectionString, String schema,
            String table,
            HttpServletResponse response) {
        try(Connection connection = getConnectByString(connectionString);
                Statement sth = connection.createStatement()) {
            String quote = connection.getMetaData().getIdentifierQuoteString();

            String schemaAndTable = Stream.of(schema, table)
                    .filter(s -> !StringUtils.isBlank(s))
                    .map(s -> quote + s + quote)
                    .collect(Collectors.joining("."));

            String queryRewrite = "SELECT * FROM " + schemaAndTable + " WHERE 1 = 0";
            ResultSet resultSet = sth.executeQuery(queryRewrite);
            String ddl = JdbcParseStringUtil.getColumnsDDL(resultSet.getMetaData());
            response.setContentType("application/octet-stream");
            ClickHouseRowBinaryStream stream = new ClickHouseRowBinaryStream(
                    response.getOutputStream(),
                    null, new ClickHouseProperties());
            stream.writeString(ddl);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage(),ex);
        }
    }

    @Override
    public void getIdentifierQuote(String connectionString, HttpServletResponse response) {

        try( Connection connection = getConnectByString(connectionString)) {
            ClickHouseRowBinaryStream stream = new ClickHouseRowBinaryStream(
                    response.getOutputStream(),
                    null, new ClickHouseProperties());
            response.setContentType("application/octet-stream");

            String identifierQuoteString = connection.getMetaData().getIdentifierQuoteString();
            stream.writeString(identifierQuoteString);
        }catch (Exception ex) {
            throw new RuntimeException(ex.getMessage(),ex);
        }
    }

    private Connection getConnectByString(String connectionString) {
        Connection connection = jdbcConfig.getConnection(ConnectionEntity.builder()
                .dataTypeName(JdbcParseStringUtil.parse(connectionString)
                        .get(JdbcConstant.DATASOURCE_TYPE))
                .jdbcUrl(connectionString)
                .userName(JdbcParseStringUtil.parse(connectionString)
                        .get(JdbcConstant.DATASOURCE_USER))
                .passWord(JdbcParseStringUtil.parse(connectionString)
                        .get(JdbcConstant.DATASOURCE_PASSWORD))
                .build());
        return connection;
    }
}
