package com.wingtrip.user.controller.errorhandling;

import com.wingtrip.user.exception.*;
import com.wingtrip.user.util.DateTimeUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UrlPathHelper;

import java.util.HashMap;
import java.util.Map;

import static com.wingtrip.user.constant.Constant.*;

@ControllerAdvice
@RestController
@Log4j2
class GlobalExceptionHandler {

    @ExceptionHandler({
            UserNotCreateException.class,
            UserIdNotFoundException.class,
            UserDeleteFailedException.class,
            UsernameNotFoundException.class,
            UsernameAlreadyExistsException.class,
            EmailNotFoundException.class,
            EmailAlreadyExistsException.class
    })
    public ResponseEntity<Map<String, Object>> handleCustomException(HttpServletRequest req, Exception ex) {
        Map<String, Object> result = new HashMap<>();
        result.put(TIMESTAMP, System.currentTimeMillis());
        result.put(STATUS, HttpStatus.BAD_REQUEST.value());
        result.put(ERROR, ex.getMessage());
        result.put(PATH, new UrlPathHelper().getPathWithinApplication(req));
        log.error("Custom exception occurred: {}" + ERROR, ex.getMessage());
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(HttpServletRequest req, Exception ex) {

        if (ex instanceof UserNotCreateException ||
                ex instanceof UserIdNotFoundException ||
                ex instanceof UserDeleteFailedException ||
                ex instanceof UsernameNotFoundException ||
                ex instanceof UsernameAlreadyExistsException ||
                ex instanceof EmailNotFoundException ||
                ex instanceof EmailAlreadyExistsException) {
            return null;
        }

        Map<String, Object> result = new HashMap<>();
        result.put(TIMESTAMP, DateTimeUtil.now().toEpochDay());
        result.put(STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value());
        result.put(ERROR, ex.getMessage());
        result.put(PATH, new UrlPathHelper().getPathWithinApplication(req));
        log.error("Generic exception occurred: {}" + ERROR, ex.getMessage());
        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
