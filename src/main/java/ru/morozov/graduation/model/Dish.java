package ru.morozov.graduation.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "dish")
@Getter
@Setter
@NoArgsConstructor
public class Dish extends NamedEntity {
    @Column(name = "price")
    @Range(min = 1, max = 10000)
    @JsonSerialize(using = JsonUtil.BigDecimalSerializer.class)
    @NotNull
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    @NotNull
    private Restaurant restaurant;

    @ManyToMany
    @Schema(hidden = true)
    @JsonIgnore
    private Set<Menu> menu = new HashSet<>();

    public Dish(Integer id, String name, double price) {
        super(id, name);
        this.price = BigDecimal.valueOf(price);
    }

    public Dish(Integer id, String name, double price, Restaurant restaurant) {
        this(id, name, price);
        this.restaurant = restaurant;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "price=" + price +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
