package nl.hanze.se4.automaat.repository;

import java.util.List;
import java.util.Optional;
import nl.hanze.se4.automaat.domain.Repair;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Repair entity.
 */
@Repository
public interface RepairRepository extends JpaRepository<Repair, Long> {
    default Optional<Repair> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Repair> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Repair> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select repair from Repair repair left join fetch repair.car left join fetch repair.employee",
        countQuery = "select count(repair) from Repair repair"
    )
    Page<Repair> findAllWithToOneRelationships(Pageable pageable);

    @Query("select repair from Repair repair left join fetch repair.car left join fetch repair.employee")
    List<Repair> findAllWithToOneRelationships();

    @Query("select repair from Repair repair left join fetch repair.car left join fetch repair.employee where repair.id =:id")
    Optional<Repair> findOneWithToOneRelationships(@Param("id") Long id);
}
