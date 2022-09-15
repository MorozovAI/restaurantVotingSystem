package ru.morozov.graduation.util;

import ru.morozov.graduation.model.Menu;
import ru.morozov.graduation.model.Restaurant;
import ru.morozov.graduation.to.RestaurantTo;

import java.util.List;
import java.util.stream.Collectors;

public class RestaurantUtil {

    public static List<RestaurantTo> getTos(List<Restaurant> restaurants) {
        return restaurants.stream()
                .map(RestaurantUtil::getTo)
                .collect(Collectors.toList());
    }

    public static RestaurantTo getTo(Restaurant r) {
        Menu menu = r.getMenuSet() != null ? r.getMenuSet()
                .stream()
                .findFirst()
                .orElse(null) : null;
        return new RestaurantTo(r.id(), r.getName(), menu);
    }
}