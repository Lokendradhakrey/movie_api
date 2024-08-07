package com.sudarsanShah.movieApi.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sudarsanShah.movieApi.exceptions.EmptyFileException;
import com.sudarsanShah.movieApi.payloads.MovieDto;
import com.sudarsanShah.movieApi.payloads.MoviePageResponse;
import com.sudarsanShah.movieApi.services.MovieService;
import com.sudarsanShah.movieApi.utilies.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/movies")
public class MovieController {
    @Autowired
    private MovieService movieService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/add-movie")
    public ResponseEntity<MovieDto> addMovie(@RequestPart MultipartFile file, @RequestPart String movieDto) throws IOException {
        if (file.isEmpty()){
            throw new EmptyFileException("File is empty. please upload other file.");
        }
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

    @GetMapping("/list-of-movies")
    public ResponseEntity<List<MovieDto>> getAllMovies() {
        return ResponseEntity.ok(this.movieService.getAllMovies());
    }

    @GetMapping("/allMoviesPage")
    public ResponseEntity<MoviePageResponse> getMoviesWithPagination(
        @RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
        @RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize

    ){
        return ResponseEntity.ok(this.movieService.getAllMoviesWithPagination(pageNumber, pageSize));
    }

    @GetMapping("/allMoviesPageSort")
    public ResponseEntity<MoviePageResponse> getMoviesWithPaginationAndSorting(
            @RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(defaultValue = AppConstants.SORT_DIR, required = false) String dir
    ){
        return ResponseEntity.ok(this.movieService.getAllMoviesWithPaginationAndSorting(pageNumber, pageSize, sortBy, dir));
    }

    @PutMapping("/update/{movieId}")
    public ResponseEntity<MovieDto> updateMovie(
            @PathVariable Integer movieId,
            @RequestPart String movieDto,
            @RequestPart MultipartFile file
    ) throws IOException {
        if (file.isEmpty()) file = null;
        return ResponseEntity.ok(this.movieService.updateMovie(movieId, convertStringToMovieDto(movieDto), file));

    }

    @DeleteMapping("/delete/{movieId}")
    public ResponseEntity<String> deleteMovie(@PathVariable Integer movieId) throws IOException {
        return ResponseEntity.ok(this.movieService.deleteMovie(movieId));
    }
}
