package nl.qnh.qforce.domain;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Analytic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dateTime;

    private String urlPath;

    private String ipAddress;

    private String userAgent;

    private Integer statusCode;

    @Override
    public String toString() {
        return "Analytic [id=" + id + ", dateTime=" + dateTime + ", urlPath=" + urlPath + ", ipAddress=" + ipAddress
                + ", userAgent=" + userAgent + ", statusCode=" + statusCode + "]";
    }
    
    public long getId() { 
        return id; 
    }
    public void setId(long id) { 
        this.id = id; 
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getUrlPath() { 
        return urlPath; 
    }
    public void setUrlPath(String urlPath) { 
        this.urlPath = urlPath; 
    }

    public String getIpAddress() {
        return ipAddress;
    }
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

}