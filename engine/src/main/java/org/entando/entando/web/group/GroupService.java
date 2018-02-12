package org.entando.entando.web.group;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.aps.system.services.page.PageMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupService implements IGroupService {

    @Autowired
    private IGroupManager groupManager;


    public PageMetadata getGroups(FieldSearchFilter fiters) {
        return null;
    }
}
