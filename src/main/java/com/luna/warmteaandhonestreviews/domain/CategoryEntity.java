package com.luna.warmteaandhonestreviews.domain;

import java.util.Objects;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "categories")
public class CategoryEntity extends BaseEntity {

    @Id
    private ObjectId id;
    private String name;

    public CategoryEntity() {
    }

    public CategoryEntity(String name) {
        this(null, name);
    }

    public CategoryEntity(ObjectId id, String name) {
        this.id = id;
        this.name = name;
    }

    public ObjectId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CategoryEntity that = (CategoryEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CategoryEntity{" +
            "id=" + id +
            ", name='" + name + '\'' +
            "} " + super.toString();
    }
}
