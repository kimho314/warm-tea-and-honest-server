package com.luna.warmteaandhonestreviews.controller;

import com.luna.warmteaandhonestreviews.core.WTAHUtility;
import com.luna.warmteaandhonestreviews.dto.GetReviewImageRespDto;
import com.luna.warmteaandhonestreviews.dto.GetReviewsRespDto;
import com.luna.warmteaandhonestreviews.dto.ReviewDto;
import com.luna.warmteaandhonestreviews.dto.SaveReviewReqDto;
import com.luna.warmteaandhonestreviews.dto.SaveReviewRespDto;
import com.luna.warmteaandhonestreviews.service.CategoryService;
import com.luna.warmteaandhonestreviews.service.ReviewService;
import com.luna.warmteaandhonestreviews.service.S3Service;
import com.luna.warmteaandhonestreviews.service.UserService;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/admin/reviews")
@RestController
public class AdminReviewController {

    private static final Logger log = LoggerFactory.getLogger(AdminReviewController.class);
    private final ReviewService reviewService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final S3Service s3Service;

    public AdminReviewController(
        ReviewService reviewService,
        UserService userService,
        CategoryService categoryService,
        S3Service s3Service
    ) {
        this.reviewService = reviewService;
        this.userService = userService;
        this.categoryService = categoryService;
        this.s3Service = s3Service;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReviewDto> getReview(
        @PathVariable(value = "id") String id,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(
            reviewService.getReview(id)
        );
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetReviewsRespDto> getReviews(
        @NonNull @RequestParam(defaultValue = "0") Integer page,
        @NonNull @RequestParam(defaultValue = "30") Integer offset,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(
            reviewService.getReviews(page, offset)
        );
    }

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
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
        List<String> categories = WTAHUtility.convertCategoryJsonToList(categoryJson);
        // check if there is new category from MongoDB, if not save it
        categoryService.saveNewCategories(categories);

        String imageUrl = s3Service.getURL(file);

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
                contents,
                imageUrl
            )
        );
        return ResponseEntity.ok(resp);
    }


    @GetMapping(value = "/{id}/image", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetReviewImageRespDto> getImage(
        @PathVariable("id") String id,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        ReviewDto review = reviewService.getReview(id);

        return ResponseEntity.ok()
            .body(new GetReviewImageRespDto(review.imageUrl()));
    }

}
