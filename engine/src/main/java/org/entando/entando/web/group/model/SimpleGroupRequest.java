package org.entando.entando.web.group.model;

import javax.validation.constraints.NotNull;

/**
 * rappresenta il payload per l'update (put)
 *
 */
public class SimpleGroupRequest {


	@NotNull(message = "NotBlank.group.descr")
	private String descr;

	public SimpleGroupRequest() {

	}

	public SimpleGroupRequest(String descr) {
		this.descr = descr;
	}


	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

}
