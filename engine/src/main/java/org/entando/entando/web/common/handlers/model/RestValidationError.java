package org.entando.entando.web.common.handlers.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Response in caso di validazione
 * 
 * @author spuddu
 *
 */
public class RestValidationError {

	private List<RestObjectError> globalErrors = new ArrayList<RestObjectError>();
	private List<RestFieldError> fieldErrors = new ArrayList<RestFieldError>();

	public RestValidationError() {

	}

	public void addFieldError(String objectName, String path, String message) {
		RestFieldError error = new RestFieldError(objectName, path, message);
		fieldErrors.add(error);
	}

	public void addGlobalError(String objectName, String message) {
		RestObjectError error = new RestObjectError(objectName, message);
		globalErrors.add(error);
	}

	public List<RestFieldError> getFieldErrors() {
		return fieldErrors;
	}

	public List<RestObjectError> getGlobalErrors() {
		return globalErrors;
	}
}
