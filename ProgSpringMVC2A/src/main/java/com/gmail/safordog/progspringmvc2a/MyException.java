package com.gmail.safordog.progspringmvc2a;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MyException extends RuntimeException {
}
