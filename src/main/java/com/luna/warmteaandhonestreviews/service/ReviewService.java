package com.luna.warmteaandhonestreviews.service;

import com.luna.warmteaandhonestreviews.domain.BookReviewEntity;
import com.luna.warmteaandhonestreviews.dto.GetReviewsRespDto;
import com.luna.warmteaandhonestreviews.dto.ReviewDto;
import com.luna.warmteaandhonestreviews.dto.SaveReviewReqDto;
import com.luna.warmteaandhonestreviews.dto.SaveReviewRespDto;
import com.luna.warmteaandhonestreviews.exception.ReviewNotFoundException;
import com.luna.warmteaandhonestreviews.repository.BookReviewRepository;
import com.luna.warmteaandhonestreviews.repository.BookReviewRepositoryCustom;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    private final BookReviewRepository bookReviewRepository;
    private final BookReviewRepositoryCustom bookReviewRepositoryCustom;

    public ReviewService(BookReviewRepository bookReviewRepository,
        BookReviewRepositoryCustom bookReviewRepositoryCustom) {
        this.bookReviewRepository = bookReviewRepository;
        this.bookReviewRepositoryCustom = bookReviewRepositoryCustom;
    }

    public ReviewDto getReview(@NonNull String reviewId) {
        BookReviewEntity bookReview = bookReviewRepository.findById(new ObjectId(reviewId))
            .orElseThrow(
                () -> new ReviewNotFoundException("Review not found with id: " + reviewId));

        return ReviewDto.of(bookReview);
    }

    public ReviewDto getReview(@NonNull String adminUserId, @NonNull String reviewId) {
        BookReviewEntity bookReview = bookReviewRepository.findByAdminUserIdAndId(
            new ObjectId(adminUserId),
            new ObjectId(reviewId)
        ).orElseThrow(() -> new ReviewNotFoundException("Review not found with id: " + reviewId));

        return ReviewDto.of(bookReview);
    }

    public GetReviewsRespDto getReviews(
        @NonNull Integer page,
        @NonNull Integer offset) {
        Page<BookReviewEntity> adminUsers = bookReviewRepository.findAll(
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

    public GetReviewsRespDto getReviews(
        @NonNull Integer page,
        @NonNull Integer offset,
        @Nullable String category
    ) {
        Page<BookReviewEntity> adminUsers = bookReviewRepositoryCustom.getBookReviews(
            category,
            PageRequest.of(page, offset, Sort.by("createdAt").descending())
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

    public List<ReviewDto> getRecentReviews(@NonNull String sort) {
        List<ReviewDto> recentReviews = bookReviewRepository.findTop6ByOrderByCreatedAtDesc()
            .stream()
            .map(it -> ReviewDto.of(it))
            .toList();
        return recentReviews;
    }
}
