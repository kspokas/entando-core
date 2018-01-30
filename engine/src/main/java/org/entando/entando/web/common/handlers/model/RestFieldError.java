package org.entando.entando.web.common.handlers.model;

public class RestFieldError extends RestObjectError {

	private String field;

	public RestFieldError() {
		//
	}

	public RestFieldError(String objectName, String field, String message) {
		super(objectName, message);
		this.setField(field);

	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	@Override
	public String toString() {
		return "RestFieldError [field=" + field + ", message=" + getMessage() + ", objectName=" + getObjectName() + "]";
	}

}
