package com.luna.warmteaandhonestreviews.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.luna.warmteaandhonestreviews.dto.CategoryDto;
import com.luna.warmteaandhonestreviews.service.CategoryService;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(ApiCategoryController.class)
public class ApiCategoriesControllerTest {

    private MockMvc mockMvc;

    @MockitoBean
    CategoryService categoryService;

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
    @DisplayName("get categories api")
    void getCategoriesTest() throws Exception {
        //given
        CategoryDto category1 = new CategoryDto(
            "69a98735da0083c357ccfe99",
            "Grief",
            LocalDateTime.now());
        List<CategoryDto> categories = List.of(category1);
        Mockito.when(categoryService.findAll())
            .thenReturn(categories);

        //when
        ResultActions perform = mockMvc.perform(
            get("/api/categories")
                .contentType(MediaType.APPLICATION_JSON));

        //then
        perform
            .andExpect(status().isOk())
            .andDo(document("{method-name}",
                responseFields(fieldWithPath("categories[].id").description("category id"),
                    fieldWithPath("categories[].name").description("category name"),
                    fieldWithPath("categories[].createdAt").description("review created at")
                )));
    }
}
