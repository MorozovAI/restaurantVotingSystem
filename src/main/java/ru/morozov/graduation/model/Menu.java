package ru.morozov.graduation.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "menu", uniqueConstraints = {@UniqueConstraint(columnNames = {"restaurant_id", "menu_date"}, name = "menu_unique_restaurantId_menuDate_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu extends NamedEntity {

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "dish_menu",
            uniqueConstraints = {@UniqueConstraint(columnNames = {"menu_id", "dish_id"},
                    name = "dish_menu_unique_idx")},
            joinColumns = @JoinColumn(name = "menu_id"),
            inverseJoinColumns = @JoinColumn(name = "dish_id")
    )
    @Schema(hidden = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Dish> dishes = new HashSet<>();

    @Column(name = "menu_date")
    @NotNull
    private LocalDate menuDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @JsonBackReference(value = "restaurant_menu")
    @Schema(hidden = true)
    private Restaurant restaurant;

    public Menu(Integer id, String name, LocalDate menuDate, Restaurant restaurant, Set<Dish> dishes) {
        super(id, name);
        this.menuDate = menuDate;
        this.restaurant = restaurant;
        this.dishes = dishes;
    }

    public Menu(Integer id, String name, LocalDate menuDate) {
        this(id, name, menuDate, null, null);
    }

    public Menu(Menu menu) {
        this(menu.id, menu.name, menu.menuDate, menu.getRestaurant(), menu.getDishes());
    }

    @Override
    public String toString() {
        return "Menu{" +
                "menuDate=" + menuDate +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
