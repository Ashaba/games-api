package com.elevate.api.game;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import java.io.Serializable;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Table(name = "games")
public class Game implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private long id;

    @Column(nullable = false)
    @NotBlank(message = "Name is required")
    private String name;

    @Column(nullable = false)
    @URL(regexp = "^(http|ftp).*", message = "Invalid URL")
    @NotBlank(message = "URL is required")
    private String url;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Category is required")
    private GameCategory category;

    public Game() {

    }

    public Game(String name, String url, GameCategory category) {
        this.name = name;
        this.url = url;
        this.category = category;
    }
}
