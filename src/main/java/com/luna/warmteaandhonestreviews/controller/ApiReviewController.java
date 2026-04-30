package com.luna.warmteaandhonestreviews.controller;

import com.luna.warmteaandhonestreviews.dto.GetReviewImageRespDto;
import com.luna.warmteaandhonestreviews.dto.ReviewDto;
import com.luna.warmteaandhonestreviews.service.CategoryService;
import com.luna.warmteaandhonestreviews.service.ReviewService;
import com.luna.warmteaandhonestreviews.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
