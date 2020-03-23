package nl.qnh.qforce.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import nl.qnh.qforce.service.PersonService;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

/**
 * Implementation of the {@link Person} interface.
 * <br/><br/>
 * {@link #movies} are populated by the {@link PersonService} after the movie urls are collected by
 * {@link PersonDeserializer} in {@link #movieUrls}
 * <br/><br/>
 * Remarks:
 * <ul>
 *     <li>{@link #movieUrls} field is ignored by Jackson, end users don't care about this</li>
 *     <li>{@link #getGender} null values (unparsable) are ignored by Jackson during serialization</li>
 * </ul>
 */
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonDeserialize(using = PersonDeserializer.class)
public class PersonImpl implements Person {

    private long id;
    private String name;
    private String birthYear;
    private Gender gender;
    private Integer height;
    private Integer weight;
    private List<Movie> movies;

    @JsonIgnore
    private List<String> movieUrls;

    public PersonImpl() {
    }

    public PersonImpl(long id, String name, String birthYear, Gender gender, Integer height, Integer weight, List<String> movieUrls) {
        this.id = id;
        this.name = name;
        this.birthYear = birthYear;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
        this.movieUrls = movieUrls;
    }

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

    @JsonInclude(JsonInclude.Include.NON_NULL)
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
        return movies;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof PersonImpl)) return false;

        PersonImpl person = (PersonImpl) o;

        return new EqualsBuilder()
                .append(id, person.id)
                .append(name, person.name)
                .append(birthYear, person.birthYear)
                .append(gender, person.gender)
                .append(height, person.height)
                .append(weight, person.weight)
                .append(movies, person.movies)
                .append(movieUrls, person.movieUrls)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(name)
                .append(birthYear)
                .append(gender)
                .append(height)
                .append(weight)
                .append(movies)
                .append(movieUrls)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("id", id)
                .append("name", name)
                .append("birthYear", birthYear)
                .append("gender", gender)
                .append("height", height)
                .append("weight", weight)
                .append("movies", movies)
                .append("movieUrls", movieUrls)
                .toString();
    }
}
