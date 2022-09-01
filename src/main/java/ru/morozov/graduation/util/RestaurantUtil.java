package ru.morozov.graduation.util;

import ru.morozov.graduation.model.Menu;
import ru.morozov.graduation.model.Restaurant;
import ru.morozov.graduation.to.RestaurantTo;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RestaurantUtil {

    public static List<RestaurantTo> getTos(List<Restaurant> restaurants) {
        return restaurants.stream().map(RestaurantUtil::getTo).collect(Collectors.toList());
    }

    public static RestaurantTo getTo(Restaurant r) {
        Set<Menu> menus = r.getMenuSet();
        Menu menu = (menus != null) ? menus.stream()
                .filter(m -> m.getMenuDate().equals(LocalDate.now()))
                .findFirst()
                .orElse(null) : null;
        return new RestaurantTo(r.id(), r.getName(), menu);
    }
}