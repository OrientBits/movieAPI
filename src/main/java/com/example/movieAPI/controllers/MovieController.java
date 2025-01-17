package com.example.movieAPI.controllers;

import com.example.movieAPI.dto.MovieDto;
import com.example.movieAPI.dto.MoviePageResponse;
import com.example.movieAPI.entities.Movie;
import com.example.movieAPI.repositories.MovieRepository;
import com.example.movieAPI.service.MovieService;
import com.example.movieAPI.utils.AppConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/movie")
public class MovieController {

    private final MovieService movieService;
    private final MovieRepository movieRepository;

    public MovieController(MovieService movieService, MovieRepository movieRepository) {
        this.movieService = movieService;
        this.movieRepository = movieRepository;
    }


    @PostMapping("/add-movie")
    public ResponseEntity<MovieDto> addMovieHandler(@RequestPart MultipartFile file,
                                                    @RequestPart String movieDto) throws IOException {
        MovieDto movieDtoObj = convertToMovieDto(movieDto);
        System.out.println("Converted Object: \n"+movieDtoObj.toString());
        return new ResponseEntity<>(movieService.addMovie(movieDtoObj, file), HttpStatus.CREATED);
    }




    @PostMapping("/add-temps")
    public String addMovieHandler(@RequestBody List<MovieDto> movieDtos) {
        for(MovieDto movieDto : movieDtos){
            System.out.println("adding temps: "+movieDto);
            Movie movie = new Movie(
                    null,
                    movieDto.getTitle(),
                    movieDto.getDirector(),
                    movieDto.getStudio(),
                    movieDto.getMovieCast(),
                    movieDto.getReleaseYear(),
                    movieDto.getPoster()
            );
            movieRepository.save(movie);
        }
        return "hello boss";
    }


    @GetMapping("/{movieId}")
    public ResponseEntity<MovieDto> getMovieHandler(@PathVariable Integer movieId){
        return ResponseEntity.ok(movieService.getMovie(movieId));
    }

    @GetMapping("/all")
    public  ResponseEntity<List> getAllMovieHandler(){
        return ResponseEntity.ok(movieService.getAllMovie());
    }

    @PutMapping("/update/{movieId}")
    public ResponseEntity<MovieDto> updateMovieHandler(@PathVariable Integer movieId,
                                                       @RequestPart MultipartFile file,
                                                       @RequestPart String movieDto) throws IOException {
        if(file.isEmpty()){
            file =null;
        }
        MovieDto movieDto1 = convertToMovieDto(movieDto);
        return ResponseEntity.ok(movieService.updateMovie(movieId,movieDto1,file));
    }

    @DeleteMapping("/{movieId}")
    public String deleteMovieHandler(@PathVariable Integer movieId) throws IOException {
        return movieService.deleteMovie(movieId);
    }


    private MovieDto convertToMovieDto(String movieDtoObj) throws JsonProcessingException {
        MovieDto movieDto = new MovieDto();
        ObjectMapper objectMapper = new ObjectMapper();
        MovieDto readValue = objectMapper.readValue(movieDtoObj, MovieDto.class);
        return readValue;
    }


    @GetMapping("/allMoviePage")
    public ResponseEntity<MoviePageResponse> getMovieWithPagination(
            @RequestParam(defaultValue = AppConstants.PAGE_number, required = false) Integer pageNumber,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize){

        return ResponseEntity.ok(movieService.getAllMoviesWithPagination(pageNumber,pageSize));

    }



    @GetMapping("/allMoviePageSort")
    public ResponseEntity<MoviePageResponse> getMovieWithPaginationAndSorting(
            @RequestParam(defaultValue = AppConstants.PAGE_number, required = false) Integer pageNumber,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(defaultValue = AppConstants.SORT_BY,required = false) String sortBy,
            @RequestParam(defaultValue = AppConstants.SORT_DIR, required = false) String dir){

        return ResponseEntity.ok(movieService.getAllMoviesWithPaginationAndSorting(pageNumber,pageSize,sortBy,dir));

    }




}
