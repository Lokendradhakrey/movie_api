package com.sudarsanShah.movieApi.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sudarsanShah.movieApi.payloads.MovieDto;
import com.sudarsanShah.movieApi.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/movies")
public class MovieController {
    @Autowired
    private MovieService movieService;

    @PostMapping("/add-movie")
    public ResponseEntity<MovieDto> addMovie(@RequestPart MultipartFile file, @RequestPart String movieDto) throws IOException {
        MovieDto addedMovie = this.movieService.addMovie(convertStringToMovieDto(movieDto), file);
        return new ResponseEntity<>(addedMovie, HttpStatus.CREATED);
    }

    private MovieDto convertStringToMovieDto(String movieDto) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(movieDto, MovieDto.class);
    }

    @GetMapping("/{movieId}")
    public ResponseEntity<MovieDto> getMovie(@PathVariable Integer movieId) {
        return ResponseEntity.ok(this.movieService.getMovie(movieId));
    }

    @GetMapping("/")
    public ResponseEntity<List<MovieDto>> getAllMovies() {
        return ResponseEntity.ok(this.movieService.getAllMovies());
    }
}
