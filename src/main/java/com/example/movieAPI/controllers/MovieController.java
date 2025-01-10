package com.example.movieAPI.controllers;

import com.example.movieAPI.dto.MovieDto;
import com.example.movieAPI.service.MovieService;
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

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }


    @PostMapping("/add-movie")
    public ResponseEntity<MovieDto> addMovieHandler(@RequestPart MultipartFile file,
                                                    @RequestPart String movieDto) throws IOException {
        MovieDto movieDtoObj = convertToMovieDto(movieDto);
        System.out.println("Converted Object: \n"+movieDtoObj.toString());
        return new ResponseEntity<>(movieService.addMovie(movieDtoObj, file), HttpStatus.CREATED);
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
        if(file.isEmpty()) file =null;
        MovieDto movieDto1 = convertToMovieDto(movieDto);
        return ResponseEntity.ok(movieService.updateMovie(movieId,movieDto1,file));
    }

    @DeleteMapping("/movieId")
    public String deleteMovieHandler(@PathVariable Integer movieId) throws IOException {
        return movieService.deleteMovie(movieId);
        
    }


    private MovieDto convertToMovieDto(String movieDtoObj) throws JsonProcessingException {
        MovieDto movieDto = new MovieDto();
        ObjectMapper objectMapper = new ObjectMapper();
        MovieDto readValue = objectMapper.readValue(movieDtoObj, MovieDto.class);
        return readValue;
    }




}
