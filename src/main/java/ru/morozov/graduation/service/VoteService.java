package ru.morozov.graduation.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.morozov.graduation.model.Vote;
import ru.morozov.graduation.repository.RestaurantRepository;
import ru.morozov.graduation.repository.UserRepository;
import ru.morozov.graduation.repository.VoteRepository;

import java.time.LocalDate;
import java.time.LocalTime;

import static ru.morozov.graduation.util.validation.ValidationUtil.assureVoteCanBeChanged;

@Service
public class VoteService {

    @Value("${settings.vote changing end time}")
    private LocalTime changingEndTime;
    private final String EXCEPTION_TOO_LATE_CHANGE_VOTE = "The vote cannot be changed. Too late.";
    private final VoteRepository voteRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;

    public VoteService(VoteRepository voteRepository, UserRepository userRepository, RestaurantRepository restaurantRepository) {
        this.voteRepository = voteRepository;
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public void setChangingEndTime(LocalTime changingEndTime) {
        this.changingEndTime = changingEndTime;
    }

    @Transactional
    public Vote save(@Nullable Vote vote, int userId, int restaurantId) {
        if (vote == null) vote = new Vote(LocalDate.now());
        else assureVoteCanBeChanged(changingEndTime, EXCEPTION_TOO_LATE_CHANGE_VOTE);
        vote.setUser(userRepository.getExisted(userId));
        vote.setRestaurant(restaurantRepository.getExisted(restaurantId));
        return voteRepository.save(vote);
    }

    public void delete(int id) {
        assureVoteCanBeChanged(changingEndTime, EXCEPTION_TOO_LATE_CHANGE_VOTE);
        voteRepository.delete(id);
    }
}
