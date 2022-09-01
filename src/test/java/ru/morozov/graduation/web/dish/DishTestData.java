package ru.morozov.graduation.web.dish;

import ru.morozov.graduation.model.Dish;
import ru.morozov.graduation.model.Menu;
import ru.morozov.graduation.model.Restaurant;
import ru.morozov.graduation.to.RestaurantTo;
import ru.morozov.graduation.web.MatcherFactory;

import java.time.LocalDate;

public class DishTestData {
    public static final MatcherFactory.Matcher<Dish> DISH_MATCHER = MatcherFactory.usingEqualsComparator(Dish.class);

    public static final int DISH1_ID = 1;
    public static final int NOT_FOUND = 100;
    public static final Dish dish1 = new Dish(1, "Чай", 99.99);

    public static Dish getNew() {
        return new Dish(null, "Новое блюдо", 99.98);
    }

    public static Dish getUpdated() {
        return new Dish(DISH1_ID, "Обновленное блюдо", 102);
    }
}
