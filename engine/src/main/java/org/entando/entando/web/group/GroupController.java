package org.entando.entando.web.group;

import javax.validation.Valid;

import com.agiletec.aps.system.exception.ApsSystemException;
import org.entando.entando.aps.system.services.group.IGroupService;
import org.entando.entando.aps.system.services.group.model.GroupDto;
import org.entando.entando.web.common.exceptions.ValidationConflictException;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestResponse;
import org.entando.entando.web.group.validator.GroupValidator;
import org.entando.entando.web.model.common.RestListRequest;
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
    private GroupValidator groupValidator;

    public IGroupService getGroupService() {
        return groupService;
    }

    public void setGroupService(IGroupService groupService) {
        this.groupService = groupService;
    }


    //@Permissions("read_")
    @RequestMapping(value = "/groups", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, name = "group_read")
    public ResponseEntity<?> getGroups(RestListRequest requestList) {
        PagedMetadata<GroupDto> result = this.getGroupService().getGroups(requestList);
        return new ResponseEntity<>(new RestResponse(result.getBody(), null, result), HttpStatus.OK);
	}


	@RequestMapping(value = "/group/{groupName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, name = "roleGroup")
	public ResponseEntity<?> getGroup(@PathVariable String groupName) {
        GroupDto group = this.getGroupService().getGroup(groupName);
        return new ResponseEntity<>(new RestResponse(group), HttpStatus.OK);
	}

	//TODO validation 
	@RequestMapping(value = "/group/{groupName}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, name = "roleGroup")
	public ResponseEntity<?> updateGroup(@PathVariable String groupName, @Valid @RequestBody SimpleGroupRequest groupRequest) {
        GroupDto group = this.getGroupService().updateGroup(groupName, groupRequest.getDescr());
        return new ResponseEntity<>(new RestResponse(group), HttpStatus.OK);
	}

	@RequestMapping(value = "/groups", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, name = "roleGroup")
	public ResponseEntity<?> addGroup(@Valid @RequestBody GroupRequest groupRequest, BindingResult bindingResult) throws ApsSystemException {
        //field validations
		if (bindingResult.hasErrors()) {
			throw new ValidationGenericException(bindingResult);
		}
        //business validations 
		groupValidator.validate(groupRequest, bindingResult);
		if (bindingResult.hasErrors()) {
			throw new ValidationConflictException(bindingResult);
		}
        GroupDto dto = this.getGroupService().addGroup(groupRequest);
		return new ResponseEntity<>(new RestResponse(dto), HttpStatus.OK);
	}


	@RequestMapping(value = "/groups/{groupName}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE, name = "roleGroup")
	public ResponseEntity<?> deleteGroup(@PathVariable String groupName) throws ApsSystemException {
		logger.info("deleting {}", groupName);
        this.getGroupService().removeGroup(groupName);
		return new ResponseEntity<>(new RestResponse(groupName), HttpStatus.OK);
	}



}
