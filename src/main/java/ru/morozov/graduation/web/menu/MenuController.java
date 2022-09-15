package ru.morozov.graduation.web.menu;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.morozov.graduation.error.IllegalRequestDataException;
import ru.morozov.graduation.model.Dish;
import ru.morozov.graduation.model.Menu;
import ru.morozov.graduation.repository.DishRepository;
import ru.morozov.graduation.repository.MenuRepository;
import ru.morozov.graduation.repository.RestaurantRepository;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static ru.morozov.graduation.util.validation.ValidationUtil.*;

@RestController
@RequestMapping(value = MenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@RequiredArgsConstructor
@CacheConfig(cacheNames = "restaurants")
public class MenuController {
    static final String REST_URL = "/api/admin/";

    private final MenuRepository menuRepository;
    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;

    @GetMapping("restaurants/{restaurantId}/menus")
    public List<Menu> getAll(@PathVariable int restaurantId) {
        log.info("getAll menus for {}", restaurantId);
        return menuRepository.getAll(restaurantId);
    }

    @GetMapping("/menus/{id}")
    public ResponseEntity<Menu> get(@PathVariable int id) {
        log.info("get menu {}", id);
        return ResponseEntity.of(menuRepository.findById(id));
    }

    @GetMapping("restaurants/{restaurantId}/menu_on_today")
    public ResponseEntity<Menu> getTodayMenu(@PathVariable int restaurantId) {
        log.info("get today menu for restaurant {}", restaurantId);
        return ResponseEntity.of(menuRepository.getByDate(LocalDate.now(), restaurantId));
    }

    @PostMapping(value = "restaurants/{restaurantId}/menus", consumes = MediaType.APPLICATION_JSON_VALUE)
    @CacheEvict(allEntries = true)
    public ResponseEntity<Menu> createWithLocation(@Valid @RequestBody Menu menu, @PathVariable int restaurantId) {
        checkNew(menu);
        log.info("create menu {}", menu);
        menu.setRestaurant(restaurantRepository.getExisted(restaurantId));
        Menu created = menuRepository.save(menu);
        URI uriOfNewResource = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path(REST_URL + "menus/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/menus/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(allEntries = true)
    public void update(@Valid @RequestBody Menu menu, @PathVariable int id) {
        log.info("update menu {}", menu);
        assureIdConsistent(menu, id);
        Menu preUpdated = menuRepository.getExisted(id);
        Menu updated = new Menu(menu.id(), menu.getName(), menu.getMenuDate(), preUpdated.getRestaurant(), preUpdated.getDishes());
        menuRepository.save(updated);
    }

    @PatchMapping(value = "menus/{menuId}/dishes/{dishId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(allEntries = true)
    public void addDish(@PathVariable int menuId, @PathVariable int dishId) {
        log.info("add dish {} to menu {}", dishId, menuId);
        Menu menu = menuRepository.getExisted(menuId);
        Dish dish = dishRepository.getExisted(dishId);
        checkMenuCanHaveDish(menu, dish);
        menu.getDishes().add(dish);
        menuRepository.save(menu);
    }

    @DeleteMapping(value = "menus/{menuId}/dishes/{dishId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(allEntries = true)
    public void removeDish(@PathVariable int menuId, @PathVariable int dishId) {
        log.info("remove dish {} from menu {}", dishId, menuId);
        Menu menu = menuRepository.getExisted(menuId);
        Dish dish = dishRepository.getExisted(dishId);
        if (!menu.getDishes().remove(dish))
            throw new IllegalRequestDataException("Menu " + menuId + " doesn't contain dish " + dishId);
        menuRepository.save(menu);
    }

    @DeleteMapping("menus/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(allEntries = true)
    public void delete(@PathVariable int id) {
        menuRepository.deleteExisted(id);
    }
}
