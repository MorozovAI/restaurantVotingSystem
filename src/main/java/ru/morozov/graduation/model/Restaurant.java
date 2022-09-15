package ru.morozov.graduation.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "restaurant", uniqueConstraints = {@UniqueConstraint(columnNames = "name", name = "restaurant_unique_name_idx")})
@Getter
@Setter
@NoArgsConstructor
public class Restaurant extends NamedEntity {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    @OrderBy("menuDate")
    @JsonIgnore
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Menu> menuSet;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    @OrderBy("price")
    @Schema(hidden = true)
   // @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Dish> dishes = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    @Schema(hidden = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Vote> votes;

    public Restaurant(Integer id, String name) {
        super(id, name);
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
