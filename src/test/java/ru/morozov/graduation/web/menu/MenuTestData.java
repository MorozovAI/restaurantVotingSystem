package ru.morozov.graduation.web.menu;

import ru.morozov.graduation.model.Menu;
import ru.morozov.graduation.model.Restaurant;
import ru.morozov.graduation.to.RestaurantTo;
import ru.morozov.graduation.web.MatcherFactory;

import java.time.LocalDate;

public class MenuTestData {
    public static final MatcherFactory.Matcher<Restaurant> MENU_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class,  "restaurant", "dishes");
    public static final int MENU1_ID = 1;
    public static final int NOT_FOUND = 100;
    public static final LocalDate menuDate = LocalDate.now();
    public static final Menu menu1 = new Menu(1, "Меню на" + menuDate, menuDate);

    public static Menu getNew() {
        return new Menu(null, "Новое меню", menuDate.plusDays(1));
    }

    public static Menu getUpdated() {
        return new Menu(MENU1_ID, "Обновленное меню", menuDate);
    }
}
