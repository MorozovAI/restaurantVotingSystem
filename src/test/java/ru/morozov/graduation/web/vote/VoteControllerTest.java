package ru.morozov.graduation.web.vote;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.morozov.graduation.model.Vote;
import ru.morozov.graduation.repository.VoteRepository;
import ru.morozov.graduation.service.VoteService;
import ru.morozov.graduation.util.JsonUtil;
import ru.morozov.graduation.web.AbstractControllerTest;

import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.morozov.graduation.web.restaurant.RestaurantTestData.RESTAURANT1_ID;
import static ru.morozov.graduation.web.user.UserTestData.NEXT_USER_MAIL;
import static ru.morozov.graduation.web.user.UserTestData.USER_MAIL;
import static ru.morozov.graduation.web.vote.VoteTestData.*;

class VoteControllerTest extends AbstractControllerTest {
    @Autowired
    VoteService voteservice;

    private static final String REST_URL = VoteController.REST_URL + "/";
    private static final String REST_URL2 = VoteController.REST_URL + "/restaurants/" + RESTAURANT1_ID;

    @Autowired
    private VoteRepository voteRepository;

    @Test
    @WithUserDetails(value = USER_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + VOTE1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(vote1));
    }

    @Test
    void getUnauth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + VOTE1_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getNotOwn() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + VOTE2_ID))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + NOT_FOUND))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + VOTE1_ID))
                .andExpect(status().isNoContent());
        Optional<Vote> v = voteRepository.findById(VOTE1_ID);
        assertFalse(voteRepository.get(VOTE1_ID) != null);
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void deleteDataConflict() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + VOTE2_ID))
                .andExpect(status().isConflict());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void update() throws Exception {
        voteservice.setChangingEndTime(LocalTime.now().plusHours(1));
        Vote updated = getUpdated();
        perform(MockMvcRequestBuilders.post(REST_URL2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)));
        VOTE_MATCHER.assertMatch(voteRepository.getExisted(VOTE1_ID), updated);
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateAfterStop() throws Exception {
        voteservice.setChangingEndTime(LocalTime.now().minusHours(1));
        Vote updated = getUpdated();
        perform(MockMvcRequestBuilders.post(REST_URL2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isConflict());
    }

    @Test
    @WithUserDetails(value = NEXT_USER_MAIL)
    void createWithLocation() throws Exception {
        Vote newVote = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newVote)));
        Vote created = VOTE_MATCHER.readFromJson(action);
        int newId = created.id();
        newVote.setId(newId);
        VOTE_MATCHER.assertMatch(created, newVote);
        VOTE_MATCHER.assertMatch(voteRepository.getExisted(newId), newVote);
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(votes1));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getResults() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "results"))
                .andExpect(status().isOk())
                .andDo(print())
                // .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTING_RESULT_TO_MATCHER.contentJson(results1));
    }

}