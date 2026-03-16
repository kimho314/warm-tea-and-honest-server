package com.luna.warmteaandhonestreviews.domain;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import org.bson.types.ObjectId;
import org.jspecify.annotations.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Document(collection = "users")
public class UserEntity extends BaseEntity implements UserDetails {

    @Id
    private ObjectId id;
    private String username;
    private String password;
    private String role;


    public UserEntity() {
    }

    public UserEntity(String username, String password, String role) {
        this(null, username, password, role);
    }


    public UserEntity(ObjectId id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public ObjectId getId() {
        return id;
    }

    public @NonNull String getUsername() {
        return username;
    }


    @Override
    public @NonNull Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserEntity that = (UserEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }


    @Override
    public String toString() {
        return "UserEntity{" +
            "id='" + id + '\'' +
            ", username='" + username + '\'' +
            ", password='" + password + '\'' +
            ", role='" + role + '\'' +
            "} " + super.toString();
    }
}
