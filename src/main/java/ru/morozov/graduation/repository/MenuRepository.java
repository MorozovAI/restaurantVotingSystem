package ru.morozov.graduation.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.morozov.graduation.model.Menu;

import java.time.LocalDate;
import java.util.List;

import static ru.morozov.graduation.util.validation.ValidationUtil.checkExisted;

@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public interface MenuRepository extends BaseRepository<Menu>{

    @Query("SELECT m FROM Menu m WHERE m.restaurant.id=?1")
    List<Menu> getAll(int restaurantId);

    @EntityGraph(attributePaths = {"dishes","restaurant" }, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m FROM Menu m  WHERE m.menuDate=?1 AND m.restaurant.id=?2")
    Menu getByDate(LocalDate localDate, int restaurantId);
}
