package nl.qnh.qforce.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import nl.qnh.qforce.domain.Analytic;
import nl.qnh.qforce.repositories.AnalyticRepository;

@Service
public class AnalyticService {
    
    private final AnalyticRepository analyticRepository;

    @Autowired
    public AnalyticService(AnalyticRepository analyticRepository) {
        this.analyticRepository = analyticRepository;
    }

    /**
     * Function to save a new analytics record
     * @param request
     * @param statusCode
     * @return 
     */
    public Analytic createAnalytic(HttpServletRequest request, HttpStatusCode statusCode) {

        String urlPath = getUrlPath(request);
        String ipAddress = getClientIp(request);
        String userAgent = request.getHeader("User-Agent");

        Analytic analytic = new Analytic();
        analytic.setDateTime(LocalDateTime.now());
        analytic.setUrlPath(urlPath);
        analytic.setIpAddress(ipAddress);
        analytic.setStatusCode(statusCode.value());
        analytic.setUserAgent(userAgent);
        analyticRepository.save(analytic);

        System.out.println(analytic.toString());
        return analytic;
    }

    /**
     * Function to retreive all analytics
     * @return
     */
    public List<Analytic> getAnalytics() {

        List<Analytic> analytics = analyticRepository.findAll();

        for (Analytic analytic : analytics) {
            System.out.println(analytic.toString());
        }
        
        return analytics;
    }

    /**
     * Function get the client IP
     * @param request
     * @return
     */
    private String getClientIp(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");

        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = request.getRemoteAddr();
        }

        // Handle multiple IP addresses in X-Forwarded-For
        if (ipAddress != null && ipAddress.contains(",")) {
            ipAddress = ipAddress.split(",")[0].trim();
        }

        return ipAddress;
    }

    /**
     * Function to get the url path with the query parameters
     * @param request
     * @return
     */
    public String getUrlPath(HttpServletRequest request) {
        
        String contextPath = request.getContextPath();
        String servletPath = request.getServletPath();
        String queryString = request.getQueryString(); 

        StringBuilder urlPath = new StringBuilder();
        urlPath.append(contextPath).append(servletPath);
        if (queryString != null) {
            urlPath.append("?").append(queryString);
        }

        return urlPath.toString();
    }

}
