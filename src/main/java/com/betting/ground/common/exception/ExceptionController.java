package com.betting.ground.common.exception;

import com.betting.ground.common.dto.Response;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionController {

    private final SlackAlarmGenerator slackAlarmGenerator;

    @ExceptionHandler(value = GlobalException.class)
    public ResponseEntity<Response<Void>> handleGlobalExceptionHandler(GlobalException e) {
        return ResponseEntity.status(e.getErrorCode().getStatus())
                .body(Response.error(e.getErrorCode().getStatus().toString(), e.getErrorCode().getMessage()));
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Response<Void>> unhandledException(Exception e, HttpServletRequest request) {
        log.error("error occur {}",e);

//        slackAlarmGenerator.sendSlackAlertErrorLog(e, request);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Response.error(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.getMessage()));
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<List<Response<Void>>> argsValidHandler(MethodArgumentNotValidException e) {
        List<Response<Void>> errors = new ArrayList<>();
        e.getFieldErrors().stream()
                .forEach(error -> errors.add(Response.error(error.getField(), error.getDefaultMessage())));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errors);
    }

}
