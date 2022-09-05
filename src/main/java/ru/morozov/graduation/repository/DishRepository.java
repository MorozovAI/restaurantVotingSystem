package ru.morozov.graduation.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.morozov.graduation.model.Dish;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public interface DishRepository extends BaseRepository<Dish> {

    @EntityGraph(attributePaths = {"restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT d FROM Dish d")
    List<Dish> getAll();

    @Query("SELECT d FROM Dish d JOIN FETCH d.restaurant WHERE d.restaurant.id = ?1")
    Set<Dish> getAllByRestaurant(int restaurantId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Dish d WHERE d.restaurant.id=:id ")
    void deleteByRestaurantId(int id);

    @Transactional
    @Modifying
    @Query("UPDATE Dish d SET d.name=?2, d.price=?3 WHERE d.id=?1")
    int update(int id, String name, BigDecimal price);
}
