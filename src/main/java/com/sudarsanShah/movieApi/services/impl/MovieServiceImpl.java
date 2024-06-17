package com.sudarsanShah.movieApi.services.impl;

import com.sudarsanShah.movieApi.entities.Movie;
import com.sudarsanShah.movieApi.payloads.MovieDto;
import com.sudarsanShah.movieApi.repositories.MovieRepo;
import com.sudarsanShah.movieApi.services.FileService;
import com.sudarsanShah.movieApi.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieServiceImpl implements MovieService {
    @Autowired
    private FileService fileService;

    @Autowired
    private MovieRepo movieRepo;

    @Value("${project.poster}")
    private String path;

    @Value("${base.url}")
    private String baseUrl;

    @Override
    public MovieDto addMovie(MovieDto movieDto, MultipartFile file) throws IOException {
        // upload file
        String uploadedFileName = this.fileService.uploadFile(path, file);
        // set file name as poster
        movieDto.setPoster(uploadedFileName);
        // map movieDto to movie
        Movie movie = new Movie(
                movieDto.getId(),
                movieDto.getTitle(),
                movieDto.getDirector(),
                movieDto.getStudio(),
                movieDto.getMovieCast(),
                movieDto.getReleaseYear(),
                movieDto.getPoster()
        );
        // save movie obj
        Movie savedMovie = this.movieRepo.save(movie);
        // generate poster url
        String posterUrl = baseUrl + "/file/" + uploadedFileName;
        // map movie to movieDto
        MovieDto addedMovie = new MovieDto(
                savedMovie.getId(),
                savedMovie.getTitle(),
                savedMovie.getDirector(),
                savedMovie.getStudio(),
                savedMovie.getMovieCast(),
                savedMovie.getReleaseYear(),
                savedMovie.getPoster(),
                posterUrl
        );
        // return added movie
        return addedMovie;

    }

    @Override
    public MovieDto getMovie(Integer movieId) {
        Movie movie = this.movieRepo.findById(movieId).orElseThrow(() -> new RuntimeException("Movie not found"));
        String posterUrl = baseUrl + "/file/" + movie.getPoster();
        MovieDto movieDto = new MovieDto(
                movie.getId(),
                movie.getTitle(),
                movie.getDirector(),
                movie.getStudio(),
                movie.getMovieCast(),
                movie.getReleaseYear(),
                movie.getPoster(),
                posterUrl
        );
        return movieDto;
    }

    @Override
    public List<MovieDto> getAllMovies() {
        List<Movie> movies = this.movieRepo.findAll();
        List<MovieDto> movieDtos = new ArrayList<>();
        for (Movie movie:movies){
            String posterUrl = baseUrl + "/file/" + movie.getPoster();
            MovieDto movieDto = new MovieDto(
                    movie.getId(),
                    movie.getTitle(),
                    movie.getDirector(),
                    movie.getStudio(),
                    movie.getMovieCast(),
                    movie.getReleaseYear(),
                    movie.getPoster(),
                    posterUrl
            );
            movieDtos.add(movieDto);
        }

        return movieDtos;
    }
}
