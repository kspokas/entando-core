package org.entando.entando.aps.system.services.group;

import java.util.List;

import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.IGroupManager;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.group.model.GroupDto;
import org.entando.entando.web.group.model.GroupsDtoBuilder;
import org.entando.entando.web.model.common.RestListRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GroupService implements IGroupService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private IGroupManager groupManager;

    protected IGroupManager getGroupManager() {
        return groupManager;
    }

    public void setGroupManager(IGroupManager groupManager) {
        this.groupManager = groupManager;
    }


    @Override
    public PagedMetadata<GroupDto> getGroups(RestListRequest restListReq) {
        try {
            SearcherDaoPaginatedResult<Group> groups = this.getGroupManager().getGroups(restListReq.getFieldSearchFilters());
            PagedMetadata<GroupDto> pagedMetadata = new PagedMetadata<>(restListReq, groups);
            List<GroupDto> dtoList = new GroupsDtoBuilder(groups.getList()).build();
            pagedMetadata.setBody(dtoList);

            return pagedMetadata;
        } catch (Throwable t) {
            throw new RuntimeException("doh!", t);
        }
    }

}
