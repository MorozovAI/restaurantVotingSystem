package ru.morozov.graduation.to;


import lombok.*;
import ru.morozov.graduation.model.Restaurant;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class VotingResultTo {

    private int restaurantId;
    private String restaurant;
    private Long votes;

    public VotingResultTo(Restaurant restaurant, Long votes) {
        this(restaurant.id(), restaurant.getName(), votes);
    }

    @Override
    public String toString() {
        return "VotingResultTo{" +
                "restaurantId=" + restaurantId +
                ", restaurant='" + restaurant + '\'' +
                ", votes=" + votes +
                '}';
    }


}
