package ru.morozov.graduation.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.morozov.graduation.model.Menu;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface MenuRepository extends BaseRepository<Menu> {

    @Query("SELECT m FROM Menu m WHERE m.restaurant.id=?1 ORDER BY m.menuDate")
    List<Menu> getAll(int restaurantId);

    @EntityGraph(attributePaths = {"dishes", "restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m FROM Menu m WHERE m.menuDate=CURRENT_DATE ORDER BY m.menuDate")
    List<Menu> getAllToday();

    @EntityGraph(attributePaths = {"dishes", "restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m FROM Menu m WHERE m.restaurant.id=?1 AND m.menuDate=CURRENT_DATE ")
    Optional<Menu> getToday(int restaurantId);
}
