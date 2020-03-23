package nl.qnh.qforce.domain;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static nl.qnh.qforce.domain.Gender.*;

/**
 * This deserializer implementation based on the {@link JsonDeserializer} provides us with the deserialize
 * method. Providing a custom deserializer seems a good option here because we need some flexibility to map the
 * {@link PersonImpl}, so move those concerns here.
 * <br/>
 * This class reads the required fields from the received JSON object and process values further if necessary:
 * <ul>
 *     <li>Integer types will be cleaned from thousand separators, then parsed as Double to retain decimals and finally
 *     rounded to closest whole number.</li>
 *     <li>Gender is expected to always map onto a {@link Gender} value. If no mapping can be found, an error will be logged and the field will
 *     not show up for the requesting user.</li>
 * </ul>
 */
public class PersonDeserializer extends JsonDeserializer<PersonImpl> {

    private static final Logger LOG = LoggerFactory.getLogger(PersonDeserializer.class);

    private static final String JSON_FIELD__NAME = "name";
    private static final String JSON_FIELD__BIRTH_YEAR = "birth_year";
    private static final String JSON_FIELD__HEIGHT = "height";
    private static final String JSON_FIELD__MASS = "mass";
    private static final String JSON_FIELD__URL = "url";
    private static final String JSON_FIELD__GENDER = "gender";
    private static final String JSON_FIELD__FILMS = "films";

    private static final String THOUSANDS_SEPARATOR = ",";

    @Override
    public PersonImpl deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

        JsonNode node = p.getCodec().readTree(p);

        if (LOG.isDebugEnabled())
            LOG.debug("Deserializing JsonNode: " + node);

        final long id = getId(node);

        final String name = getStringValue(node, JSON_FIELD__NAME).orElse(null);
        final String birthYear = getStringValue(node, JSON_FIELD__BIRTH_YEAR).orElse(null);

        final Gender gender = getGender(node);

        final Integer height = parseInteger(node, JSON_FIELD__HEIGHT);
        final Integer weight = parseInteger(node, JSON_FIELD__MASS);

        final List<String> movieUrls = getMovieUrls(node);

        return new PersonImpl(id, name, birthYear, gender, height, weight, movieUrls);
    }

    private Long getId(JsonNode node) {
        return getStringValue(node, JSON_FIELD__URL)
                .map(text -> {
                    final String[] split = text.split("/");
                    return Long.parseLong(split[split.length - 1]);
                })
                .orElse(0L);
    }

    private Optional<String> getStringValue(JsonNode node, String field) {
        return Optional.ofNullable(node.get(field)).map(JsonNode::asText);
    }

    private Integer parseInteger(JsonNode node, String field) {
        return getStringValue(node, field)
                .map(text -> {
                    final String sanitizedNumberString = text.replaceAll(THOUSANDS_SEPARATOR, "");
                    try {
                        return (int) Math.round(Double.parseDouble(sanitizedNumberString));
                    } catch (NumberFormatException e) {
                        LOG.error("Cannot parse number with value: \"{}\"", text);
                        return null;
                    }
                })
                .orElse(null);
    }

    private Gender getGender(JsonNode node) {

        final String gender = getStringValue(node, JSON_FIELD__GENDER).orElse("");

        return switch (gender) {
            case "male" -> MALE;
            case "female" -> FEMALE;
            case "n/a" -> NOT_APPLICABLE;
            case "unknown" -> UNKNOWN;
            default -> {
                LOG.error("Non fatal error: Unsupported gender type. Cannot translate value \"{}\" to enum. ", gender);
                yield null;
            }
        };
    }

    private List<String> getMovieUrls(JsonNode node) {

        final List<String> movieUrls = new ArrayList<>();

        node.withArray(JSON_FIELD__FILMS)
                .elements()
                .forEachRemaining(jsonNode -> movieUrls.add(jsonNode.textValue()));

        return movieUrls;
    }
}
