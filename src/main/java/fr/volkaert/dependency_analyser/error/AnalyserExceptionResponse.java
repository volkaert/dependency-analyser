package fr.volkaert.dependency_analyser.error;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;

@Data
public class AnalyserExceptionResponse {
    private Instant timestamp;
    private int status;
    private String error;
    private String message;
    private String path;

    public AnalyserExceptionResponse(AnalyserException ex) {
        this.timestamp = ex.getTimestamp();
        this.status = ex.getHttpStatusCode();
        this.error = ex.getHttpStatusMessage();
        this.message = ex.getMessage();
        this.path = ex.getPath();
    }

    public AnalyserExceptionResponse(Exception ex) {
        this.status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        this.error = HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();
        this.message = ex.getMessage();
    }

    public static ResponseEntity build(HttpStatus status, String msg, String path) {
        return new ResponseEntity(new AnalyserExceptionResponse(new AnalyserException(status, msg, path)), status);
    }

    public static ResponseEntity build(HttpStatus status, String msg, String path, Exception ex) {
        return new ResponseEntity(new AnalyserExceptionResponse(new AnalyserException(status, msg, ex, path)), status);
    }
}
