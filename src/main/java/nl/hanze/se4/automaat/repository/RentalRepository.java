package nl.hanze.se4.automaat.repository;

import java.util.List;
import java.util.Optional;
import nl.hanze.se4.automaat.domain.Rental;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Rental entity.
 */
@Repository
public interface RentalRepository extends JpaRepository<Rental, Long>, JpaSpecificationExecutor<Rental> {
    default Optional<Rental> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Rental> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Rental> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select rental from Rental rental left join fetch rental.customer left join fetch rental.car",
        countQuery = "select count(rental) from Rental rental"
    )
    Page<Rental> findAllWithToOneRelationships(Pageable pageable);

    @Query("select rental from Rental rental left join fetch rental.customer left join fetch rental.car")
    List<Rental> findAllWithToOneRelationships();

    @Query("select rental from Rental rental left join fetch rental.customer left join fetch rental.car where rental.id =:id")
    Optional<Rental> findOneWithToOneRelationships(@Param("id") Long id);
}
