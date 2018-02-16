package org.entando.entando.web.group;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.authorization.AuthorizationManager;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.user.IAuthenticationProviderManager;
import com.agiletec.aps.system.services.user.UserDetails;
import org.entando.entando.aps.system.services.group.GroupService;
import org.entando.entando.aps.system.services.group.model.GroupDto;
import org.entando.entando.aps.system.services.oauth2.IApiOAuth2TokenManager;
import org.entando.entando.web.AbstractControllerTest;
import org.entando.entando.web.common.interceptor.EntandoOauth2Interceptor;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.model.common.RestListRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class GroupControllerTest extends AbstractControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IApiOAuth2TokenManager apiOAuth2TokenManager;

    @Mock
    private IAuthenticationProviderManager authenticationProviderManager;

    @Mock
    private IAuthorizationManager authorizationManager;

    @Mock
    private GroupService groupService;

    @InjectMocks
    private GroupController controller;

    @InjectMocks
    private EntandoOauth2Interceptor entandoOauth2Interceptor;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                                 .addInterceptors(entandoOauth2Interceptor)
                                 .setHandlerExceptionResolvers(createHandlerExceptionResolver())
                                 .build();
    }

    @Test
    public void should_load_the_list_of_groups() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .grantedToManageRoles(Group.FREE_GROUP_NAME)
                .build();
        String accessToken = mockOAuthInterceptor(user);

        String mockJsonResult = "{\n" +
                "  \"page\" : 1,\n" +
                "  \"size\" : 2,\n" +
                "  \"last\" : 1,\n" +
                "  \"count\" : 6,\n" +
                "  \"body\" : [ {\n" +
                "    \"code\" : \"helpdesk\",\n" +
                "    \"name\" : \"Helpdesk\"\n" +
                "  }, {\n" +
                "    \"code\" : \"management\",\n" +
                "    \"name\" : \"Management\"\n" +
                "  } ]\n" +
                "}";
        PagedMetadata<GroupDto> mockResult = (PagedMetadata<GroupDto>) this.createPagedMetadata(mockJsonResult);
        when(groupService.getGroups(any(RestListRequest.class))).thenReturn(mockResult);

        // @formatter:off
        ResultActions result = mockMvc.perform(
                                               get("/groups")
                                               .param("pageNum", "1")
                                               .param("pageSize", "4")
                                               .header("Authorization", "Bearer " + accessToken)
                );
        // @formatter:on
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.payload", hasSize(2)));
        result.andExpect(jsonPath("$.metadata.page", is(1)));
        result.andExpect(jsonPath("$.metadata.size", is(2)));
    }

    @Test
    public void should_load_the_list_of_groups_2() throws Exception {

        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .grantedToManageRoles(Group.FREE_GROUP_NAME)
                .build();
        String accessToken = mockOAuthInterceptor(user);

        String mockJsonResult = "{\n" +
                                "  \"page\" : 1,\n" +
                                "  \"size\" : 2,\n" +
                                "  \"last\" : 1,\n" +
                                "  \"count\" : 6,\n" +
                                "  \"body\" : [ {\n" +
                                "    \"code\" : \"helpdesk\",\n" +
                                "    \"name\" : \"Helpdesk\"\n" +
                                "  }, {\n" +
                                "    \"code\" : \"management\",\n" +
                                "    \"name\" : \"Management\"\n" +
                                "  } ]\n" +
                                "}";
        PagedMetadata<GroupDto> mockResult = (PagedMetadata<GroupDto>) this.createPagedMetadata(mockJsonResult);
        when(groupService.getGroups(any(RestListRequest.class))).thenReturn(mockResult);

        // @formatter:off
        ResultActions result = mockMvc.perform(
                                               get("/groups")
                                               .param("pageNum", "1")
                                               .param("pageSize", "4")
                                               .param("filter[0].attribute", "code")
                                               .param("filter[0].value", "free")
                                               .header("Authorization", "Bearer " + accessToken)
                );
        // @formatter:on
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.payload", hasSize(2)));
        result.andExpect(jsonPath("$.metadata.page", is(1)));
        result.andExpect(jsonPath("$.metadata.size", is(2)));
    }


    @SuppressWarnings("unchecked")
    @Test
    public void should_be_unauthorized() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withGroup(Group.FREE_GROUP_NAME)
                .build();
        String accessToken = mockOAuthInterceptor(user);

        String mockJsonResult = "{\n" +
                                "  \"page\" : 1,\n" +
                                "  \"size\" : 2,\n" +
                                "  \"last\" : 1,\n" +
                                "  \"count\" : 6,\n" +
                                "  \"body\" : [ {\n" +
                                "    \"code\" : \"helpdesk\",\n" +
                                "    \"name\" : \"Helpdesk\"\n" +
                                "  }, {\n" +
                                "    \"code\" : \"management\",\n" +
                                "    \"name\" : \"Management\"\n" +
                                "  } ]\n" +
                                "}";
        PagedMetadata<GroupDto> mockResult = (PagedMetadata<GroupDto>) this.createPagedMetadata(mockJsonResult);
        when(groupService.getGroups(any(RestListRequest.class))).thenReturn(mockResult);

        // @formatter:off
        ResultActions result = mockMvc.perform(
                                               get("/groups")
                                               .header("Authorization", "Bearer " + accessToken)
                                               );
        // @formatter:on

        String response = result.andReturn().getResponse().getContentAsString();
        System.out.println(response);
        result.andExpect(status().isUnauthorized());
    }


    protected String mockOAuthInterceptor(UserDetails user) throws Exception, ApsSystemException {
        String accessToken = OAuth2TestUtils.getValidAccessToken();
        when(apiOAuth2TokenManager.getApiOAuth2Token(Mockito.anyString())).thenReturn(OAuth2TestUtils.getOAuth2Token(user.getUsername(), accessToken));
        when(authenticationProviderManager.getUser(user.getUsername())).thenReturn(user);
        when(authorizationManager.isAuthOnPermission(any(UserDetails.class), anyString())).then(new Answer<Boolean>() {

            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                UserDetails user = (UserDetails) invocation.getArguments()[0];
                String permissionName = (String) invocation.getArguments()[1];
                return new AuthorizationManager().isAuthOnPermission(user, permissionName);
            }
        });
        return accessToken;
    }


}
