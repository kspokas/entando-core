package org.entando.entando.web.group.model;

import com.agiletec.aps.system.services.group.Group;

public class GroupDto {

	private String code;
	private String name;

	public GroupDto() {

	}

	public GroupDto(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public GroupDto(Group group) {
		this.setCode(group.getName());
		this.setName(group.getDescription());
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
