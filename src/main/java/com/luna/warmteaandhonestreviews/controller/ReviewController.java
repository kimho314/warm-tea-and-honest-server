package com.luna.warmteaandhonestreviews.controller;

import com.luna.warmteaandhonestreviews.dto.GetReviewsRespDto;
import com.luna.warmteaandhonestreviews.dto.ReviewDto;
import com.luna.warmteaandhonestreviews.dto.SaveReviewReqDto;
import com.luna.warmteaandhonestreviews.dto.SaveReviewRespDto;
import com.luna.warmteaandhonestreviews.service.CategoryService;
import com.luna.warmteaandhonestreviews.service.ReviewService;
import com.luna.warmteaandhonestreviews.service.StorageService;
import com.luna.warmteaandhonestreviews.service.UserService;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

@RestController
public class ReviewController {

    private static final Logger log = LoggerFactory.getLogger(ReviewController.class);
    private final ReviewService reviewService;
    private final StorageService storageService;
    private final UserService userService;
    private final CategoryService categoryService;

    public ReviewController(
        ReviewService reviewService,
        StorageService storageService,
        UserService userService,
        CategoryService categoryService
    ) {
        this.reviewService = reviewService;
        this.storageService = storageService;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    @GetMapping(value = "/admin/reviews/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReviewDto> getReview(
        @PathVariable(value = "id") String id,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(
            reviewService.getReview(
                userService.getUserIdByUsername(userDetails.getUsername()),
                id
            )
        );
    }

    @GetMapping(value = "/admin/reviews", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetReviewsRespDto> getReviews(
        @NonNull @RequestParam(defaultValue = "0") Integer page,
        @NonNull @RequestParam(defaultValue = "30") Integer offset,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(
            reviewService.getReviews(userService.getUserIdByUsername(userDetails.getUsername()),
                page,
                offset
            )
        );
    }

    @PostMapping(value = "/admin/reviews", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SaveReviewRespDto> createReview(
        @RequestPart("cover") MultipartFile file,
        @RequestPart("title") String title,
        @RequestPart("author") String author,
        @RequestPart("rating") Double rating,
        @RequestPart("page") Integer page,
        @RequestPart("language") String language,
        @RequestPart("category") String categoryJson,
        @RequestPart("content") String contents,
        @RequestPart("publishedAt") @DateTimeFormat(pattern = "yyyy-MM-dd") String publishedAt,
        @RequestPart(value = "excerpt", required = false) String excerpt,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        log.info("categories : {}", categoryJson);
        Optional<ReviewDto> maybeReview = reviewService.getByTitle(title);
        if (maybeReview.isPresent()) {
            return ResponseEntity.ok(new SaveReviewRespDto(maybeReview.get().id()));
        }

        String adminUserId = userService.getUserIdByUsername(userDetails.getUsername());
        storageService.store(file);

        List<String> categories = convertCategoryJsonToList(categoryJson);
        // check if there is new category from MongoDB, if not save it
        categoryService.saveNewCategories(categories);

        SaveReviewRespDto resp = reviewService.save(
            new SaveReviewReqDto(
                adminUserId,
                title,
                author,
                rating,
                page,
                language,
                categories,
                LocalDate.parse(publishedAt),
                excerpt,
                file.getOriginalFilename(),
                contents
            )
        );
        return ResponseEntity.ok(resp);
    }

    private @NonNull List<String> convertCategoryJsonToList(String categoryJson) {
        ObjectMapper mapper = new ObjectMapper();
        return Arrays.stream(mapper.readValue(categoryJson, String[].class))
            .toList();
    }


    @GetMapping(value = "/admin/reviews/{id}/image", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> getImage(
        @PathVariable("id") String id,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        log.info("name: {}, authority: {}", userDetails.getUsername(),
            userDetails.getAuthorities());
        String adminUserId = userService.getUserIdByUsername(userDetails.getUsername());
        String reviewImage = reviewService.getReviewImage(adminUserId, id);

        Resource resource = storageService.loadAsResource(reviewImage);
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + resource.getFilename() + "\"")
            .body(resource);
    }

}
