package ru.gb.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class ExceptionHandlingController {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<HttpStatus> catchNoSuchElementException(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(IssueRejectedException.class)
    public ResponseEntity<HttpStatus> catchIssueRejectedException(){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }


}