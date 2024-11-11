package nl.hanze.se4.automaat.repository;

import nl.hanze.se4.automaat.domain.RouteStop;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the RouteStop entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RouteStopRepository extends JpaRepository<RouteStop, Long> {}
