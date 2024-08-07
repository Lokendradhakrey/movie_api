package com.sudarsanShah.movieApi.services;

import com.sudarsanShah.movieApi.entities.Movie;
import com.sudarsanShah.movieApi.repositories.MovieRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MovieServiceTest {

    @Autowired
    private MovieRepo movieRepo;

    @Test
    void getMovie() {
        Movie actualRes = movieRepo.findById(1).orElseThrow(() -> new UsernameNotFoundException("Movie not found"));
    }
}