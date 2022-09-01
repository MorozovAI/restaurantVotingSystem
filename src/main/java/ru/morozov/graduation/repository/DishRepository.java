package ru.morozov.graduation.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.morozov.graduation.model.Dish;

import java.util.List;

@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public interface DishRepository extends BaseRepository<Dish> {

    @EntityGraph(attributePaths = {"restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT d FROM Dish d")
    List<Dish> getAll();

    @Query("SELECT d FROM Dish d JOIN FETCH d.restaurant WHERE d.restaurant.id = ?1")
    List<Dish> getAllByRestaurant(int restaurantId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Dish d WHERE d.restaurant.id=:id ")
    int deleteByRestaurant_Id(int id);
}
