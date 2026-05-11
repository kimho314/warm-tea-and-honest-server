package com.luna.warmteaandhonestreviews.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.luna.warmteaandhonestreviews.config.MongoDBConfig;
import com.luna.warmteaandhonestreviews.domain.CategoryEntity;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.mongodb.test.autoconfigure.DataMongoTest;
import org.springframework.context.annotation.Import;

@Import({MongoDBConfig.class})
@DataMongoTest
public class CategoryRepositoryTest {

    private static final Logger log = LoggerFactory.getLogger(CategoryRepositoryTest.class);

    @Autowired
    CategoryRepository categoryRepository;

    @Test
    void saveCategoryTest() {
        //given
        CategoryEntity categoryEntity = new CategoryEntity("SyFi");

        //when
        CategoryEntity saved = categoryRepository.save(categoryEntity);

        //then
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("SyFi");
    }
}
