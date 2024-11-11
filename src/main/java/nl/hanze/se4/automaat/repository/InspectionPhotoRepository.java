package nl.hanze.se4.automaat.repository;

import nl.hanze.se4.automaat.domain.InspectionPhoto;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the InspectionPhoto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InspectionPhotoRepository extends JpaRepository<InspectionPhoto, Long> {}
