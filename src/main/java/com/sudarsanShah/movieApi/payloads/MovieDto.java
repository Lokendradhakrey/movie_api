package com.sudarsanShah.movieApi.payloads;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieDto {

    private Integer id;
    @NotBlank(message = "Please enter the title.")
    private String title;
    @NotBlank(message = "Please enter the director.")
    private String director;
    @NotBlank(message = "Please enter the studio.")
    private String studio;
    private Set<String> movieCast;

    private Integer releaseYear;
    @NotBlank(message = "Please enter the poster.")
    private String poster;
    @NotBlank(message = "please enter the poster url")
    private String posterUrl;
}
