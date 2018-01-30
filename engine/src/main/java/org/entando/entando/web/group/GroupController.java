package org.entando.entando.web.group;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.entando.entando.web.common.PageMetadata;
import org.entando.entando.web.common.RestResponse;
import org.entando.entando.web.group.model.GroupDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.GroupManager;

@RestController
public class GroupController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private GroupManager groupManager;

	@RequestMapping(value = "/groups", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getGroups() {
		List<Group> groups = groupManager.getGroups();
		List<GroupDto> dtoList = this.buildList(groups);

		return new ResponseEntity<>(new RestResponse(dtoList, null, new PageMetadata(1, 3, 3)), HttpStatus.OK);
	}


	//TODO manca dto
	@RequestMapping(value = "/group/{groupName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getGroup(@PathVariable String groupName) {
		Group group = this.groupManager.getGroup(groupName);
		GroupDto dto = new GroupDto(group);
		return new ResponseEntity<>(new RestResponse(dto), HttpStatus.OK);
	}

	//TODO validation 
	@RequestMapping(value = "/group/{groupName}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateGroup(@PathVariable String groupName, @RequestBody GroupRequest groupRequest) {
		Group group = this.groupManager.getGroup(groupName);
		GroupDto dto = new GroupDto(group);
		return new ResponseEntity<>(new RestResponse(dto), HttpStatus.OK);
	}

	//TODO validation 
	@RequestMapping(value = "/groups", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> addGroup(@Valid @RequestBody GroupRequest groupRequest) throws Throwable {
		///is valid?
		//esiste gia....
		Group group = this.createGroup(groupRequest);
		try {
			this.groupManager.addGroup(group);
		} catch (ApsSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		GroupDto dto = new GroupDto(group);
		return new ResponseEntity<>(new RestResponse(dto), HttpStatus.OK);
	}

	//TODO validation 
	@RequestMapping(value = "/groups/{groupName}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deleteGroup(@PathVariable String groupName) throws ApsSystemException {

		logger.info("deleting {}", groupName);
		Group group = this.groupManager.getGroup(groupName);
		this.groupManager.removeGroup(group);
		return new ResponseEntity<>(new RestResponse(groupName), HttpStatus.OK);
	}


	private List<GroupDto> buildList(List<Group> groups) {
		List<GroupDto> list = new ArrayList<>();
		for (Group group : groups) {
			list.add(new GroupDto(group));
		}
		return list;
	}

	protected Group createGroup(GroupRequest groupRequest) {
		Group group = new Group();
		group.setName(groupRequest.getName());
		group.setDescription(groupRequest.getDescr());
		return group;
	}



}
