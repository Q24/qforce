package nl.qnh.qforce.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;

/**
 * Analytics are collected in the form of the natural language pattern actor-verb-object statements (simplified from
 * xAPI).
 * <br/><br/>
 * The {@link #actor} can be an entity of sorts. In this application it will be the url (actor) that yields (verb) some
 * result (object).
 */
@Entity
@Table(name = "analytics")
public class AnalyticsEntry extends BaseDbEntity {

    @Column(nullable = false)
    private String actor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AnalyticsVerb verb;

    private String object;

    public AnalyticsEntry() {
    }

    public AnalyticsEntry(String actor, AnalyticsVerb verb, String object) {
        this.actor = actor;
        this.verb = verb;
        this.object = object;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public AnalyticsVerb getVerb() {
        return verb;
    }

    public void setVerb(AnalyticsVerb verb) {
        this.verb = verb;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof AnalyticsEntry)) return false;

        AnalyticsEntry that = (AnalyticsEntry) o;

        return new EqualsBuilder()
                .append(actor, that.actor)
                .append(verb, that.verb)
                .append(object, that.object)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(actor)
                .append(verb)
                .append(object)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("actor", actor)
                .append("verb", verb)
                .append("object", object)
                .toString();
    }
}
