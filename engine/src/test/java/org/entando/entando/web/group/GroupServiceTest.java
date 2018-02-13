package org.entando.entando.web.group;

import java.util.ArrayList;
import java.util.List;

import com.agiletec.aps.system.services.group.Group;
import org.entando.entando.web.common.model.Pageable;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

public class GroupServiceTest {

    @InjectMocks
    private GroupService groupService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testX() {
        RestRequestRequestMetadata requestList = new RestRequestRequestMetadata();

        Filter f0 = new Filter();
        f0.setAttribute("name");
        f0.setValue("gro");
        Filter[] filters = new Filter[1];
        filters[0] = f0;

        requestList.setFilter(filters);
        this.groupService.getGroups(requestList);

    }

    @Test
    public void testY() {
        List<Group> list = this.createFakeGroups();
        Pageable<Group> pp = new Pageable<>(list, 0, 10);

        System.out.println(pp.getListForPage());

    }

    private List<Group> createFakeGroups() {
        List<Group> groups = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            groups.add(this.createGroup("G_" + i, "descr_" + i));
        }
        return groups;
    }

    private Group createGroup(String string, String string2) {
        Group group = new Group();
        group.setName(string);
        group.setDescription(string2);
        return group;
    }

}
