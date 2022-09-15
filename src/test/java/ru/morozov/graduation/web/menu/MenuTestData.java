package ru.morozov.graduation.web.menu;

import ru.morozov.graduation.model.Menu;
import ru.morozov.graduation.web.MatcherFactory;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.morozov.graduation.web.dish.DishTestData.dishes1;
import static ru.morozov.graduation.web.restaurant.RestaurantTestData.restaurant1;

public class MenuTestData {
    public static final MatcherFactory.Matcher<Menu> MENU_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Menu.class, "restaurant", "dishes");
    public static final int MENU1_ID = 1;
    public static final int NOT_FOUND = 100;
    public static final LocalDate menuDate = LocalDate.now();
    public static final Menu menu1 = new Menu(1, "Меню на " + menuDate, menuDate, restaurant1, dishes1);
    public static final Menu menu5 = new Menu(5, "Меню на " + menuDate.minusDays(1), menuDate.minusDays(1), restaurant1, dishes1);
    public static final Set<Menu> menuSet1 = Stream.of(menu1, menu5).sorted(Comparator.comparing(Menu::getMenuDate).reversed()).collect(Collectors.toCollection(LinkedHashSet::new));

    public static Menu getNew() {
        return new Menu(null, "Новое меню", menuDate.plusDays(1));
    }

    public static Menu getUpdated() {
        return new Menu(MENU1_ID, "Обновленное меню", menuDate);
    }
}
