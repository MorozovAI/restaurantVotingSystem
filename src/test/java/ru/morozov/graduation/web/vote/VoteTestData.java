package ru.morozov.graduation.web.vote;

import ru.morozov.graduation.model.Vote;
import ru.morozov.graduation.to.VotingResultTo;
import ru.morozov.graduation.web.MatcherFactory;

import java.time.LocalDate;
import java.util.List;

import static ru.morozov.graduation.web.restaurant.RestaurantTestData.*;
import static ru.morozov.graduation.web.user.UserTestData.*;

public class VoteTestData {
    public static final MatcherFactory.Matcher<Vote> VOTE_MATCHER = MatcherFactory.usingEqualsComparator(Vote.class);
    public static final MatcherFactory.Matcher<VotingResultTo> VOTING_RESULT_TO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(VotingResultTo.class);

    public static final int VOTE1_ID = 4;
    public static final int VOTE2_ID = 5;
    public static final int NOT_FOUND = 100;
    public static final Vote vote1 = new Vote(4, LocalDate.now(), user, restaurant2);
    public static final Vote vote2 = new Vote(2, LocalDate.now().minusDays(1), admin, restaurant4);
    public static final Vote vote3 = new Vote(5, LocalDate.now(), admin, restaurant4);
    public static final List<Vote> adminVotes = List.of(vote2,vote3);

    public static final List<VotingResultTo> results1 = List.of(new VotingResultTo(restaurant1, 1L), new VotingResultTo(restaurant2, 1L),
            new VotingResultTo(restaurant4, 1L));

    public static Vote getNew() {
        return new Vote(4, LocalDate.now(), nextUser, restaurant2);
    }

    public static Vote getUpdated() {
        return new Vote(4, LocalDate.now(), user, restaurant2);
    }
}