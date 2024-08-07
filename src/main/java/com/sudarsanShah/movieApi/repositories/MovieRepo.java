package com.sudarsanShah.movieApi.repositories;

import com.sudarsanShah.movieApi.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepo extends JpaRepository<Movie, Integer> {
}
