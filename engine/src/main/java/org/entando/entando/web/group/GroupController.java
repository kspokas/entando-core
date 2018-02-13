package org.entando.entando.web.group;

import javax.validation.Valid;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.IGroupManager;
import org.entando.entando.web.common.exceptions.ValidationConflictException;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestResponse;
import org.entando.entando.web.group.model.GroupDto;
import org.entando.entando.web.group.model.GroupDtoBuilder;
import org.entando.entando.web.group.validator.GroupValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GroupController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
    private IGroupService groupService;

    @Autowired
    private IGroupManager groupManager;

	@Autowired
	private GroupValidator groupValidator;


    //@Permissions("read_")
    @RequestMapping(value = "/groups", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, name = "group_read")
    public ResponseEntity<?> getGroups(RestRequestRequestMetadata requestList) {

        PagedMetadata<GroupDto> result = getGroupService().getGroups(requestList);
        return new ResponseEntity<>(new RestResponse(result.getBody(), null, result), HttpStatus.OK);
	}


	@RequestMapping(value = "/group/{groupName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, name = "roleGroup")
	public ResponseEntity<?> getGroup(@PathVariable String groupName) {
		Group group = this.groupManager.getGroup(groupName);
		GroupDto dto = new GroupDtoBuilder(group).build();
		return new ResponseEntity<>(new RestResponse(dto), HttpStatus.OK);
	}

	//TODO validation 
	@RequestMapping(value = "/group/{groupName}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, name = "roleGroup")
	public ResponseEntity<?> updateGroup(@PathVariable String groupName, @Valid @RequestBody SimpleGroupRequest groupRequest) {
		Group group = this.groupManager.getGroup(groupName);
		GroupDto dto = new GroupDtoBuilder(group).build();
		return new ResponseEntity<>(new RestResponse(dto), HttpStatus.OK);
	}

	@RequestMapping(value = "/groups", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, name = "roleGroup")
	public ResponseEntity<?> addGroup(@Valid @RequestBody GroupRequest groupRequest, BindingResult bindingResult) throws ApsSystemException {
		
		//validazioni formali
		if (bindingResult.hasErrors()) {
			throw new ValidationGenericException(bindingResult);
		}
		
        //----
		//validazioni applicative
		groupValidator.validate(groupRequest, bindingResult);
		if (bindingResult.hasErrors()) {
			throw new ValidationConflictException(bindingResult);
		}

		Group group = this.createGroup(groupRequest);
		this.groupManager.addGroup(group);
		GroupDto dto = new GroupDtoBuilder(group).build();
		return new ResponseEntity<>(new RestResponse(dto), HttpStatus.OK);
	}


	@RequestMapping(value = "/groups/{groupName}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE, name = "roleGroup")
	public ResponseEntity<?> deleteGroup(@PathVariable String groupName) throws ApsSystemException {
		logger.info("deleting {}", groupName);
		Group group = this.groupManager.getGroup(groupName);
		this.groupManager.removeGroup(group);
		return new ResponseEntity<>(new RestResponse(groupName), HttpStatus.OK);
	}


	protected Group createGroup(GroupRequest groupRequest) {
		Group group = new Group();
		group.setName(groupRequest.getName());
		group.setDescription(groupRequest.getDescr());
		return group;
	}


    public IGroupService getGroupService() {
        return groupService;
    }


    public void setGroupService(IGroupService groupService) {
        this.groupService = groupService;
    }

}
