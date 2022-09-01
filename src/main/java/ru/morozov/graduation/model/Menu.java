package ru.morozov.graduation.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "menu", uniqueConstraints = {@UniqueConstraint(columnNames = {"restaurant_id", "name"}, name = "menu_unique_restaurant_id_name_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Menu extends NamedEntity {

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "dish_menu",
            joinColumns = @JoinColumn(name = "menu_id"),
            inverseJoinColumns = @JoinColumn(name = "dish_id")
    )
    @OrderBy("price DESC")
    @Schema(hidden = true)
    private Set<Dish> dishes = new HashSet<>();

    @Column(name = "menu_date", nullable = false, columnDefinition = "timestamp default now()", updatable = false)
    @NotNull
    private LocalDate menuDate;

    @ManyToOne(fetch = FetchType.EAGER)
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
}
