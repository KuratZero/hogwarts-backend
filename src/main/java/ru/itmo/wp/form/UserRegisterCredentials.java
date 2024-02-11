package ru.itmo.wp.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Setter
@Getter
public class UserRegisterCredentials {
    @NotBlank
    @Size(min = 2, max = 24)
    @Pattern(regexp = "[a-z]+", message = "Expected lowercase Latin letters")
    private String login;
    @NotBlank
    @Size(min = 1, max = 100)
    private String name;
    @NotBlank
    @Size(min = 1, max = 60)
    private String password;
}
