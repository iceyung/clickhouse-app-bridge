package iceyung.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AppService {

    void queryHandler(String query, String connectionString,
            HttpServletRequest request, HttpServletResponse response);

    void getColumnsInfo(String connectionString, String schema, String table,
            HttpServletResponse response);

    void getIdentifierQuote(String connectionString, HttpServletResponse response);

}
