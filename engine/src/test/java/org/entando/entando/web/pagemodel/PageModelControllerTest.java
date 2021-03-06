package org.entando.entando.web.pagemodel;

import com.agiletec.aps.system.services.user.UserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.entando.entando.aps.system.services.pagemodel.PageModelService;
import org.entando.entando.web.AbstractControllerTest;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.pagemodel.model.PageModelFrameReq;
import org.entando.entando.web.pagemodel.model.PageModelRequest;
import org.entando.entando.web.pagemodel.validator.PageModelValidator;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PageModelControllerTest extends AbstractControllerTest {


    private MockMvc mockMvc;

    @Mock
    private PageModelService pageModelService;

    @InjectMocks
    private PageModelController controller;


    @Spy
    private PageModelValidator pagemModelValidator = new PageModelValidator();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                                 .addInterceptors(entandoOauth2Interceptor)
                                 .setHandlerExceptionResolvers(createHandlerExceptionResolver())
                                 .build();
    }


    @Test
    public void should_load_the_list_of_pageModels() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);


        when(pageModelService.getPageModels(any(RestListRequest.class))).thenReturn(new PagedMetadata<>());
        ResultActions result = mockMvc.perform(get("/pagemodels")
                                                                 .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isOk());
        RestListRequest restListReq = new RestListRequest();
        Mockito.verify(pageModelService, Mockito.times(1)).getPageModels(restListReq);
    }

    @Test
    public void should_validate_add_page_model_empty() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        PageModelRequest pageModel = new PageModelRequest();
        ObjectMapper mapper = new ObjectMapper();
        String payload = mapper.writeValueAsString(pageModel);
        ResultActions result = mockMvc.perform(
                                               post("/pagemodels")
                                                                  .content(payload)
                                                                  .contentType(MediaType.APPLICATION_JSON)
                                                                  .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isBadRequest());
        result.andExpect(jsonPath("$.errors.length()", is(3)));

    }

    @Test
    public void should_validate_add_page_model_invalid_frames_first() throws Exception {

        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);

        PageModelRequest pageModel = new PageModelRequest();
        pageModel.setCode("test");
        pageModel.setDescription("test_descr");

        PageModelFrameReq frame0 = new PageModelFrameReq();
        frame0.setPos(1);
        pageModel.getConfiguration().add(frame0);

        ObjectMapper mapper = new ObjectMapper();
        String payload = mapper.writeValueAsString(pageModel);

        ResultActions result = mockMvc.perform(
                                               post("/pagemodels")
                                               .content(payload)
                                               .contentType(MediaType.APPLICATION_JSON)
                                               .header("Authorization", "Bearer " + accessToken));

        result.andExpect(status().isBadRequest());
        result.andExpect(jsonPath("$.errors.length()", is(1)));

    }

    @Test
    public void should_validate_add_page_model_invalid_frames_last() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);

        PageModelRequest pageModel = new PageModelRequest();
        pageModel.setCode("test");
        pageModel.setDescription("test_descr");

        PageModelFrameReq frame0 = new PageModelFrameReq(0, "descr_0");
        PageModelFrameReq frame1 = new PageModelFrameReq(2, "descr_1");
        pageModel.getConfiguration().add(frame0);
        pageModel.getConfiguration().add(frame1);

        ObjectMapper mapper = new ObjectMapper();
        String payload = mapper.writeValueAsString(pageModel);

        ResultActions result = mockMvc.perform(
                                               post("/pagemodels")
                                                                  .content(payload)
                                                                  .contentType(MediaType.APPLICATION_JSON)
                                                                  .header("Authorization", "Bearer " + accessToken));

        result.andExpect(status().isBadRequest());
        result.andExpect(jsonPath("$.errors.length()", is(1)));

    }

    @Test
    public void should_validate_add_page_model_invalid_frames_progressive() throws Exception {

        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);

        PageModelRequest pageModel = new PageModelRequest();
        pageModel.setCode("test");
        pageModel.setDescription("test_descr");

        PageModelFrameReq frame0 = new PageModelFrameReq(0, "descr_0");
        PageModelFrameReq frame1 = new PageModelFrameReq(0, "descr_1");
        PageModelFrameReq frame2 = new PageModelFrameReq(2, "descr_2");

        pageModel.getConfiguration().add(frame0);
        pageModel.getConfiguration().add(frame1);
        pageModel.getConfiguration().add(frame2);

        ObjectMapper mapper = new ObjectMapper();
        String payload = mapper.writeValueAsString(pageModel);

        ResultActions result = mockMvc.perform(
                                               post("/pagemodels")
                                                                  .content(payload)
                                                                  .contentType(MediaType.APPLICATION_JSON)
                                                                  .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isBadRequest());
        result.andExpect(jsonPath("$.errors.length()", is(1)));

    }

    @Test
    public void should_validate_add_page_model_frame_no_descr() throws Exception {

        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);

        PageModelRequest pageModel = new PageModelRequest();
        pageModel.setCode("test");
        pageModel.setDescription("test_descr");

        PageModelFrameReq frame0 = new PageModelFrameReq(0, "descr_0");
        PageModelFrameReq frame1 = new PageModelFrameReq(1, null);


        pageModel.getConfiguration().add(frame0);
        pageModel.getConfiguration().add(frame1);

        ObjectMapper mapper = new ObjectMapper();
        String payload = mapper.writeValueAsString(pageModel);

        ResultActions result = mockMvc.perform(
                                               post("/pagemodels")
                                                                  .content(payload)
                                                                  .contentType(MediaType.APPLICATION_JSON)
                                                                  .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isBadRequest());

    }

    @Test
    public void should_add_valid_page_model() throws Exception {

        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);

        String payload = " {\n" +
                         "            \"code\": \"test\",\n" +
                         "            \"description\": \"test\",\n" +
                         "            \"configuration\": {\n" +
                         "                \"frames\": [\n" +
                         "                    {\n" +
                         "                        \"pos\": 0,\n" +
                         "                        \"description\": \"test_frame\",\n" +
                         "                        \"mainFrame\": false,\n" +
                         "                        \"defaultWidget\": null,\n" +
                         "                        \"sketch\": null\n" +
                         "                    }\n" +
                         "                ]\n" +
                         "            },\n" +
                         "            \"pluginCode\": null,\n" +
                         "            \"template\": \"ciao\"\n" +
                         "        }";

        ResultActions result = mockMvc.perform(
                                               post("/pagemodels")
                                                                  .content(payload)
                                                                  .contentType(MediaType.APPLICATION_JSON)
                                                                  .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isOk());
        Mockito.verify(pageModelService, Mockito.times(1)).addPageModel(Mockito.any());

    }

}
