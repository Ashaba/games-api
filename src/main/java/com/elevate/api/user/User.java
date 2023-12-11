package com.elevate.api.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email(message = "Invalid Email Address")
    @NotBlank(message = "Email is required")
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank(message = "Username is required")
    @Column(unique = true, nullable = false)
    private String username;

    @NotBlank(message = "full_name is required")
    @Column(name = "full_name", nullable = false)
    @JsonProperty("full_name")
    private String fullName;

    @NotBlank(message = "Password is required")
    @Column(nullable = false)
    private String password;

    public User(String email, String username, String fullName, String password) {
        this.email = email;
        this.username = username;
        this.fullName = fullName;
        this.password = password;
    }
}
