package nl.hanze.se4.automaat.repository;

import java.util.List;
import java.util.Optional;
import nl.hanze.se4.automaat.domain.CarReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CarReview entity.
 */
@Repository
public interface CarReviewRepository extends JpaRepository<CarReview, Long> {
    default Optional<CarReview> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<CarReview> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<CarReview> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select carReview from CarReview carReview left join fetch carReview.customer left join fetch carReview.car",
        countQuery = "select count(carReview) from CarReview carReview"
    )
    Page<CarReview> findAllWithToOneRelationships(Pageable pageable);

    @Query("select carReview from CarReview carReview left join fetch carReview.customer left join fetch carReview.car")
    List<CarReview> findAllWithToOneRelationships();

    @Query(
        "select carReview from CarReview carReview left join fetch carReview.customer left join fetch carReview.car where carReview.id =:id"
    )
    Optional<CarReview> findOneWithToOneRelationships(@Param("id") Long id);
}
