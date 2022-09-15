package ru.morozov.graduation.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.morozov.graduation.model.Restaurant;

import java.util.List;

import static ru.morozov.graduation.util.validation.ValidationUtil.checkExisted;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {

    @EntityGraph(attributePaths = {"menuSet"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT r FROM Restaurant r LEFT JOIN Menu m ON r.id=m.restaurant.id WHERE m.menuDate = CURRENT_DATE OR m.menuDate IS NULL ORDER BY r.name")
    List<Restaurant> getAllWithTodayMenu();

    @EntityGraph(attributePaths = {"menuSet"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT r FROM Restaurant r LEFT JOIN Menu m ON r.id=m.restaurant.id WHERE r.id=?1 AND (m.menuDate = CURRENT_DATE OR m.menuDate IS NULL) ORDER BY r.name")
    Restaurant getWithTodayMenu(int id);

    default Restaurant getExistedWithTodayMenu(int id) {
        return checkExisted(getWithTodayMenu(id), id);
    }
}
