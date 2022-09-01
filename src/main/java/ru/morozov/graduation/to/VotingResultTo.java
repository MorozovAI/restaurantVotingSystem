package ru.morozov.graduation.to;


import ru.morozov.graduation.model.Restaurant;

public class VotingResultTo {
    private final int restaurantId;
    private final String restaurant;
    private final Long votes;

    public VotingResultTo(Restaurant restaurant, Long votes) {
        this.restaurantId = restaurant.id();
        this.restaurant = restaurant.getName();
        this.votes = votes;
    }

    public String getRestaurant() {
        return restaurant;
    }

    @Override
    public String toString() {
        return "VotingResultTo{" +
                "restaurantId=" + restaurantId +
                ", restaurant='" + restaurant + '\'' +
                ", votes=" + votes +
                '}';
    }

    public Long getVotes() {
        return votes;
    }

}
