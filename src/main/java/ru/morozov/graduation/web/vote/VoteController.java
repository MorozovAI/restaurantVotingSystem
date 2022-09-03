package ru.morozov.graduation.web.vote;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.morozov.graduation.model.Vote;
import ru.morozov.graduation.repository.VoteRepository;
import ru.morozov.graduation.service.VoteService;
import ru.morozov.graduation.to.VotingResultTo;
import ru.morozov.graduation.web.AuthUser;
import ru.morozov.graduation.web.SecurityUtil;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
public class VoteController {
    static final String REST_URL = "/api/profile/votes";

    private final VoteRepository voteRepository;
    private final VoteService voteService;

    @GetMapping
    public List<Vote> getAll(@AuthenticationPrincipal AuthUser authUser) {
        return voteRepository.getAllByUser(authUser.getUser());
    }

    @GetMapping("/{id}")
    public Vote get(@PathVariable int id) {
        int userId = SecurityUtil.authId();
        log.info("get vote {} of user {}", id, userId);
        voteRepository.checkBelong(id, userId);
        return voteRepository.get(id, userId).orElse(null);
    }

    @GetMapping("/results")
    public List<VotingResultTo> getResults() {
        return voteRepository.getVotingResults(LocalDate.now());
    }

    @PostMapping(value = "/restaurants/{restaurantId}")
    public ResponseEntity<Vote> createWithLocation(@AuthenticationPrincipal AuthUser authUser, @PathVariable int restaurantId) {
        Vote vote = voteRepository.getByVoteDate(LocalDate.now(), authUser.id());
        Vote created = voteService.save(vote, authUser.id(), restaurantId);
        log.info(vote == null ? "create" : "update" + " {} for restaurant {}", vote, restaurantId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        int userId = SecurityUtil.authId();
        log.info("delete vote {}", id);
        voteRepository.checkBelong(id, userId);
        voteRepository.delete(id);
        }
}
