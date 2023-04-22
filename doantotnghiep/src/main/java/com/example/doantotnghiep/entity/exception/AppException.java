package com.example.doantotnghiep.entity.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Getter
@Setter
@ToString
@JsonIgnoreProperties({"stackTrace", "cause", "suppressed", "localizedMessage"})
public class AppException extends RuntimeException {
    private Instant timestamp;
    private int code;
    private String message;
    private String path;

    public AppException(int code, String message) {
        this.code = code;
        this.message = message;
        this.timestamp = Instant.now();
    }

    public AppException(int code, String message, String path) {
        this.code = code;
        this.message = message;
        this.timestamp = Instant.now();
        this.path = path;
    }

    public AppException(ErrorResponseBase errorResponseBase) {
        this.code = errorResponseBase.getStatus().value();
        this.message = errorResponseBase.message;
        this.timestamp = Instant.now();
    }

    public AppException(Exception ex) {
        String mes = ex.getMessage();
        if (mes == null) {
            this.code = HttpStatus.INTERNAL_SERVER_ERROR.value();
            this.message = "NullPointerException";
        } else if (mes.contains("Access Denied")) {
            this.code = ErrorResponseBase.UNAUTHORIZED.status.value();
            this.message = ErrorResponseBase.UNAUTHORIZED.getMessage();
        } else if (mes.contains("not found") || mes.contains("not exist") || mes.contains("NoSuchKey") || mes.contains("not_exist")) {
            this.code = ErrorResponseBase.NOT_FOUND.status.value();
            this.message = ErrorResponseBase.NOT_FOUND.getMessage();
        } else {
            this.code = HttpStatus.INTERNAL_SERVER_ERROR.value();
            this.message = ex.getMessage();
        }
        this.timestamp = Instant.now();
    }


//    @ExceptionHandler(BindException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public AppException handleBindException(BindException e, HttpServletRequest request) {
//        String errorMessage = "";
//        if (e.getBindingResult().hasErrors()){
//            for(int i=0;i<= e.getBindingResult().getAllErrors().size();i++){
//                errorMessage += e.getBindingResult().getAllErrors().get(0).getDefaultMessage() +"\n";
//            }
//        }
//        AppException appException= new AppException(400,errorMessage, request.getRequestURI());
//        return appException;
//    }
}
