package ru.itmo.wp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Setter
@Getter
@Entity
@Table(name="comments", indexes = @Index(columnList = "creationTime"))
public class Comment {
    @Id
    @GeneratedValue
    private long id;
    
    @Lob
    @NotNull
    @NotBlank
    @Size(min = 1, max = 700)
    private String text;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @CreationTimestamp
    private Date creationTime;

}