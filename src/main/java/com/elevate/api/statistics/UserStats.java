package com.elevate.api.statistics;

import com.elevate.api.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Setter
@Getter
@Table(name = "user_stats")
@AllArgsConstructor
@NoArgsConstructor
public class UserStats {
    @Id
    @JsonIgnore
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    @MapsId
    @JsonIgnore
    private User user;

    @JsonProperty("total_games_played")
    private int totalGamesPlayed;
    @JsonProperty("total_math_games_played")
    private int totalMathGamesPlayed;
    @JsonProperty("total_reading_games_played")
    private int totalReadingGamesPlayed;
    @JsonProperty("total_speaking_games_played")
    private int totalSpeakingGamesPlayed;
    @JsonProperty("total_writing_games_played")
    private int totalWritingGamesPlayed;
    @JsonProperty("current_streak_in_days")
    private int currentStreakInDays;
    @JsonIgnore
    private LocalDate lastPlayedDate;

    public UserStats(User user, int totalGamesPlayed, int totalMathGamesPlayed, int totalReadingGamesPlayed,
                     int totalSpeakingGamesPlayed, int totalWritingGamesPlayed, int currentStreakInDays,
                     LocalDate lastPlayedDate) {
        this.user = user;
        this.totalGamesPlayed = totalGamesPlayed;
        this.totalMathGamesPlayed = totalMathGamesPlayed;
        this.totalReadingGamesPlayed = totalReadingGamesPlayed;
        this.totalSpeakingGamesPlayed = totalSpeakingGamesPlayed;
        this.totalWritingGamesPlayed = totalWritingGamesPlayed;
        this.currentStreakInDays = currentStreakInDays;
        this.lastPlayedDate = lastPlayedDate;
    }
}
