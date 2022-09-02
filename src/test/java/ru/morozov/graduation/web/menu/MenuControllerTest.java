package ru.morozov.graduation.web.menu;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.morozov.graduation.model.Menu;
import ru.morozov.graduation.repository.MenuRepository;
import ru.morozov.graduation.util.JsonUtil;
import ru.morozov.graduation.web.AbstractControllerTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.morozov.graduation.web.dish.DishTestData.*;
import static ru.morozov.graduation.web.menu.MenuTestData.NOT_FOUND;
import static ru.morozov.graduation.web.menu.MenuTestData.*;
import static ru.morozov.graduation.web.restaurant.RestaurantTestData.RESTAURANT1_ID;
import static ru.morozov.graduation.web.user.UserTestData.ADMIN_MAIL;

class MenuControllerTest extends AbstractControllerTest {

    private static final String REST_URL = MenuController.REST_URL + "/menus/";
    private static final String REST_URL2 = MenuController.REST_URL + "/" + RESTAURANT1_ID + "/menus";

    @Autowired
    private MenuRepository repository;

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + MENU1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_MATCHER.contentJson(menu1));
    }

    @Test
    void getUnauth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + MENU1_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + NOT_FOUND))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + MENU1_ID))
                .andExpect(status().isNoContent());
        assertFalse(repository.findById(MENU1_ID).isPresent());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteDataConflict() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + NOT_FOUND))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void update() throws Exception {
        Menu updated = MenuTestData.getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + MENU1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());

        MENU_MATCHER.assertMatch(repository.getExisted(MENU1_ID), updated);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createWithLocation() throws Exception {
        Menu newMenu = MenuTestData.getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMenu)));
        Menu created = MENU_MATCHER.readFromJson(action);
        int newId = created.id();
        newMenu.setId(newId);
        MENU_MATCHER.assertMatch(created, newMenu);
        MENU_MATCHER.assertMatch(repository.getExisted(newId), newMenu);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL2))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_MATCHER.contentJson(menuSet1));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createInvalid() throws Exception {
        Menu invalid = new Menu(null, "M", null);
        perform(MockMvcRequestBuilders.post(REST_URL2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateInvalid() throws Exception {
        Menu invalid = new Menu(MENU1_ID, "R", null);
        perform(MockMvcRequestBuilders.put(REST_URL + MENU1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateHtmlUnsafe() throws Exception {
        Menu invalid = new Menu(MENU1_ID, "<script>alert(123)</script>", LocalDate.now());
        perform(MockMvcRequestBuilders.put(REST_URL + MENU1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    @WithUserDetails(value = ADMIN_MAIL)
    void updateDuplicate() {
        Menu invalid = new Menu(menu1.id(), menu5.getName(), menu5.getMenuDate());
        assertThrows(Exception.class, () ->
                perform(MockMvcRequestBuilders.put(REST_URL + MENU1_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(invalid)))
                        .andDo(print())
                        .andExpect(status().isUnprocessableEntity())
        );
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    @WithUserDetails(value = ADMIN_MAIL)
    void createDuplicate() {
        Menu invalid = new Menu(null, "new menu", menu5.getMenuDate());
        assertThrows(Exception.class, () ->
                perform(MockMvcRequestBuilders.post(REST_URL2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(invalid)))
                        .andDo(print())
                        .andExpect(status().isUnprocessableEntity())
        );
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void addDish() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL + MENU1_ID + "/dishes/add/" + (DISH1_ID + 1))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        DISH_MATCHER.assertMatch(repository.getExisted(MENU1_ID).getDishes(), dishes2);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void addWrongDish() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL + MENU1_ID + "/dishes/add/" + NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void removeDish() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL + MENU1_ID + "/dishes/remove/" + DISH1_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        DISH_MATCHER.assertMatch(repository.getExisted(MENU1_ID).getDishes(), dishes3);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void addDishFromOtherRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL + MENU1_ID + "/dishes/add/" + (DISH1_ID + 6))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());

    }
}