package nl.qnh.qforce.repository;

import nl.qnh.qforce.domain.AnalyticsEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnalyticsRepository extends JpaRepository<AnalyticsEntry, Long> {
}
