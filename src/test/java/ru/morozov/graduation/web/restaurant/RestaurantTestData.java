package ru.morozov.graduation.web.restaurant;

import ru.morozov.graduation.model.Restaurant;
import ru.morozov.graduation.to.RestaurantTo;
import ru.morozov.graduation.web.MatcherFactory;

import java.util.List;

public class RestaurantTestData {
    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class, "menuSet", "votes", "dishes");
    public static final MatcherFactory.Matcher<RestaurantTo> RESTAURANT_TO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(RestaurantTo.class, "todayMenu");

    public static final int RESTAURANT1_ID = 1;
    public static final int NOT_FOUND = 100;

    public static final Restaurant restaurant1 = new Restaurant(RESTAURANT1_ID, "Столовая №1");
    public static final Restaurant restaurant2 = new Restaurant(RESTAURANT1_ID+1, "Kofeman");
    public static final Restaurant restaurant3 = new Restaurant(RESTAURANT1_ID+2, "Sushisun");
    public static final Restaurant restaurant4 = new Restaurant(RESTAURANT1_ID+3, "Steakballs");
    public static final Restaurant restaurant5 = new Restaurant(RESTAURANT1_ID+4, "Чебургер");
    public static final List<Restaurant> restaurants = List.of(restaurant2, restaurant4, restaurant3, restaurant1, restaurant5);

    public static Restaurant getNew() {
        return new Restaurant(null, "Новый ресторан");
    }

    public static Restaurant getUpdated() {
        return new Restaurant(RESTAURANT1_ID, "Обновленная Столовая №1");
    }
}
