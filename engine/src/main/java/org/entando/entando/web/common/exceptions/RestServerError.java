package org.entando.entando.web.common.exceptions;


public class RestServerError extends RuntimeException {

    private Exception ex;

    public RestServerError(String message, Exception ex) {
        super(message);
        this.ex = ex;
    }
}
