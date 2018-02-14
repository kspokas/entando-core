package org.entando.entando.aps.system.services.group;


import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.group.model.GroupDto;
import org.entando.entando.web.model.common.RestListRequest;

public interface IGroupService {

    PagedMetadata<GroupDto> getGroups(RestListRequest restRequest);
}
