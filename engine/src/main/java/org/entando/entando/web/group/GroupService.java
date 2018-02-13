package org.entando.entando.web.group;

import java.util.List;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections4.ListUtils;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.group.model.GroupDto;
import org.entando.entando.web.group.model.GroupsDtoBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupService implements IGroupService {


    @Autowired
    private IGroupManager groupManager;


    @Override
    public PagedMetadata<GroupDto> getGroups(RestRequestRequestMetadata requestList) {
        try {
            //XXX
            if (null == requestList) {
                requestList = new RestRequestRequestMetadata();
                requestList.setPageNum(0);
                requestList.setPageSize(5);
            }
            List<Group> groups = this.getGroupManager().getGroups(requestList.getFieldSearchFilters());

            List<List<Group>> resd = ListUtils.partition(groups, requestList.getPageSize());
            PagedMetadata<GroupDto> pagedMetadata = new PagedMetadata<>(requestList.getPageNum(), groups.size(), resd.size());

            List<GroupDto> dtoList = new GroupsDtoBuilder(resd.get(requestList.getPageNum())).build();
            pagedMetadata.setBody(dtoList);

            return pagedMetadata;
        } catch (ApsSystemException e) {
            throw new RuntimeException("doh!");
        }
    }


    private void print(RestRequestRequestMetadata requestList) {
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


    public IGroupManager getGroupManager() {
        return groupManager;
    }


    public void setGroupManager(IGroupManager groupManager) {
        this.groupManager = groupManager;
    }

}
