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

import com.luna.warmteaandhonestreviews.dto.ReviewDto;
import com.luna.warmteaandhonestreviews.service.CategoryService;
import com.luna.warmteaandhonestreviews.service.ReviewService;
import com.luna.warmteaandhonestreviews.service.UserService;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
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
@WebMvcTest(ApiReviewController.class)
public class ApiReviewControllerTest {

    private MockMvc mockMvc;

    @MockitoBean
    ReviewService reviewService;
    @MockitoBean
    UserService userService;
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
    void getApiReviewTest() throws Exception {
        //given
        String testId = "69f204be986cfb0f01cd9ad0";
        String adminUserId = "69a7e7bf7595cce6855d25ac";
        String title = "test title";
        String slug = "test-title";
        String author = "test author";
        double rating = 4.5;
        int page = 300;
        String language = "English";
        List<String> categories = List.of("Fiction");
        LocalDate publishedAt = LocalDate.now();
        LocalDate createdAt = LocalDate.now();
        String coverImage = "test cover image";
        String excerpt = "test excerpt";
        String content = "<html><h1>Hello</html>";
        String image = "test image";

        Mockito.when(reviewService.getReview(testId))
            .thenReturn(new ReviewDto(
                testId,
                adminUserId,
                title,
                slug,
                author,
                rating,
                page,
                language,
                categories,
                publishedAt,
                createdAt,
                coverImage,
                excerpt,
                content,
                image
            ));

        // when
        ResultActions perform = mockMvc.perform(
            get("/api/reviews/{id}", testId)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        perform
            .andExpect(status().isOk())
            .andDo(document("{method-name}",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(fieldWithPath("id").description("review id"),
                    fieldWithPath("adminUserId").description("review admin user id"),
                    fieldWithPath("title").description("review title"),
                    fieldWithPath("slug").description("review slug"),
                    fieldWithPath("author").description("review author"),
                    fieldWithPath("rating").description("review rating"),
                    fieldWithPath("page").description("review page"),
                    fieldWithPath("language").description("review language"),
                    fieldWithPath("categories").description("review category"),
                    fieldWithPath("publishedAt").description("review published at"),
                    fieldWithPath("createdAt").description("review created at"),
                    fieldWithPath("coverImage").description("review cover image"),
                    fieldWithPath("excerpt").description("review excerpt"),
                    fieldWithPath("imageUrl").description("test url"),
                    fieldWithPath("content").description("review contents"))
            ));
    }

}
