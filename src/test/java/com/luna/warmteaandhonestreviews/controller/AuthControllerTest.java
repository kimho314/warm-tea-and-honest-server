package com.luna.warmteaandhonestreviews.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.luna.warmteaandhonestreviews.config.SecurityConfig;
import com.luna.warmteaandhonestreviews.dto.LoginReqDto;
import com.luna.warmteaandhonestreviews.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tools.jackson.databind.ObjectMapper;

@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
public class AuthControllerTest {

    private MockMvc mockMvc;

    @MockitoBean
    UserService userService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext,
        RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply(documentationConfiguration(restDocumentation))
            .alwaysDo(document("{method-name}",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())))
            .build();
    }

    @Test
    void loginSuccessTest() throws Exception {
        //given
        String username = "NilKim";
        String password = "1234";
        ObjectMapper mapper = new ObjectMapper();

        Mockito.when(userService.login(username, password))
            .thenReturn(Boolean.TRUE);

        //when
        ResultActions perform = mockMvc.perform(post("/admin/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(new LoginReqDto(username, password))));

        //then
        perform.andExpect(status().isOk());
    }

    @Test
    void loginFailTest() throws Exception {
        //given
        String username = "NilKim";
        String password = "1234";
        ObjectMapper mapper = new ObjectMapper();

        Mockito.when(userService.login(username, password))
            .thenReturn(Boolean.FALSE);

        //when
        ResultActions perform = mockMvc.perform(post("/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(new LoginReqDto(username, password))));

        //then
        perform.andExpect(status().is4xxClientError());
    }
}
