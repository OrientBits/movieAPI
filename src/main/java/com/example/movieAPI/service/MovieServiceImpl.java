package com.example.movieAPI.service;

import com.example.movieAPI.dto.MovieDto;
import com.example.movieAPI.entities.Movie;
import com.example.movieAPI.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final FileService fileService;

    @Value("${project.poster}")
    private String path;

    @Value("${base.url}")
    private String baseUrl;

    public MovieServiceImpl(MovieRepository movieRepository, FileService fileService) {
        this.movieRepository = movieRepository;
        this.fileService = fileService;
    }


    @Override
    public MovieDto addMovie(MovieDto movieDto, MultipartFile file) throws IOException {
        //1. upload the file

        if(Files.exists(Paths.get(path+ File.separator+file.getOriginalFilename()))){
            throw new RuntimeException("File is already exists! Please enter another file name");
        }
        String uploadedFileName = fileService.uploadFile(path, file);

        //2. set the value of field 'poster' as fileName
        movieDto.setPoster(uploadedFileName);

        //3. map dto to Movie Object
        Movie movie = new Movie(
                null,
                movieDto.getTitle(),
                movieDto.getDirector(),
                movieDto.getStudio(),
                movieDto.getMovieCast(),
                movieDto.getReleaseYear(),
                movieDto.getPoster()
                );
        //4. save the movie object

        System.out.println("Before saving the Data: "+movie.toString());
        Movie savedMovie = movieRepository.save(movie);

        //5. generate the poster url
        String posterUrl = baseUrl + "/file/" + uploadedFileName;

        //6. map Movie object to DTO object and return it.
        MovieDto response = new MovieDto(
                savedMovie.getMovieId(),
                savedMovie.getTitle(),
                savedMovie.getDirector(),
                savedMovie.getStudio(),
                savedMovie.getMovieCast(),
                savedMovie.getReleaseYear(),
                savedMovie.getPoster(),
                posterUrl
        );


        return response;
    }

    @Override
    public MovieDto getMovie(Integer movieID) {
        Movie byId = movieRepository.findById(movieID).orElseThrow(()->new RuntimeException("Movie not found"));
        String posterUrl = baseUrl + "/file/" + byId.getPoster();

        MovieDto response = new MovieDto(
                byId.getMovieId(),
                byId.getTitle(),
                byId.getDirector(),
                byId.getStudio(),
                byId.getMovieCast(),
                byId.getReleaseYear(),
                byId.getPoster(),
                posterUrl
        );

        return response;
    }

    @Override
    public List<MovieDto> getAllMovie() {
        List<Movie> movieList = movieRepository.findAll();

        List<MovieDto> movieDtos = new ArrayList<>();

        for(Movie m: movieList){
            String posterUrl = baseUrl + "/file/" + m.getPoster();
            MovieDto dto = new MovieDto(
                    m.getMovieId(),
                    m.getTitle(),
                    m.getDirector(),
                    m.getStudio(),
                    m.getMovieCast(),
                    m.getReleaseYear(),
                    m.getPoster(),
                    posterUrl
            );
            movieDtos.add(dto);

        }

        return movieDtos;
    }

    @Override
    public MovieDto updateMovie(Integer movieId, MovieDto movieDto, MultipartFile file) throws IOException {

        Movie movie = movieRepository.findById(movieId).orElseThrow(
                () -> new RuntimeException("Movie with id: " + movieId + " doesn't exist"));
        //check if the movie is already exist or not
        //if file is null, do nothing
        //if file is not null, then delete existing file associated with the record
        // and upload new file
        String fileName = movie.getPoster();
        if (file !=null){
            Files.deleteIfExists(Paths.get(path+File.separator+fileName));
            fileName = fileService.uploadFile(path,file);
        }
       movieDto.setPoster(fileName);
        //map it to Movie object

        Movie updatedMovie = new Movie(
                movie.getMovieId(),
                movieDto.getTitle(),
                movieDto.getStudio(),
                movieDto.getDirector(),
                movieDto.getMovieCast(),
                movieDto.getReleaseYear(),
                movieDto.getPoster()
        );
        //save the movie object -> return the object
        Movie saved = movieRepository.save(updatedMovie);

        //map to movieDto and return it.
        String posterUrl = baseUrl + "/file/" + saved.getPoster();

        MovieDto response = new MovieDto(
                saved.getMovieId(),
                saved.getTitle(),
                saved.getDirector(),
                saved.getStudio(),
                saved.getMovieCast(),
                saved.getReleaseYear(),
                saved.getPoster(),
                posterUrl
        );
        return response;
    }


    @Override
    public String deleteMovie(Integer movieId) throws IOException {

        System.out.println("inside delete movie service.....");
        Movie movie = movieRepository.findById(movieId).orElseThrow(
                () -> new RuntimeException("Movie with id: " + movieId + " doesn't exist"));

        Files.deleteIfExists(Paths.get(path+File.separator+movie.getPoster()));
        System.out.println("after file delete movie service.....");

        movieRepository.deleteById(movieId);

        return "Movie deleted with ID: "+movieId;
    }

}
