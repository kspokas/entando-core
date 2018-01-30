package org.entando.entando.web.common.handlers.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
public class RestObjectError {

	private String message;
	private String objectName;

	public RestObjectError() {

	}

	public RestObjectError(String objectName, String message) {
		this.message = message;
		this.objectName = objectName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	@Override
	public String toString() {
		return "RestObjectError [message=" + message + ", objectName=" + objectName + "]";
	}

}
