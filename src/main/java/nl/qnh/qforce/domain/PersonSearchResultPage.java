package nl.qnh.qforce.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to map the search results for persons onto. {@link PersonImpl} already has a custom deserializer,
 * so Jackson can deserialize and map to this class, no annotations needed.
 */
public class PersonSearchResultPage {

    private int count;
    private String next;
    private String previous;
    private List<PersonImpl> results = new ArrayList<>();

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public List<PersonImpl> getResults() {
        return results;
    }

    public void setResults(List<PersonImpl> results) {
        this.results = results;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof PersonSearchResultPage)) return false;

        PersonSearchResultPage that = (PersonSearchResultPage) o;

        return new EqualsBuilder()
                .append(count, that.count)
                .append(next, that.next)
                .append(previous, that.previous)
                .append(results, that.results)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(count)
                .append(next)
                .append(previous)
                .append(results)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("count", count)
                .append("next", next)
                .append("previous", previous)
                .append("results", results)
                .toString();
    }
}

