package learn.sharding.jdbc.example.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by sunyong on 2018-11-20.
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    @ResponseBody
    public String globalException(Exception e) {
        log.error("[exception]:message==>{} e==>{}", e.getMessage(), e);
        return "error";
    }
}
