package ru.morozov.graduation.util;

import ru.morozov.graduation.model.Menu;
import ru.morozov.graduation.model.Restaurant;
import ru.morozov.graduation.to.RestaurantTo;

import java.util.List;
import java.util.stream.Collectors;

public class RestaurantUtil {

    public static List<RestaurantTo> getTos(List<Restaurant> restaurants, List<Menu> todayMenus) {
        return restaurants.stream()
                .map(r -> getTo(r, todayMenus != null ? todayMenus.stream()
                        .filter(m -> m.getRestaurant().id() == r.id())
                        .findFirst()
                        .orElse(null) : null))
                .collect(Collectors.toList());

    }

    public static RestaurantTo getTo(Restaurant r, Menu todayMenu) {
        return new RestaurantTo(r.id(), r.getName(), todayMenu);
    }
}