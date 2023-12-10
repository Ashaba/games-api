package com.elevate.api.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @GenericGenerator(name = "uuid", type = org.hibernate.id.uuid.UuidGenerator.class)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Email(message = "Invalid Email Address")
    @NotBlank(message = "Email is required")
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank(message = "Username is required")
    @Column(unique = true, nullable = false)
    private String username;

    @NotBlank(message = "Full name is required")
    @Column(name = "full_name", nullable = false)
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
