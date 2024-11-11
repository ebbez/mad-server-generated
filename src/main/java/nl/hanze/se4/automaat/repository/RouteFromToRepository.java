package nl.hanze.se4.automaat.repository;

import java.util.List;
import java.util.Optional;
import nl.hanze.se4.automaat.domain.RouteFromTo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the RouteFromTo entity.
 */
@Repository
public interface RouteFromToRepository extends JpaRepository<RouteFromTo, Long> {
    default Optional<RouteFromTo> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<RouteFromTo> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<RouteFromTo> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select routeFromTo from RouteFromTo routeFromTo left join fetch routeFromTo.employee",
        countQuery = "select count(routeFromTo) from RouteFromTo routeFromTo"
    )
    Page<RouteFromTo> findAllWithToOneRelationships(Pageable pageable);

    @Query("select routeFromTo from RouteFromTo routeFromTo left join fetch routeFromTo.employee")
    List<RouteFromTo> findAllWithToOneRelationships();

    @Query("select routeFromTo from RouteFromTo routeFromTo left join fetch routeFromTo.employee where routeFromTo.id =:id")
    Optional<RouteFromTo> findOneWithToOneRelationships(@Param("id") Long id);
}
