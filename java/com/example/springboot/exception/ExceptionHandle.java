package com.example.springboot.exception;

import com.example.springboot.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@ControllerAdvice
public class ExceptionHandle {

    @ExceptionHandler(value = ServiceException.class)
    public ResponseEntity<Result> handleServiceException(ServiceException e) {
        log.error("业务异常", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Result.error(e.getMessage()));
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Result> handleValidationException(MethodArgumentNotValidException ex) {
        log.error("参数验证错误: {}", ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Result.error(ex.getBindingResult().getAllErrors().get(0).getDefaultMessage()));
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Result> handleException(Exception e) {
        log.error("未处理的异常", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Result.error("服务器内部出错啦"));
    }
}


