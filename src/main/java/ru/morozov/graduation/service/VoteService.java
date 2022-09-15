package ru.morozov.graduation.service;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.morozov.graduation.error.AppException;
import ru.morozov.graduation.model.Restaurant;
import ru.morozov.graduation.model.Vote;
import ru.morozov.graduation.repository.RestaurantRepository;
import ru.morozov.graduation.repository.VoteRepository;
import ru.morozov.graduation.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.springframework.boot.web.error.ErrorAttributeOptions.Include.MESSAGE;
import static ru.morozov.graduation.util.validation.ValidationUtil.checkVoteCanBeChanged;

@Service
@RequiredArgsConstructor
public class VoteService {

    @Value("${settings.vote-changing-end-time}")
    private LocalTime changingEndTime;
    private final VoteRepository voteRepository;
    private final RestaurantRepository restaurantRepository;
    private final SessionFactory sessionFactory;

    public void setChangingEndTime(LocalTime changingEndTime) {
        this.changingEndTime = changingEndTime;
    }

    @Transactional
    public Vote save(Vote vote, int userId, int restaurantId) {
        Session session = sessionFactory.openSession();
        boolean isNewVote = voteRepository.getByVoteDate(LocalDate.now(), userId).isEmpty();
        if (isNewVote) {
            vote.setUser(SecurityUtil.authUser());
            vote.setRestaurant(session.load(Restaurant.class, restaurantId));
            session.close();
        } else
            throw new AppException(HttpStatus.CONFLICT, "Vote is created already", ErrorAttributeOptions.of(MESSAGE));

        return voteRepository.save(vote);
    }

    @Transactional
    public Vote update(int restaurantId) {
        checkVoteCanBeChanged(changingEndTime);
        int userId = SecurityUtil.authId();
        Vote vote = voteRepository.getCurrentVote(userId).orElseThrow(
                () -> new AppException(HttpStatus.CONFLICT, "No vote found for update", ErrorAttributeOptions.of(MESSAGE)));
        voteRepository.checkBelong(vote.id(), userId);
        vote.setRestaurant(restaurantRepository.getExisted(restaurantId));
        return voteRepository.save(vote);
    }
}
