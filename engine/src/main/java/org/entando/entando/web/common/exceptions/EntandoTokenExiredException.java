package org.entando.entando.web.common.exceptions;

import javax.servlet.http.HttpServletRequest;

public class EntandoTokenExiredException extends EntandoAuthorizationException {

    public EntandoTokenExiredException(String message, HttpServletRequest request, String username) {
        super(message, request, username);
        // TODO Auto-generated constructor stub
    }

}
