package nl.qnh.qforce.config;

import org.hibernate.validator.constraints.URL;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "qforce")
@Validated
public class QforceConfig {

    @URL
    private String swapiFilmsUrl;
    @URL
    private String swapiPeopleUrl;
    @URL
    private String swapiPeopleSearchUrl;

    public QforceConfig() {
    }

    public String getSwapiFilmsUrl() {
        return swapiFilmsUrl;
    }

    public void setSwapiFilmsUrl(String swapiFilmsUrl) {
        this.swapiFilmsUrl = swapiFilmsUrl;
    }

    public String getSwapiPeopleUrl() {
        return swapiPeopleUrl;
    }

    public void setSwapiPeopleUrl(String swapiPeopleUrl) {
        this.swapiPeopleUrl = swapiPeopleUrl;
    }

    public String getSwapiPeopleSearchUrl() {
        return swapiPeopleSearchUrl;
    }

    public void setSwapiPeopleSearchUrl(String swapiPeopleSearchUrl) {
        this.swapiPeopleSearchUrl = swapiPeopleSearchUrl;
    }
}
