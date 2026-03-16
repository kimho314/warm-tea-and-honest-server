package com.luna.warmteaandhonestreviews.service;

import com.luna.warmteaandhonestreviews.domain.BookReviewEntity;
import com.luna.warmteaandhonestreviews.dto.GetReviewsRespDto;
import com.luna.warmteaandhonestreviews.dto.ReviewDto;
import com.luna.warmteaandhonestreviews.dto.SaveReviewReqDto;
import com.luna.warmteaandhonestreviews.dto.SaveReviewRespDto;
import com.luna.warmteaandhonestreviews.exception.ReviewNotFoundException;
import com.luna.warmteaandhonestreviews.repository.BookReviewRepository;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    private final BookReviewRepository bookReviewRepository;

    public ReviewService(BookReviewRepository bookReviewRepository) {
        this.bookReviewRepository = bookReviewRepository;
    }

    public ReviewDto getReview(@NonNull String adminUserId, @NonNull String reviewId) {
        BookReviewEntity bookReview = bookReviewRepository.findByAdminUserIdAndId(
            new ObjectId(adminUserId),
            new ObjectId(reviewId)
        ).orElseThrow(() -> new ReviewNotFoundException("Review not found with id: " + reviewId));

        return ReviewDto.of(bookReview);
    }

    public String getReviewImage(@NonNull String adminUserId, @NonNull String reviewId) {
        BookReviewEntity bookReview = bookReviewRepository.findByAdminUserIdAndId(
                new ObjectId(adminUserId),
                new ObjectId(reviewId)
            )
            .orElseThrow(
                () -> new ReviewNotFoundException("Review not found with id: " + reviewId));
        return bookReview.getCoverImage();
    }

    public GetReviewsRespDto getReviews(@NonNull String adminUserId,
        @NonNull Integer page,
        @NonNull Integer offset) {
        Page<BookReviewEntity> adminUsers = bookReviewRepository.findAllByAdminUserId(
            new ObjectId(adminUserId),
            PageRequest.of(page, offset)
        );

        List<ReviewDto> reviewDtos = adminUsers.getContent().stream()
            .map(ReviewDto::of)
            .toList();

        return new GetReviewsRespDto(reviewDtos,
            adminUsers.getTotalElements(),
            adminUsers.getNumber(),
            adminUsers.getSize());
    }

    public SaveReviewRespDto save(@NonNull SaveReviewReqDto saveReviewReqDto) {
        BookReviewEntity saved = bookReviewRepository.save(saveReviewReqDto.toEntity());
        return new SaveReviewRespDto(saved.getId().toString());
    }

    public Optional<ReviewDto> getByTitle(@NonNull String title) {
        return bookReviewRepository.findByTitle(title).map(ReviewDto::of);
    }
}
