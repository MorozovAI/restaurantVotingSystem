package ru.morozov.graduation.web.menu;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.morozov.graduation.model.Dish;
import ru.morozov.graduation.model.Menu;
import ru.morozov.graduation.repository.DishRepository;
import ru.morozov.graduation.repository.MenuRepository;
import ru.morozov.graduation.repository.RestaurantRepository;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static ru.morozov.graduation.util.validation.ValidationUtil.assureMenuCanHaveDish;
import static ru.morozov.graduation.util.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = MenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class MenuController {
    static final String REST_URL = "/api/admin/restaurants/";

    private final MenuRepository menuRepository;
    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;

    @GetMapping("{restaurantId}/menus")
    public List<Menu> getAll(@PathVariable int restaurantId) {
        log.info("getAll menus for {}", restaurantId);
        return menuRepository.getAll(restaurantId);
    }

    @GetMapping("menus/{id}")
    public Menu get(@PathVariable int id) {
        log.info("get menu {}", id);
        return menuRepository.getExisted(id);
    }

    @GetMapping("{restaurantId}/menu_on_today")
    public Menu getTodayMenu(@PathVariable int restaurantId) {
        log.info("get today menu for restaurant {}", restaurantId);
        return menuRepository.getByDate(LocalDate.now(), restaurantId);
    }

    @PostMapping(value = "{restaurantId}/menus", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Menu> createWithLocation(@Valid @RequestBody Menu menu, @PathVariable int restaurantId) {
        checkNew(menu);
        log.info("create menu {}", menu);
        menu.setRestaurant(restaurantRepository.getExisted(restaurantId));
        Menu created = menuRepository.save(menu);
        URI uriOfNewResource = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/menus/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody Menu menu, @PathVariable int id) {
        log.info("update menu {}", menu);
        Menu updated = new Menu(id, menu.getName(), menu.getMenuDate(), menu.getRestaurant(), menu.getDishes());
        menuRepository.save(updated);
    }

    @PutMapping(value = "menus/{menuId}/dishes/add/{dishId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addDish(@PathVariable int menuId, @PathVariable int dishId) {
        log.info("add dish {} to menu {}", dishId, menuId);
        Menu menu = get(menuId);
        Dish dish = dishRepository.getExisted(dishId);
        assureMenuCanHaveDish(menu, dish);
        menu.getDishes().add(dish);
        update(menu, menuId);
    }

    @PutMapping(value = "menus/{menuId}/dishes/remove/{dishId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeDish(@PathVariable int menuId, @PathVariable int dishId) {
        log.info("remove dish {} from menu {}", dishId, menuId);
        Menu menu = get(menuId);
        Dish dish = dishRepository.getExisted(dishId);
        assureMenuCanHaveDish(menu, dish);
        menu.getDishes().remove(dish);
        update(menu, menuId);
    }

    @DeleteMapping("menus/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        menuRepository.deleteExisted(id);
    }
}
