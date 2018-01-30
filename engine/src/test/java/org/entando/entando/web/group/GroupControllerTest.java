package org.entando.entando.web.group;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.GroupManager;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
// @formatter:off
 

@ContextConfiguration(locations = { 
		"classpath*:spring/mock-applicationContext.xml", 
		"classpath*:spring/web/servlet-context.xml", 
		
})

// @formatter:on
@WebAppConfiguration()
@ActiveProfiles("test")
public class GroupControllerTest {

	private MockMvc mockMvc;

	@Mock
	private GroupManager groupManager;

	@InjectMocks
	private GroupController groupController;


	private List<Group> mockGroups = new ArrayList<>();

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
		//mockMvc = MockMvcBuilders.standaloneSetup(groupController).build();
		createMockGroups();
	}


	@Rollback
	@After
	public void tearDown() {
		// tear down after every test method
	}

	@Test
	public void should_load_the_list_of_groups() throws Exception {
		when(groupManager.getGroups()).thenReturn(mockGroups);
		ResultActions result = mockMvc.perform(get("/groups"));
		result.andExpect(status().isOk());
		String response = result.andReturn().getResponse().getContentAsString();
		System.out.println(response);
	}

	@Test
	public void should_update_group() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		when(groupManager.getGroup(anyString())).thenReturn(mockGroups.get(0));
		doNothing().when(groupManager).updateGroup((Group) anyObject());

		GroupRequest request = new GroupRequest(null, "hello");
		String json = objectMapper.writeValueAsString(request);

		ResultActions result = mockMvc
				.perform(put("/group/{groupName}", "GROUP_0").content(json).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isOk());
		String response = result.andReturn().getResponse().getContentAsString();
		System.out.println(response);
	}

	@Test
	public void should_add_group() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		when(groupManager.getGroup(anyString())).thenReturn(mockGroups.get(0));
		doNothing().when(groupManager).updateGroup((Group) anyObject());

		GroupRequest request = new GroupRequest("GRUPPO_LOL", "lol");
		String json = objectMapper.writeValueAsString(request);
		ResultActions result = mockMvc.perform(post("/groups").content(json).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isOk());
		String response = result.andReturn().getResponse().getContentAsString();
		System.out.println(response);
	}

	@Test
	public void should_validate_add_group() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		when(groupManager.getGroup(anyString())).thenReturn(mockGroups.get(0));
		doNothing().when(groupManager).updateGroup((Group) anyObject());

		GroupRequest request = new GroupRequest(null, null);
		String json = objectMapper.writeValueAsString(request);
		ResultActions result = mockMvc.perform(post("/groups").content(json).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
		String response = result.andReturn().getResponse().getContentAsString();
		System.out.println(response);
		result.andExpect(status().isOk());
	}


	@Test
	public void should_delete_group() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		when(groupManager.getGroup(anyString())).thenReturn(mockGroups.get(0));
		doNothing().when(groupManager).updateGroup((Group) anyObject());

		ResultActions result = mockMvc
				.perform(delete("/groups/{groupName}", "GROUP_0").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isOk());
		String response = result.andReturn().getResponse().getContentAsString();
		System.out.println(response);
	}



	private void createMockGroups() {
		for (int i = 0; i < 3; i++) {
			Group group = new Group();
			group.setName("GROUP_" + i);
			group.setDescription("group " + i);
			mockGroups.add(group);
		}
	}


}
