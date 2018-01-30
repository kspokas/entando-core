package org.entando.entando.web.common;

public class RestResponse {

	private Object payload;
	private Object errors;
	private Object metadata;

	public RestResponse(Object payload) {
		this.payload = payload;
	}

	public RestResponse(Object payload, Object errors, Object metadata) {
		this.payload = payload;
		this.errors = errors;
		this.metadata = metadata;
	}

	public Object getPayload() {
		return payload;
	}

	public void setPayload(Object payload) {
		this.payload = payload;
	}
	public Object getErrors() {
		return errors;
	}
	public void setErrors(Object errors) {
		this.errors = errors;
	}
	public Object getMetadata() {
		return metadata;
	}
	public void setMetadata(Object metadata) {
		this.metadata = metadata;
	}


}
