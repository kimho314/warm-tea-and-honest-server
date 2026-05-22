package com.luna.warmteaandhonestreviews.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.luna.warmteaandhonestreviews.dto.GetReviewsRespDto;
import com.luna.warmteaandhonestreviews.dto.ReviewDto;
import com.luna.warmteaandhonestreviews.dto.SaveReviewRespDto;
import com.luna.warmteaandhonestreviews.service.CategoryService;
import com.luna.warmteaandhonestreviews.service.ReviewService;
import com.luna.warmteaandhonestreviews.service.S3Service;
import com.luna.warmteaandhonestreviews.service.StorageService;
import com.luna.warmteaandhonestreviews.service.UserService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;
import org.springframework.web.context.WebApplicationContext;
import tools.jackson.databind.ObjectMapper;

@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(AdminReviewController.class)
@WithMockUser(username = "NilKim", roles = "ADMIN", password = "1234")
public class AdminReviewControllerTest {

    private MockMvc mockMvc;

    @MockitoBean
    ReviewService reviewService;
    @MockitoBean
    StorageService storageService;
    @MockitoBean
    UserService userService;
    @MockitoBean
    CategoryService categoryService;
    @MockitoBean
    S3Service s3Service;

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
    void getReviewTest() throws Exception {
        // given
        String id = UUID.randomUUID().toString();
        String adminUserID = "162a59e1-571f-42a3-a41a-edc83b03618a";
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
        String url = "http://test.com";

        Mockito.when(reviewService.getReview(id))
            .thenReturn(new ReviewDto(
                id,
                adminUserID,
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
                url
            ));

        // when
        ResultActions perform = mockMvc.perform(
            get("/admin/reviews/{id}", id)
                .with(httpBasic("NilKim", "1234"))
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

    @Test
    void getReviewsTest() throws Exception {
        // given
        Integer page = 1;
        Integer offset = 30;
        String adminUserId = "162a59e1-571f-42a3-a41a-edc83b03618a";

        List<ReviewDto> reviewDtos = new ArrayList<>();
        String id = UUID.randomUUID().toString();
        String title = "test title";
        String slug = "test-title";
        String author = "test author";
        double rating = 4.5;
        int bookPage = 300;
        String language = "English";
        List<String> categories = List.of("Fiction");
        LocalDate publishedAt = LocalDate.now();
        LocalDate createdAt = LocalDate.now();
        String coverImage = "test cover image";
        String excerpt = "test excerpt";
        String content = "<html><h1>Hello</html>";
        String url = "http://test.com";

        ReviewDto review1 = new ReviewDto(id,
            adminUserId,
            title,
            slug,
            author,
            rating,
            bookPage,
            language,
            categories,
            publishedAt,
            createdAt,
            coverImage,
            excerpt,
            content,
            url
        );
        reviewDtos.add(review1);

        Mockito.when(reviewService.getReviews(page, offset))
            .thenReturn(new GetReviewsRespDto(reviewDtos,
                2L,
                page,
                offset));

        // when
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("page", page.toString());
        params.add("offset", offset.toString());
        ResultActions perform = mockMvc.perform(
            get("/admin/reviews")
                .params(params)
                .with(httpBasic("NilKim", "1234"))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        perform
            .andExpect(status().isOk())
            .andDo(document("{method-name}",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                    fieldWithPath("reviews[]").description("An array of reviews"),
                    fieldWithPath("reviews[].id").description("An review's id"),
                    fieldWithPath("reviews[].adminUserId").description("An review's admin user id"),
                    fieldWithPath("reviews[].title").description("An review's title"),
                    fieldWithPath("reviews[].slug").description("An review's slug"),
                    fieldWithPath("reviews[].author").description("An review's author"),
                    fieldWithPath("reviews[].rating").description("An review's rating"),
                    fieldWithPath("reviews[].page").description("An review's book page"),
                    fieldWithPath("reviews[].language").description("An review's language"),
                    fieldWithPath("reviews[].categories").description("An review's category"),
                    fieldWithPath("reviews[].publishedAt").description("An review's published at"),
                    fieldWithPath("reviews[].createdAt").description("An review's created at"),
                    fieldWithPath("reviews[].coverImage").description("An review's cover image"),
                    fieldWithPath("reviews[].excerpt").description("An review's excerpt"),
                    fieldWithPath("reviews[].content").description("review contents"),
                    fieldWithPath("reviews[].imageUrl").description("test url"),
                    fieldWithPath("total").description("Total number of reviews"),
                    fieldWithPath("page").description("Current page number"),
                    fieldWithPath("offset").description("Current offset")
                )));
    }

    @Test
    void createReviewTest() throws Exception {
        // given
        ClassPathResource resource = new ClassPathResource("IlkbaharRuyasi.jpg");
        byte[] imageBytes = StreamUtils.copyToByteArray(resource.getInputStream());
        MockMultipartFile cover = new MockMultipartFile("cover",
            "IlkbaharRuyasi.jpg",
            MediaType.IMAGE_JPEG_VALUE,
            imageBytes
        );
        String adminUserId = "162a59e1-571f-42a3-a41a-edc83b03618a";

        Mockito.when(reviewService.getByTitle("test title"))
            .thenReturn(Optional.empty());
        Mockito.when(s3Service.getURL(any())).thenReturn(
            "https://warm-tea-and-honest.s3.eu-west-2.amazonaws.com/dev/wheelie-awkward-romance.jpg");
        Mockito.when(reviewService.save(any()))
            .thenReturn(new SaveReviewRespDto(UUID.randomUUID().toString()));
        Mockito.when(userService.getUserIdByUsername(anyString()))
            .thenReturn(adminUserId);
        Mockito.doNothing().when(categoryService).saveNewCategories(anyList());

        List<String> categories = List.of("Fiction");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(categories);

        // when
        ResultActions perform = mockMvc.perform(multipart("/admin/reviews")
            .with(httpBasic("NilKim", "1234"))
            .file(cover)
            .part(new MockPart("title", null, "test title".getBytes(), MediaType.APPLICATION_JSON))
            .part(
                new MockPart("author", null, "test author".getBytes(), MediaType.APPLICATION_JSON))
            .part(new MockPart("rating", null, "4.5".getBytes(), MediaType.APPLICATION_JSON))
            .part(new MockPart("page", null, "300".getBytes(), MediaType.APPLICATION_JSON))
            .part(new MockPart("language", null, "English".getBytes(), MediaType.APPLICATION_JSON))
            .part(new MockPart("category", null, json.getBytes(), MediaType.APPLICATION_JSON))
            .part(new MockPart("publishedAt", null, "2021-08-01".getBytes(),
                MediaType.APPLICATION_JSON))
            .part(new MockPart("excerpt", null, "test excerpt".getBytes(),
                MediaType.APPLICATION_JSON))
            .part(new MockPart("content", null, "<html></html>".getBytes(),
                MediaType.APPLICATION_JSON))
        );

        // then
        perform
            .andExpect(status().isOk())
            .andDo(document("{method-name}",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(fieldWithPath("id").description("review id"))
            ));

    }

    @Test
    void getReviewImageTest() throws Exception {
        //given
        String id = UUID.randomUUID().toString();
        String adminUserId = "162a59e1-571f-42a3-a41a-edc83b03618a";
        String coverImage = "test cover image";

        List<ReviewDto> reviewDtos = new ArrayList<>();
        String title = "test title";
        String slug = "test-title";
        String author = "test author";
        double rating = 4.5;
        int bookPage = 300;
        String language = "English";
        List<String> categories = List.of("Fiction");
        LocalDate publishedAt = LocalDate.now();
        LocalDate createdAt = LocalDate.now();
        String excerpt = "test excerpt";
        String content = "<html><h1>Hello</html>";
        String url = "http://test.com";

        ReviewDto review1 = new ReviewDto(id,
            adminUserId,
            title,
            slug,
            author,
            rating,
            bookPage,
            language,
            categories,
            publishedAt,
            createdAt,
            coverImage,
            excerpt,
            content,
            url
        );
        reviewDtos.add(review1);

        Mockito.when(reviewService.getReview(anyString()))
            .thenReturn(review1);

        //when
        ResultActions perform = mockMvc.perform(get("/admin/reviews/{id}/image", id)
            .with(httpBasic("NilKim", "1234"))
            .contentType(MediaType.APPLICATION_JSON));

        //then
        perform
            .andExpect(status().isOk())
            .andDo(document("{method-name}",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(fieldWithPath("imageUrl").description(
                    "https://warm-tea-and-honest.s3.eu-west-2.amazonaws.com/dev/wheelie-awkward-romance.jpg"))
            ));
    }

    @Test
    void deleteReviewTest() throws Exception {
        //given
        String reviewId = "6a05a6f0cc9b447b75164f77";

        Mockito.doNothing().when(reviewService).deleteReview(reviewId);

        //when
        ResultActions perform = mockMvc.perform(
            delete("/admin/reviews/{id}", reviewId)
                .with(httpBasic("NilKim", "1234"))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        perform
            .andExpect(status().isOk())
            .andDo(document("{method-name}",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()))
            );
    }
}
