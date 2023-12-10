package com.elevate.api.game;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    @Query("SELECT count(g) FROM Game g WHERE g.id = :id")
    Long checkExisting(@Param("id") long id);

    Optional<Game> findById(long id);
}
