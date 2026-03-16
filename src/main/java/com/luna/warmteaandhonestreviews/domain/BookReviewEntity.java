package com.luna.warmteaandhonestreviews.domain;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "book_reviews")
public class BookReviewEntity extends BaseEntity {

    @Id
    private ObjectId id;
    private ObjectId adminUserId;
    private String title;
    private String author;
    private Double rating;
    private Integer page;
    private String language;
    private List<String> categories = new ArrayList<>();
    private Instant publishedAt;
    private String coverImage;
    private String contents;
    private String excerpt;


    public BookReviewEntity() {
    }

    public BookReviewEntity(ObjectId adminUserId,
        String title,
        String author,
        Double rating,
        Integer page,
        String language,
        List<String> categories,
        Instant publishedAt,
        String coverImage,
        String contents,
        String excerpt) {
        this(null,
            adminUserId,
            title,
            author,
            rating,
            page,
            language,
            categories,
            publishedAt,
            coverImage,
            contents,
            excerpt);
    }

    public BookReviewEntity(ObjectId id,
        ObjectId adminUserId,
        String title,
        String author,
        Double rating,
        Integer page,
        String language,
        List<String> categories,
        Instant publishedAt,
        String coverImage,
        String contents,
        String excerpt) {
        this.id = id;
        this.adminUserId = adminUserId;
        this.title = title;
        this.author = author;
        this.rating = rating;
        this.page = page;
        this.language = language;
        this.categories = categories;
        this.publishedAt = publishedAt;
        this.coverImage = coverImage;
        this.contents = contents;
        this.excerpt = excerpt;
    }

    public ObjectId getId() {
        return id;
    }

    public ObjectId getAdminUserId() {
        return adminUserId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public Double getRating() {
        return rating;
    }

    public Integer getPage() {
        return page;
    }

    public String getLanguage() {
        return language;
    }

    public List<String> getCategories() {
        return categories;
    }

    public Instant getPublishedAt() {
        return publishedAt;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public String getContents() {
        return contents;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BookReviewEntity that = (BookReviewEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "BookReviewEntity{" +
            "id=" + id +
            ", adminUserId=" + adminUserId +
            ", title='" + title + '\'' +
            ", author='" + author + '\'' +
            ", rating=" + rating +
            ", page=" + page +
            ", language='" + language + '\'' +
            ", categories=" + categories +
            ", publishedAt=" + publishedAt +
            ", coverImage='" + coverImage + '\'' +
            ", contents='" + contents + '\'' +
            ", excerpt='" + excerpt + '\'' +
            "} " + super.toString();
    }
}
