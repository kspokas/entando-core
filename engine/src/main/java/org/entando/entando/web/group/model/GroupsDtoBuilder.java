package org.entando.entando.web.group.model;

import java.util.ArrayList;
import java.util.List;

import com.agiletec.aps.system.services.group.Group;


/**
 * Unico punto di conversione tra l'oggetto del service layer con la relativa
 * rappresentazione REST
 *
 */
public class GroupsDtoBuilder {

	private List<GroupDto> list;

	public GroupsDtoBuilder() {
		this.list = new ArrayList<>();
	}

	public GroupsDtoBuilder(List<Group> entityList) {
		this.list = new ArrayList<>();
		if (null != entityList) {
			for (Group entity : entityList) {
				list.add(new GroupDtoBuilder(entity).build());
			}
		}
	}

	public List<GroupDto> build() {
		return this.list;
	}	
}
