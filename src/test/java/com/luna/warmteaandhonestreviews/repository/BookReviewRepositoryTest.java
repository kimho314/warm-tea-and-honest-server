package com.luna.warmteaandhonestreviews.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.luna.warmteaandhonestreviews.AbstractTest;
import com.luna.warmteaandhonestreviews.config.MongoDBConfig;
import com.luna.warmteaandhonestreviews.domain.BookReviewEntity;
import com.luna.warmteaandhonestreviews.domain.UserEntity;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.mongodb.test.autoconfigure.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;

@Import({MongoDBConfig.class, BookReviewRepositoryCustom.class})
@DataMongoTest
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookReviewRepositoryTest {

    private static final Logger log = LoggerFactory.getLogger(BookReviewRepositoryTest.class);

    @Autowired
    BookReviewRepository bookReviewRepository;
    @Autowired
    BookReviewRepositoryCustom bookReviewRepositoryCustom;
    @Autowired
    MongoTemplate mongoTemplate;

    private BookReviewEntity bookReview;

    @Order(1)
    @Test
    void saveTest() {
        // given
        BookReviewEntity bookReview1 = AbstractTest.bookReview1;
        // when
        BookReviewEntity saved = bookReviewRepository.save(bookReview1);

        //then
        log.info("saved={}", saved);
        assertThat(saved).isNotNull();
        assertThat(saved.getTitle()).isEqualTo(bookReview1.getTitle());
        assertThat(saved.getAuthor()).isEqualTo(bookReview1.getAuthor());
        assertThat(saved.getRating()).isEqualTo(bookReview1.getRating());
        assertThat(saved.getAdminUserId()).isEqualTo(bookReview1.getAdminUserId());
        assertThat(saved.getCoverImage()).isEqualTo(bookReview1.getCoverImage());
        assertThat(saved.getExcerpt()).isEqualTo(bookReview1.getExcerpt());

        bookReview = saved;
    }

    @Order(2)
    @Test
    void findByAdminUserIdTest() {
        // given
        UserEntity adminUser1 = AbstractTest.adminUser1;

        List<BookReviewEntity> list = new ArrayList<>();
        list.add(AbstractTest.bookReview1);
        list.add(AbstractTest.bookReview2);

        // when
        bookReviewRepository.saveAll(list);
        Page<BookReviewEntity> reviews = bookReviewRepository.findAllByAdminUserId(
            adminUser1.getId(),
            PageRequest.of(0, 30));

        // then
        assertThat(reviews.getTotalElements()).isEqualTo(2);
        assertThat(reviews.getContent()).hasSameElementsAs(list);
    }

    @Order(3)
    @Test
    void findTop6SortedByCreatedAtTest() {
        //given

        //when
        List<BookReviewEntity> reviews = bookReviewRepository.findTop6ByOrderByCreatedAtDesc();

        //then
        log.info("reviews={}", reviews);
    }

    @Order(4)
    @Test
    void findReviewsWithCursorTest() {
        //given
        Instant instant = Instant.parse("2026-05-01T09:02:10.425Z");
        String id = "69f46c1221ad3eeeebcc8b66";

        //when
        List<BookReviewEntity> reviews = bookReviewRepositoryCustom.findReviews(new ObjectId(id),
            instant,
            3);

        //then
        assertThat(reviews).hasSizeLessThan(4);
        log.info("reviews={}", reviews);
    }

    @Order(5)
    @Test
    void findAllByCategoriesTest() {
        //given
        String category = "Fiction";
//        String category = null;
        //when
        Page<BookReviewEntity> reviews = bookReviewRepositoryCustom.getBookReviews(
            category,
            PageRequest.of(0, 6));

        //then
        log.info("reviews={}", reviews.getContent());
        Assertions.assertThat(reviews.getTotalElements()).isGreaterThan(0);
    }

    @Order(100)
    @Test
    void deleteCategoryTest() {
        //given

        //when
        bookReviewRepository.delete(bookReview);

        //then
    }
}
