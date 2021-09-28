package fr.volkaert.dependency_analyser.error;

import org.springframework.http.HttpStatus;

import java.time.Instant;

public class AnalyserException extends  RuntimeException {

    private Instant timestamp = Instant.now();
    public Instant getTimestamp() {
        return timestamp;
    }

    private HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public int getHttpStatusCode() {
        return httpStatus != null ? httpStatus.value() : HttpStatus.INTERNAL_SERVER_ERROR.value();
    }
    public String getHttpStatusMessage() {
        return httpStatus != null ? httpStatus.getReasonPhrase() : HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();
    }

    private String path;
    public String getPath() {
        return path;
    }

    public AnalyserException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public AnalyserException(HttpStatus httpStatus, String message, String path) {
        super(message);
        this.httpStatus = httpStatus;
        this.path = path;
    }

    public AnalyserException(HttpStatus httpStatus, String message, Throwable cause) {
        super(message + (cause != null ? ". Inner cause is '" + cause.getMessage() + "'" : ""), cause);
        this.httpStatus = httpStatus;
    }

    public AnalyserException(HttpStatus httpStatus, String message, Throwable cause, String path) {
        super(message + (cause != null ? ". Inner cause is '" + cause.getMessage() + "'" : ""), cause);
        this.httpStatus = httpStatus;
        this.path = path;
    }

    public AnalyserException(HttpStatus httpStatus, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message + (cause != null ? ". Inner cause is '" + cause.getMessage() + "'" : ""), cause, enableSuppression, writableStackTrace);
        this.httpStatus = httpStatus;
    }

    public AnalyserException(HttpStatus httpStatus, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String path) {
        super(message + (cause != null ? ". Inner cause is '" + cause.getMessage() + "'" : ""), cause, enableSuppression, writableStackTrace);
        this.httpStatus = httpStatus;
        this.path = path;
    }
}
