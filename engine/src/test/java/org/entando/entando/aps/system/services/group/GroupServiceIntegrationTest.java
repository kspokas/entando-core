package org.entando.entando.aps.system.services.group;

import com.agiletec.aps.BaseTestCase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.entando.entando.aps.system.services.group.model.GroupDto;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.model.common.Filter;
import org.entando.entando.web.model.common.RestListRequest;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class GroupServiceIntegrationTest extends BaseTestCase {

    private IGroupService groupService;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }

    private void init() throws Exception {
        try {
            groupService = (IGroupService) this.getApplicationContext().getBean(IGroupService.BEAN_NAME);
        } catch (Exception e) {
            throw e;
        }
    }


    @Test
    public void testGetGroups() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        RestListRequest restListRequest = new RestListRequest();
        PagedMetadata<GroupDto> res = this.groupService.getGroups(restListRequest);
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(res);
        System.out.println(json);
        assertThat(res.getPage(), is(0));
        assertThat(res.getSize(), is(5));
        assertThat(res.getLast(), is(1));
        assertThat(res.getCount(), is(6));

        //
        restListRequest.setPageSize(2);
        res = this.groupService.getGroups(restListRequest);
        json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(res);
        System.out.println(json);
        assertThat(res.getPage(), is(0));
        assertThat(res.getSize(), is(2));
        assertThat(res.getLast(), is(2));
        assertThat(res.getCount(), is(6));

        //
        restListRequest.setPageSize(4);
        res = this.groupService.getGroups(restListRequest);
        json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(res);
        System.out.println(json);
        assertThat(res.getPage(), is(0));
        assertThat(res.getSize(), is(4));
        assertThat(res.getLast(), is(1));
        assertThat(res.getCount(), is(6));

        //
        restListRequest.setPageSize(4);
        restListRequest.setPageNum(1);
        res = this.groupService.getGroups(restListRequest);
        json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(res);
        System.out.println(json);
        assertThat(res.getPage(), is(1));
        assertThat(res.getSize(), is(2));
        assertThat(res.getLast(), is(1));
        assertThat(res.getCount(), is(6));
        //
        restListRequest.setPageSize(4);
        restListRequest.setPageNum(1000);
        res = this.groupService.getGroups(restListRequest);
        json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(res);
        System.out.println(json);
        assertThat(res.getPage(), is(1000));
        assertThat(res.getSize(), is(0));
        assertThat(res.getLast(), is(1));
        assertThat(res.getCount(), is(6));
    }

    @Test
    public void testGetGroups_filter() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        RestListRequest restListRequest = new RestListRequest();
        restListRequest.addFilter(new Filter("groupname", "fr"));

        PagedMetadata<GroupDto> res = this.groupService.getGroups(restListRequest);
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(res);
        System.out.println(json);
        assertThat(res.getPage(), is(0));
        assertThat(res.getSize(), is(1));
        assertThat(res.getLast(), is(0));
        assertThat(res.getCount(), is(1));
    }



}
