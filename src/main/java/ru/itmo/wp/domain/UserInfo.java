package ru.itmo.wp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Data
public class UserInfo {
    @Id
    @Column(name = "user_id")
    @JsonIgnore
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User userInfo;

    @Size(max=2500)
    private String about;

    @Size(max=200)
    private String city;

}
