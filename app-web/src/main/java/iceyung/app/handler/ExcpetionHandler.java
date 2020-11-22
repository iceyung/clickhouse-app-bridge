package iceyung.app.handler;


import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ExcpetionHandler {

    @SneakyThrows
    @ExceptionHandler(value = Exception.class)
    public void exceptionHandler(HttpServletRequest request, HttpServletResponse response,
            Exception e) {
        log.error(e.getMessage(), e);
        response.sendError(HttpStatus.SC_INTERNAL_SERVER_ERROR,
                Objects.nonNull(e.getMessage()) ? e.getMessage() : "null");
    }

}
