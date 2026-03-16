package com.luna.warmteaandhonestreviews.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.luna.warmteaandhonestreviews.AbstractTest;
import com.luna.warmteaandhonestreviews.config.MongoDBConfig;
import com.luna.warmteaandhonestreviews.domain.BookReviewEntity;
import com.luna.warmteaandhonestreviews.domain.UserEntity;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.mongodb.test.autoconfigure.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@Import(MongoDBConfig.class)
@DataMongoTest
public class BookReviewRepositoryTest {

    private static final Logger log = LoggerFactory.getLogger(BookReviewRepositoryTest.class);

    @Autowired
    BookReviewRepository bookReviewRepository;

    @AfterEach
    void tearDown() {
        bookReviewRepository.deleteAll();
    }

    @Test
    void saveTest() {
        // given
        BookReviewEntity bookReview = AbstractTest.bookReview1;
        // when
        BookReviewEntity saved = bookReviewRepository.save(bookReview);

        //then
        log.info("saved={}", saved);
        assertThat(saved).isNotNull();
        assertThat(saved.getTitle()).isEqualTo(bookReview.getTitle());
        assertThat(saved.getAuthor()).isEqualTo(bookReview.getAuthor());
        assertThat(saved.getRating()).isEqualTo(bookReview.getRating());
        assertThat(saved.getAdminUserId()).isEqualTo(bookReview.getAdminUserId());
        assertThat(saved.getCoverImage()).isEqualTo(bookReview.getCoverImage());
        assertThat(saved.getExcerpt()).isEqualTo(bookReview.getExcerpt());
    }

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
}
