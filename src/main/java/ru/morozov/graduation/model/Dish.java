package ru.morozov.graduation.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import ru.morozov.graduation.util.JsonUtil;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "dish", uniqueConstraints = {@UniqueConstraint(columnNames = {"restaurant_id", "name"}, name = "dish_unique_restaurant_id_name_idx")})
@Getter
@Setter
@NoArgsConstructor
public class Dish extends NamedEntity {
    @Column(name = "price")
    @Range(min = 1, max = 10000)
    @JsonSerialize(using = JsonUtil.BigDecimalSerializer.class)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_id")
    @JsonIgnore
    private Restaurant restaurant;

    @ManyToMany
    @Schema(hidden = true)
    @JsonIgnore
    private Set<Menu> menu = new HashSet<>();

    public Dish(Integer id, String name, double price) {
        super(id, name);
        this.price = BigDecimal.valueOf(price);
    }
}
