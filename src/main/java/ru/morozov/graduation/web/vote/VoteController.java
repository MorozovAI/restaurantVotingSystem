package ru.morozov.graduation.web.vote;

import lombok.RequiredArgsConstructor;
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

import static ru.morozov.graduation.util.validation.ValidationUtil.assureIdConsistent;

@RestController
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
public class VoteController {
    static final String REST_URL = "/api/profile/votes";

    private final VoteRepository voteRepository;
    private final VoteService voteService;

    @GetMapping
    public List<Vote> getAll(@AuthenticationPrincipal AuthUser authUser) {
        return voteRepository.getAllByUser(authUser.getUser());
    }

    @GetMapping("/currentVote")
    public ResponseEntity<Vote> getCurrentVote() {
        int userId = SecurityUtil.authId();
        log.info("get current vote of user {}", userId);
        return ResponseEntity.of(voteRepository.getCurrentVote(userId));
    }

    @GetMapping("/results")
    public List<VotingResultTo> getResults() {
        log.info("get current voting results");
        return voteRepository.getVotingResults(LocalDate.now());
    }

    @PostMapping(value = "/restaurants/{restaurantId}")
    public ResponseEntity<Vote> create(@AuthenticationPrincipal AuthUser authUser, @PathVariable int restaurantId) {
        Vote created = voteService.save(new Vote(LocalDate.now()), authUser.id(), restaurantId);
        log.info("create {} for restaurant {}", created, restaurantId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/restaurants/{restaurantId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable int restaurantId) {
        voteService.update(restaurantId);
    }
}
