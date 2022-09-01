package ru.morozov.graduation.to;

import lombok.*;
import ru.morozov.graduation.model.Menu;
import ru.morozov.graduation.model.Restaurant;

import java.time.LocalDate;
import java.util.Set;

@Data
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RestaurantTo extends NamedTo {

    private Menu todayMenu;

    public RestaurantTo(Integer id, String name, Menu todayMenu) {
        super(id, name);
        this.todayMenu = todayMenu;
    }
}
