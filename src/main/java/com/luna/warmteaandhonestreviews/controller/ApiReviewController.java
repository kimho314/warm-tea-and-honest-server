package com.luna.warmteaandhonestreviews.controller;

import com.luna.warmteaandhonestreviews.dto.GetRecentReviewsRespDto;
import com.luna.warmteaandhonestreviews.dto.GetReviewImageRespDto;
import com.luna.warmteaandhonestreviews.dto.GetReviewsRespDto;
import com.luna.warmteaandhonestreviews.dto.ReviewDto;
import com.luna.warmteaandhonestreviews.service.CategoryService;
import com.luna.warmteaandhonestreviews.service.ReviewService;
import com.luna.warmteaandhonestreviews.service.UserService;
import java.util.List;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reviews")
public class ApiReviewController {

    private static final Logger log = LoggerFactory.getLogger(ApiReviewController.class);
    private final ReviewService reviewService;
    private final UserService userService;
    private final CategoryService categoryService;

    public ApiReviewController(ReviewService reviewService, UserService userService,
        CategoryService categoryService) {
        this.reviewService = reviewService;
        this.userService = userService;
        this.categoryService = categoryService;
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

    @GetMapping(value = "/{id}/image", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetReviewImageRespDto> getImage(@PathVariable("id") String id) {
        ReviewDto review = reviewService.getReview(id);

        return ResponseEntity.ok()
            .body(new GetReviewImageRespDto(review.imageUrl()));
    }

    @GetMapping(value = "", params = "sort", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetRecentReviewsRespDto> getRecentReviews(
        @RequestParam("sort") String sort) {
        List<ReviewDto> recentReviews = reviewService.getRecentReviews(sort);

        return ResponseEntity.ok(new GetRecentReviewsRespDto(recentReviews));
    }

    @GetMapping(value = "", params = {"page",
        "offset"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetReviewsRespDto> getReviews(
        @NonNull @RequestParam(defaultValue = "0") Integer page,
        @NonNull @RequestParam(defaultValue = "6") Integer offset
    ) {
        return ResponseEntity.ok(
            reviewService.getReviews(
                page,
                offset
            )
        );
    }
}
