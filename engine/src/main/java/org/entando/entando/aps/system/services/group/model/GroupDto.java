package org.entando.entando.aps.system.services.group.model;

import com.agiletec.aps.system.services.group.Group;

/**
 * Rappresentazione REST dell'oggetto GROUP
 *
 * 
 */
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

    //TODO FIX MAGIC STRINGS
    public static String getEntityFieldName(String dtoFieldName) {
        if (dtoFieldName.equals("code")) {
            return "groupname";
        } else if (dtoFieldName.equals("name")) {
            return "descr";
        } else {
            return dtoFieldName;
        }
    }

}

