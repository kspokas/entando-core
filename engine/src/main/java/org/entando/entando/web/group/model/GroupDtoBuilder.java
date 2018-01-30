package org.entando.entando.web.group.model;

import com.agiletec.aps.system.services.group.Group;


/**
 * Unico punto di conversione tra l'oggetto del service layer con la relativa
 * rappresentazione REST
 *
 */
public class GroupDtoBuilder {
	private GroupDto item;

	public GroupDtoBuilder() {
		this.item = new GroupDto();
	}

	public GroupDtoBuilder(Group entity) {
		GroupDto dto = null;
		if (null != entity) {
			dto = new GroupDto();
			dto.setCode(entity.getName());
			dto.setName(entity.getDescription());
		}
		this.item = dto;
	}
	
	
	public GroupDto build() {
		return this.item;
	}	
}
