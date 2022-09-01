package ru.morozov.graduation.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.morozov.graduation.error.DataConflictException;
import ru.morozov.graduation.model.User;
import ru.morozov.graduation.model.Vote;
import ru.morozov.graduation.to.VotingResultTo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote> {

    @Query(value = "SELECT " +
            "    new ru.morozov.graduation.to.VotingResultTo(r, COUNT(v.restaurant.id) as cnt) " +
            "FROM " +
            "   Restaurant r LEFT JOIN Vote v ON r.id=v.restaurant.id " +
          //  "WHERE v.voteDate=?1" +
            "GROUP BY " +
            " r " +
            "ORDER BY cnt DESC")
    List<VotingResultTo> getVotingResults(LocalDate localDate);

    @Query("SELECT v FROM Vote v WHERE v.voteDate=?1 AND v.user.id=?2")
    Vote getByVoteDate(LocalDate date, int userId);

    @EntityGraph(attributePaths = {"restaurant", "user"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT v FROM Vote v")
    List<Vote> getAll();

    @EntityGraph(attributePaths = {"restaurant", "user"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT v FROM Vote v WHERE v.id=?1 and v.user.id=?2")
    Optional<Vote> get(int id, int userId);

    List<Vote> getAllByUser(User user);

    default Vote checkBelong(int id, int userId) {
        return get(id, userId).orElseThrow(
                () -> new DataConflictException("Vote id=" + id + " doesn't belong to User id=" + userId));
    }
}