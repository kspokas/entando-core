/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entando.entando.web.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.user.IAuthenticationProviderManager;
import com.agiletec.aps.system.services.user.UserDetails;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.ParameterStyle;
import org.apache.oltu.oauth2.rs.request.OAuthAccessResourceRequest;
import org.entando.entando.aps.system.services.api.IApiErrorCodes;
import org.entando.entando.aps.system.services.api.model.ApiException;
import org.entando.entando.aps.system.services.oauth2.IApiOAuth2TokenManager;
import org.entando.entando.aps.system.services.oauth2.model.OAuth2Token;
import org.entando.entando.web.common.exceptions.EntandoAuthorizationException;
import org.entando.entando.web.common.exceptions.EntandoTokenExiredException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 *
 * @author paddeo
 */
public class EntandoOauth2Interceptor extends HandlerInterceptorAdapter {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IApiOAuth2TokenManager oAuth2TokenManager;

    @Autowired
    private IAuthenticationProviderManager authenticationProviderManager;

    @Autowired
    private IAuthorizationManager authorizationManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;
            if (method.getMethod().isAnnotationPresent(RequestMapping.class)) {
                RequestMapping rqm = method.getMethodAnnotation(RequestMapping.class);
                String permission = rqm.name();
                this.extractOAuthParameters(request, permission);
            }
        }
        return true;
    }

    protected void extractOAuthParameters(HttpServletRequest request, String permission) throws ApiException {
        try {
            logger.info("Permission required: {}", permission);
            OAuthAccessResourceRequest requestMessage = new OAuthAccessResourceRequest(request, ParameterStyle.HEADER);

            // Get the access token
            String accessToken = requestMessage.getAccessToken();
            final OAuth2Token token = oAuth2TokenManager.getApiOAuth2Token(accessToken);
            if (token != null) {
                if (!token.getAccessToken().equals(accessToken)) {
                    throw new ApiException(IApiErrorCodes.API_AUTHENTICATION_REQUIRED, "Token does not match", Response.Status.UNAUTHORIZED);
                } // check if access token is expired
                else if (token.getExpiresIn().getTime() < System.currentTimeMillis()) {
                    throw new EntandoTokenExiredException(null, request, null);

                }
                String username = token.getClientId();
                UserDetails user = authenticationProviderManager.getUser(username);
                if (user != null) {
                    logger.info("User {} requesting resource that requires {} permission ", username, permission);
                    if (permission != null) {
                        if (!authorizationManager.isAuthOnPermission(user, permission)) {
                            throw new EntandoAuthorizationException(null, request, username);
                        }
                    }
                } else {
                    logger.info("User {} not found ", username);
                }
            } else {
                if (accessToken != null) {
                    throw new ApiException(IApiErrorCodes.API_AUTHENTICATION_REQUIRED, "Token not found, request new one", Response.Status.UNAUTHORIZED);
                }
                throw new ApiException(IApiErrorCodes.API_AUTHENTICATION_REQUIRED, "Authentication Required", Response.Status.UNAUTHORIZED);
            }
        } catch (OAuthSystemException | ApsSystemException | OAuthProblemException ex) {
            logger.error("System exception {}", ex.getMessage());
            throw new ApiException(IApiErrorCodes.SERVER_ERROR, ex.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

}
