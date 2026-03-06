package com.wingtrip.payment.controller.errorhandling;


import com.wingtrip.payment.exception.*;
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

import static com.wingtrip.payment.constant.Constant.*;

@ControllerAdvice
@RestController
@Log4j2
public class GlobalExceptionHandler {

    @ExceptionHandler({
            PaymentNotFoundException.class,
            PaymentNotCreatedException.class,
            PaymentNotUpdatedException.class,
            PaymentAlreadyProcessedException.class,
            PaymentAlreadyRefundedException.class,
            PaymentCannotBeRefundedException.class,
            PaymentFailedException.class
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

        if (ex instanceof PaymentNotFoundException ||
                ex instanceof PaymentNotCreatedException ||
                ex instanceof PaymentNotUpdatedException ||
                ex instanceof PaymentAlreadyProcessedException ||
                ex instanceof PaymentAlreadyRefundedException ||
                ex instanceof PaymentCannotBeRefundedException ||
                ex instanceof PaymentFailedException) {
            return null;
        }

        Map<String, Object> result = new HashMap<>();
        result.put(TIMESTAMP, System.currentTimeMillis());
        result.put(STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value());
        result.put(ERROR, ex.getMessage());
        result.put(PATH, new UrlPathHelper().getPathWithinApplication(req));
        log.error("Generic exception occurred: {}" + ERROR, ex.getMessage());
        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
