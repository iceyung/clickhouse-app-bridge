package iceyung.app.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConnectionEntity {

    private int id;

    private String dataTypeName;

    private String driverClassName;

    private String userName;

    private String passWord;

    private String jdbcUrl;


}
