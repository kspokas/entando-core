package org.entando.entando.web.group.model;

import org.entando.entando.aps.system.services.group.model.GroupDto;
import org.entando.entando.web.model.common.Filter;

public class GroupFilter extends Filter {

    @Override
    public String getAttributeName() {
        return GroupDto.getEntityFieldName(this.getAttribute());
    }

}

