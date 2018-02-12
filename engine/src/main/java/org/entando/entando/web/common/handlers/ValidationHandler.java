package org.entando.entando.web.common.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.web.common.exceptions.EntandoAuthorizationException;
import org.entando.entando.web.common.exceptions.ValidationConflictException;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.entando.entando.web.common.model.RestError;
import org.entando.entando.web.common.model.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * intercetta globalmente le exception sollevate dai servizi rest e le traduce
 * in una {@link RestResponse} con status e payload customizzabile
 *
 */
//TODO RENAME
@ControllerAdvice
public class ValidationHandler {

    private static final Logger logger = LoggerFactory.getLogger(ValidationHandler.class);

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(value = EntandoAuthorizationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public RestResponse processEntandoAuthorizationException(EntandoAuthorizationException ex) {
        logger.debug("Handling {} error", ex.getClass().getSimpleName());
        RestResponse response = new RestResponse();
        RestError error = new RestError("101", this.resolveLocalizedErrorMessage("UNAUTHORIZED", new Object[]{ex.getUsername(), ex.getRequestURI(), ex.getMethod()}));
        List<RestError> errors = new ArrayList<>();
        errors.add(error);
        response.setErrors(errors);
        return response;
    }

    @ExceptionHandler(value = ValidationConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public RestResponse processValidationError(ValidationConflictException ex) {
        logger.debug("Handling ValidationException error");
        BindingResult result = ex.getBindingResult();
        RestResponse response = processAllErrors(result);
        return response;
    }

    @ExceptionHandler(value = ValidationGenericException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public RestResponse processValidationError(ValidationGenericException ex) {
        logger.debug("Handling ValidationException error");
        BindingResult result = ex.getBindingResult();
        RestResponse response = processAllErrors(result);
        return response;
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public RestResponse processValidationError(MethodArgumentNotValidException ex) {
        logger.debug("Handling MethodArgumentNotValidException error");
        BindingResult result = ex.getBindingResult();
        RestResponse response = processAllErrors(result);
        return response;
    }

    private RestResponse processAllErrors(BindingResult result) {
        return processAllErrors(result.getFieldErrors(), result.getGlobalErrors());
    }

    private RestResponse processAllErrors(List<FieldError> fieldErrors, List<ObjectError> objectErrors) {
        RestResponse dto = new RestResponse();
        processFieldErrors(dto, fieldErrors);
        processGlobalErrors(dto, objectErrors);
        return dto;
    }

    private RestResponse processFieldErrors(RestResponse dto, List<FieldError> fieldErrors) {
        if (null != fieldErrors) {
            List<RestError> errors = new ArrayList<>();
            for (FieldError fieldError : fieldErrors) {
                String localizedErrorMessage = resolveLocalizedErrorMessage(fieldError);
                errors.add(new RestError(null, localizedErrorMessage));
            }
            dto.addErrors(errors);
        }
        return dto;
    }

    private RestResponse processGlobalErrors(RestResponse dto, List<ObjectError> globalErrors) {
        if (null != globalErrors) {
            List<RestError> errors = new ArrayList<>();
            for (ObjectError globalError : globalErrors) {
                String localizedErrorMessage = resolveLocalizedErrorMessage(globalError);
                errors.add(new RestError(globalError.getCode(), localizedErrorMessage));
            }
            dto.addErrors(errors);
        }
        return dto;
    }

    /**
     * prova ad utilizzare il default message, altrimenti va sul default di
     * hibernate
     *
     * @param fieldError
     * @return
     */
    private String resolveLocalizedErrorMessage(DefaultMessageSourceResolvable fieldError) {
        Locale currentLocale = LocaleContextHolder.getLocale();
        String msgCode = StringUtils.isNotBlank(fieldError.getDefaultMessage()) ? fieldError.getDefaultMessage() : fieldError.getCode();
        String localizedErrorMessage = getMessageSource().getMessage(msgCode, fieldError.getArguments(), currentLocale);
        return localizedErrorMessage;
    }

    private String resolveLocalizedErrorMessage(String code, Object[] args) {
        Locale currentLocale = LocaleContextHolder.getLocale();
        String localizedErrorMessage = getMessageSource().getMessage(code, args, currentLocale);
        return localizedErrorMessage;
    }

    public MessageSource getMessageSource() {
        return messageSource;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
}
