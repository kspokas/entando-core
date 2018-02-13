package org.entando.entando.web.group;

import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.group.model.GroupDto;

public interface IGroupService {


    PagedMetadata<GroupDto> getGroups(RestRequestRequestMetadata requestList);
}
