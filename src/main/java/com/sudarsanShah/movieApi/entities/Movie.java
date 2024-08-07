package com.sudarsanShah.movieApi.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "movies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    @NotBlank(message = "Please enter the title.")
    private String title;
    @Column(nullable = false)
    @NotBlank(message = "Please enter the director.")
    private String director;
    @Column(nullable = false)
    @NotBlank(message = "Please enter the studio.")
    private String studio;
    @ElementCollection
    @CollectionTable(name = "movie_cast")
    private Set<String> movieCast;

    private Integer releaseYear;
    @Column(nullable = false)
    @NotBlank(message = "Please enter the poster.")
    private String poster;
}
