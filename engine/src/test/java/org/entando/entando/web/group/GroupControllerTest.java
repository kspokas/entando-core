package org.entando.entando.web.group;

import com.agiletec.aps.system.services.authorization.Authorization;
import com.agiletec.aps.system.services.authorization.AuthorizationManager;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
import com.agiletec.aps.system.services.role.Role;
import com.agiletec.aps.system.services.user.User;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.system.services.user.UserManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Method;
import java.util.Calendar;
import org.entando.entando.aps.system.services.oauth2.ApiOAuth2TokenManager;
import org.entando.entando.aps.system.services.oauth2.model.OAuth2Token;
import org.entando.entando.web.common.handlers.ValidationHandler;
import org.entando.entando.web.common.interceptor.EntandoOauth2Interceptor;
import org.entando.entando.web.group.validator.GroupValidator;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.ExceptionHandlerMethodResolver;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;

@RunWith(SpringJUnit4ClassRunner.class)
// @formatter:off

@ContextConfiguration(locations = {
    "classpath*:spring/mock-applicationContext.xml",
    "classpath*:spring/web/servlet-context.xml",})

// @formatter:on
@WebAppConfiguration()
@ActiveProfiles("test")
public class GroupControllerTest {

    private MockMvc mockMvc;

    @Mock
    private GroupManager groupManager;

    @Mock
    private UserManager userManager;

    @Mock
    private ApiOAuth2TokenManager OAuth2TokenManager;

    @Mock
    private AuthorizationManager authManager;

    @Autowired
    @InjectMocks
    private GroupController groupController;

    @InjectMocks
    private EntandoOauth2Interceptor interceptor;

    @InjectMocks
    private GroupValidator validator;

    @Autowired
    @InjectMocks
    private ValidationHandler handler;

    private List<Group> mockGroups = new ArrayList<>();

    private UserDetails mockUser;

    private OAuth2Token mockToken;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
//        mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        mockMvc = MockMvcBuilders.standaloneSetup(groupController).addInterceptors(interceptor)
                .setValidator(validator).setHandlerExceptionResolvers(createExceptionResolver()).build();
        createMockGroups();
        createMockUser();
        createToken();
    }

    @Rollback
    @After
    public void tearDown() {
        // tear down after every test method
    }

    @Test
    public void should_load_the_list_of_groups() throws Exception {
        String username = "testUser", password = "testTest";
        when(groupManager.getGroups()).thenReturn(mockGroups);
        prepareAuth(username, password);
        ResultActions result = mockMvc.perform(get("/groups").header("Authorization", "Bearer " + getAccessToken(username, password)));
        result.andExpect(status().isOk());
        String response = result.andReturn().getResponse().getContentAsString();
        System.out.println(response);
        result.andExpect(jsonPath("$.payload", hasSize(3)));
    }

    @Test
    public void should_update_group() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        when(groupManager.getGroup(anyString())).thenReturn(mockGroups.get(0));
        doNothing().when(groupManager).updateGroup((Group) anyObject());

        GroupRequest request = new GroupRequest(null, "hello");
        String json = objectMapper.writeValueAsString(request);
        String username = "testUser", password = "testTest";
        prepareAuth(username, password);
        ResultActions result = mockMvc
                .perform(put("/group/{groupName}", "GROUP_0").header("Authorization", "Bearer " + getAccessToken(username, password))
                        .content(json).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

        String response = result.andReturn().getResponse().getContentAsString();
        System.out.println(response);
        result.andExpect(status().isOk());
    }

    @Test
    public void should_add_group() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        when(groupManager.getGroup(anyString())).thenReturn(mockGroups.get(0));
        doNothing().when(groupManager).updateGroup((Group) anyObject());

        GroupRequest request = new GroupRequest("GRUPPO_LOL", "lol");
        String json = objectMapper.writeValueAsString(request);
        String username = "testUser", password = "testTest";
        prepareAuth(username, password);
        ResultActions result = null;
        try {
            result = mockMvc.perform(post("/groups").header("Authorization", "Bearer " + getAccessToken(username, password))
                    .content(json).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
            String response = result.andReturn().getResponse().getContentAsString();
            System.out.println(response);
            result.andExpect(status().isOk());
        } catch (Exception e) {
            String response = result.andReturn().getResponse().getContentAsString();
            System.out.println(response);
            result.andExpect(status().isOk());
        }
    }

    @Test
    public void should_validate_add_group_empty_fields() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        when(groupManager.getGroup(anyString())).thenReturn(mockGroups.get(0));
        doNothing().when(groupManager).updateGroup((Group) anyObject());

        GroupRequest request = new GroupRequest(null, null);
        String json = objectMapper.writeValueAsString(request);
        String username = "testUser", password = "testTest";
        prepareAuth(username, password);
        ResultActions result = mockMvc.perform(post("/groups").header("Authorization", "Bearer " + getAccessToken(username, password))
                .content(json).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
        String response = result.andReturn().getResponse().getContentAsString();
        System.out.println(response);
        result.andExpect(status().isBadRequest());

        // @formatter:off
        result
                .andExpect(jsonPath("$.errors", hasSize(2)))
                .andExpect(jsonPath("$.errors[*].message", containsInAnyOrder(
                        "Description is required",
                        "Name is required"
                )));
        // @formatter:on

    }

    @Test
    public void should_validate_add_group_existing_code() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        when(groupManager.getGroup(anyString())).thenReturn(mockGroups.get(0));

        GroupRequest request = new GroupRequest(mockGroups.get(0).getName(), mockGroups.get(0).getDescription());
        String json = objectMapper.writeValueAsString(request);
        String username = "testUser", password = "testTest";
        prepareAuth(username, password);
        ResultActions result = mockMvc.perform(post("/groups").header("Authorization", "Bearer " + getAccessToken(username, password))
                .content(json).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
        String response = result.andReturn().getResponse().getContentAsString();
        System.out.println(response);
        result.andExpect(status().isConflict());
    }

    @Test
    public void should_delete_group() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        when(groupManager.getGroup(anyString())).thenReturn(mockGroups.get(2));
        doNothing().when(groupManager).updateGroup((Group) anyObject());
        String username = "testUser", password = "testTest";
        prepareAuth(username, password);
        ResultActions result = mockMvc
                .perform(delete("/groups/{groupName}", "GROUP_0").header("Authorization", "Bearer " + getAccessToken(username, password)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
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

    private void prepareAuth(String username, String password) throws Exception {
        when(userManager.getUser(username, password)).thenReturn(mockGetUser(username, password));
        when(userManager.getUser(username)).thenReturn(mockGetUser(username, password));
        when(OAuth2TokenManager.getApiOAuth2Token(getAccessToken(username, password))).thenReturn(mockToken);
        when(authManager.getUserAuthorizations(username)).thenReturn(mockUser.getAuthorizations());
        when(authManager.isAuthOnPermission(mockUser, "roleGroup")).thenReturn(true);
    }

    private String getAccessToken(String username, String password) throws Exception {
        if (mockGetUser(username, password) != null) {
            return "valid_token";
        }
        return "wrong_token";
    }

    private void createMockUser() {
        User rawUser = new User();
        rawUser.setUsername("testUser");
        rawUser.setPassword("testTest");
        Group group = new Group();
        group.setDescription("test group");
        group.setName("groupCode");
        Role role = new Role();
        role.addPermission("roleGroup");
        role.setDescription("managing groups");
        role.setName("manageGroup");
        Authorization auth = new Authorization(group, role);
        rawUser.addAuthorization(auth);
        mockUser = rawUser;
    }

    private UserDetails mockGetUser(String username, String password) {
        if (username.equals(mockUser.getUsername()) && password.equals(mockUser.getPassword())) {
            return mockUser;
        }
        return null;
    }

    private UserDetails mockGetUser(String username) {
        if (username.equals(mockUser.getUsername())) {
            return mockUser;
        }
        return null;
    }

    private void createToken() {
        OAuth2Token oAuth2Token = new OAuth2Token();
        oAuth2Token.setAccessToken("valid_token");
        oAuth2Token.setRefreshToken("refresh_token");
        oAuth2Token.setClientId("testUser");
        Calendar calendar = Calendar.getInstance(); // gets a calendar using the default time zone and locale.
        calendar.add(Calendar.SECOND, 3600);
        oAuth2Token.setExpiresIn(calendar.getTime());
        oAuth2Token.setGrantType("password");
        mockToken = oAuth2Token;
    }

    private ExceptionHandlerExceptionResolver createExceptionResolver() {
        ExceptionHandlerExceptionResolver exceptionResolver = new ExceptionHandlerExceptionResolver() {
            protected ServletInvocableHandlerMethod getExceptionHandlerMethod(HandlerMethod handlerMethod, Exception exception) {
                Method method = new ExceptionHandlerMethodResolver(ValidationHandler.class).resolveMethod(exception);
                return new ServletInvocableHandlerMethod(handler, method);
            }
        };
        exceptionResolver.afterPropertiesSet();
        return exceptionResolver;
    }
}
