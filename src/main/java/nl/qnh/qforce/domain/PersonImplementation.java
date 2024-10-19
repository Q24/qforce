package nl.qnh.qforce.domain;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import nl.qnh.qforce.deserializers.GenderDeserializer;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PersonImplementation implements Person {
    
    private Long id; 

    private String name;

    @JsonProperty("birth_year")
    @JsonAlias({"birthYear"})
    private String birthYear;

    @JsonDeserialize(using = GenderDeserializer.class)
    private Gender gender;

    private Integer height;

    @JsonAlias({"mass"})
    private Integer weight;
    
    @JsonAlias("films")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<String> movieUrls;

    private List<Movie> movies;

    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    
    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getBirthYear() {
        return birthYear;
    }
    public void setBirthYear(String birthYear) {
        this.birthYear = birthYear;
    }

    @Override
    public Gender getGender() {
        return gender;
    }
    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @Override
    public Integer getHeight() {
        return height;
    }
    public void setHeight(Integer height) {
        this.height = height;
    }

    @Override
    public Integer getWeight() {
        return weight;
    }
    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    @Override
    public List<Movie> getMovies() {
        List<Movie> movieList = new ArrayList<>();
        movieList.addAll(movies);
        return movieList;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public List<String> getMovieUrls() {
        return movieUrls;
    }

    public void setMovieUrls(List<String> movieUrls) {
        this.movieUrls = movieUrls;
    }
 
}