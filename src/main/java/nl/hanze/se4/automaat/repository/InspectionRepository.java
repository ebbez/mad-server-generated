package nl.hanze.se4.automaat.repository;

import java.util.List;
import java.util.Optional;
import nl.hanze.se4.automaat.domain.Inspection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Inspection entity.
 */
@Repository
public interface InspectionRepository extends JpaRepository<Inspection, Long>, JpaSpecificationExecutor<Inspection> {
    default Optional<Inspection> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Inspection> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Inspection> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select inspection from Inspection inspection left join fetch inspection.car left join fetch inspection.employee",
        countQuery = "select count(inspection) from Inspection inspection"
    )
    Page<Inspection> findAllWithToOneRelationships(Pageable pageable);

    @Query("select inspection from Inspection inspection left join fetch inspection.car left join fetch inspection.employee")
    List<Inspection> findAllWithToOneRelationships();

    @Query(
        "select inspection from Inspection inspection left join fetch inspection.car left join fetch inspection.employee where inspection.id =:id"
    )
    Optional<Inspection> findOneWithToOneRelationships(@Param("id") Long id);
}
