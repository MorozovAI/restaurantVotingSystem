package ru.morozov.graduation.model;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "vote", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "vote_date"}, name = "vote_unique_user_vote_date_idx")})
@Getter
@Setter
@NoArgsConstructor
public class Vote extends BaseEntity {

    @Column(name = "vote_date")
    @NotNull
    private LocalDate voteDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonIncludeProperties({"id", "name", "email"})
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_id")
    @JsonIncludeProperties({"id", "name"})
    private Restaurant restaurant;

    public Vote(LocalDate voteDate) {
        this.voteDate = voteDate;
    }

    public Vote(Integer id, LocalDate voteDate, User user, Restaurant restaurant) {
        super(id);
        this.voteDate = voteDate;
        this.user = user;
        this.restaurant = restaurant;
    }
}
