package nl.hanze.se4.automaat.repository;

import java.util.Optional;
import nl.hanze.se4.automaat.domain.Customer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Customer entity.
 */
@Repository
public interface AMCustomerRepository extends CustomerRepository {
    @Query("select customer from Customer customer left join fetch customer.systemUser user where user.id =:id")
    Optional<Customer> findOneWithUserId(@Param("id") Long id);
}
