package ru.itmo.wp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Getter
@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "login"))
public class User {
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @NotBlank
    @Size(min = 1, max = 100)
    private String name;

    @Setter
    @NotBlank
    @Size(min = 2, max = 24)
    @Pattern(regexp = "[a-z]+")
    private String login;

    @Setter
    @JsonIgnore
    private boolean admin;

    @Setter
    @JsonIgnore
    @CreationTimestamp
    private Date creationTime;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Post> posts;

    @OneToOne(mappedBy = "userInfo", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    @Setter
    private UserInfo info;

    public void addPost(Post post) {
        posts.add(post);
        post.setUser(this);
    }
}
