package ru.morozov.graduation.web.dish;

import ru.morozov.graduation.model.Dish;
import ru.morozov.graduation.web.MatcherFactory;

import java.util.List;
import java.util.Set;

import static ru.morozov.graduation.web.restaurant.RestaurantTestData.restaurant2;

public class DishTestData {
    public static final MatcherFactory.Matcher<Dish> DISH_MATCHER = MatcherFactory.usingEqualsComparator(Dish.class);

    public static final int DISH1_ID = 1;
    public static final int NOT_FOUND = 100;
    public static final Dish dish1 = new Dish(1, "Чай", 99.99);
    public static final Dish dish2 = new Dish(2, "Кофе", 99.99);
    public static final Dish dish3 = new Dish(3, "Макароны с котлетой", 199.99);
    public static final Dish dish4 = new Dish(4, "Картофельное пюре с сосиской", 199.99);
    public static final Dish dish5 = new Dish(5, "Борщ", 299.99);
    public static final Dish dish6 = new Dish(6, "Щи", 299.99);
    public static final Dish dish7 = new Dish(7, "Капучино", 199.99);
    public static final Set<Dish> dishes1 = Set.of(dish5, dish3, dish1);
    public static final Set<Dish> dishes2 = Set.of(dish5, dish3, dish2, dish1);
    public static final Set<Dish> dishes3 = Set.of(dish5, dish3);
    public static final List<Dish> dishes4 = List.of(dish1, dish2, dish3, dish4, dish5, dish6);

    static {
        dish7.setRestaurant(restaurant2);
    }

    public static Dish getNew() {
        return new Dish(null, "Новое блюдо", 99.98);
    }

    public static Dish getUpdated() {
        return new Dish(DISH1_ID, "Обновленное блюдо", 102);
    }
}
