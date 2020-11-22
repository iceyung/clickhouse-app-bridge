package iceyung.app.controller;

import iceyung.app.AppService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {

    @Autowired(required = false)
    private AppService appService;

    @GetMapping("/ping")
    public String ping() {
        return "Ok.\n";
    }

    @PostMapping("/identifier_quote")
    public void identifierQuote(@RequestParam(value = "connection_string", required = false)
            String connectionString, HttpServletResponse response) {
        appService.getIdentifierQuote(connectionString, response);
    }

    @PostMapping("/columns_info")
    public void columnsInfo(@RequestParam(value = "schema", required = false) String schema,
            @RequestParam(value = "table", required = false) String table,
            @RequestParam(value = "connection_string", required = false) String connectionString,
            HttpServletResponse response) {
        appService.getColumnsInfo(connectionString, schema, table, response);
    }

    @PostMapping("/")
    public void query(@RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "connection_string", required = false) String connectionString,
            HttpServletRequest request,
            HttpServletResponse response) {
        appService.queryHandler(query, connectionString, request, response);
    }

}
