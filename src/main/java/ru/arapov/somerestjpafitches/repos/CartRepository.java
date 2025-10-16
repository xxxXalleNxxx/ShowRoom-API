package ru.arapov.somerestjpafitches.repos;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.arapov.somerestjpafitches.models.Cart;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    @EntityGraph(attributePaths = {"items", "items.item"})
    @Query("SELECT i FROM Cart i WHERE i.user.id = :userId")
    Optional<Cart> findByUserId(@Param("userId") Long userId);
}
