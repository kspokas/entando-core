package org.entando.entando.aps.system.services.group;

import java.util.List;

import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.group.model.GroupDto;
import org.entando.entando.web.group.model.GroupsDtoBuilder;
import org.entando.entando.web.model.common.RestListRequest;
import org.springframework.stereotype.Service;

@Service
public class GroupService implements IGroupService {

    private IGroupManager groupManager;

    public IGroupManager getGroupManager() {
        return groupManager;
    }

    public void setGroupManager(IGroupManager groupManager) {
        this.groupManager = groupManager;
    }


    @Override
    public PagedMetadata<GroupDto> getGroups(RestListRequest restListReq) {
        try {
            //XXX
            if (null == restListReq) {
                restListReq = new RestListRequest();
                restListReq.setPageNum(0);
                restListReq.setPageSize(5);
            }
            SearcherDaoPaginatedResult<Group> groups = this.getGroupManager().getGroups(restListReq.getFieldSearchFilters());
            PagedMetadata<GroupDto> pagedMetadata = new PagedMetadata<>(restListReq, groups);
            List<GroupDto> dtoList = new GroupsDtoBuilder(groups.getList()).build();
            pagedMetadata.setBody(dtoList);

            return pagedMetadata;
        } catch (Throwable t) {
            throw new RuntimeException("doh!", t);
        }
    }


    private void print(RestListRequest requestList) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String x = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestList);
            System.out.println(x);
            System.out.println("-----------------------------------------");
            String y = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestList.getFieldSearchFilters());
            System.out.println(y);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



}
