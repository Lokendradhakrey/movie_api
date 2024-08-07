package com.sudarsanShah.movieApi.services.impl;

import com.sudarsanShah.movieApi.entities.Movie;
import com.sudarsanShah.movieApi.exceptions.FileExistException;
import com.sudarsanShah.movieApi.exceptions.MovieNotFoundException;
import com.sudarsanShah.movieApi.payloads.MovieDto;
import com.sudarsanShah.movieApi.payloads.MoviePageResponse;
import com.sudarsanShah.movieApi.repositories.MovieRepo;
import com.sudarsanShah.movieApi.services.FileService;
import com.sudarsanShah.movieApi.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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
        if (Files.exists(Paths.get(path + File.separator + file.getOriginalFilename()))) {
            throw new FileExistException("File already exists! Please upload other file.");
        }
        // upload file
        String uploadedFileName = this.fileService.uploadFile(path, file);
        // set file name as poster
        movieDto.setPoster(uploadedFileName);
        // map movieDto to movie
        Movie movie = new Movie(
                null,
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
        Movie movie = this.movieRepo.findById(movieId).orElseThrow(() -> new MovieNotFoundException("Movie not found with id " + movieId));
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
        for (Movie movie : movies) {
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

    @Override
    public String deleteMovie(Integer movieId) throws IOException {
        Movie movie = this.movieRepo.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException("Movie not found with id " + movieId));
        Files.deleteIfExists(Paths.get(path + File.separator + movie.getPoster()));
        movieRepo.delete(movie);
        return "Movie has been deleted successfully with movieId: " + movie.getId();
    }

    @Override
    public MovieDto updateMovie(Integer movieId, MovieDto movieDto, MultipartFile file) throws IOException {
        Movie mv = this.movieRepo.findById(movieId).orElseThrow(() -> new MovieNotFoundException("Movie not found with id " + movieId));
        String fileName = mv.getPoster();
        if (file != null) {
            Files.deleteIfExists(Paths.get(path + File.separator + fileName));
            fileName = this.fileService.uploadFile(path, file);
        }
        movieDto.setPoster(fileName);
        Movie movie = new Movie(
                mv.getId(),
                movieDto.getTitle(),
                movieDto.getDirector(),
                movieDto.getStudio(),
                movieDto.getMovieCast(),
                movieDto.getReleaseYear(),
                movieDto.getPoster()
        );
        Movie updatedMovie = this.movieRepo.save(movie);
        String posterUrl = baseUrl + "/file/" + fileName;

        MovieDto response = new MovieDto(
                movie.getId(),
                movie.getTitle(),
                movie.getDirector(),
                movie.getStudio(),
                movie.getMovieCast(),
                movie.getReleaseYear(),
                movie.getPoster(),
                posterUrl
        );
        return response;
    }

    @Override
    public MoviePageResponse getAllMoviesWithPagination(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Movie> moviePages = movieRepo.findAll(pageable);
        List<Movie> movies = moviePages.getContent();
        List<MovieDto> movieDtos = new ArrayList<>();
        for (Movie movie : movies) {
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
        return new MoviePageResponse(movieDtos, pageNumber, pageSize,
                moviePages.getNumberOfElements(), moviePages.getTotalPages(), moviePages.isLast());
    }

    @Override
    public MoviePageResponse getAllMoviesWithPaginationAndSorting(Integer pageNumber, Integer pageSize, String sortBy, String dir) {
        Sort sort = dir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Movie> moviePages = movieRepo.findAll(pageable);
        List<Movie> movies = moviePages.getContent();
        List<MovieDto> movieDtos = new ArrayList<>();
        for (Movie movie : movies) {
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
        return new MoviePageResponse(movieDtos, pageNumber, pageSize,
                moviePages.getNumberOfElements(), moviePages.getTotalPages(), moviePages.isLast());
    }
}
