package nl.qnh.qforce.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import nl.qnh.qforce.domain.Analytic;

@Repository
public interface AnalyticRepository extends JpaRepository<Analytic, Long> {
    
}