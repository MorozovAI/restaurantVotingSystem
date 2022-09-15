package ru.morozov.graduation.to;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.morozov.graduation.model.Menu;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RestaurantTo extends NamedTo {

    @JsonIncludeProperties("dishes")
    private Menu todayMenu;

    public RestaurantTo(Integer id, String name, Menu todayMenu) {
        super(id, name);
        this.todayMenu = todayMenu;
    }

    @Override
    public String toString() {
        return "RestaurantTo{" +
                "todayMenu=" + todayMenu +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
