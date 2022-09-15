package ru.morozov.graduation.web.dish;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.morozov.graduation.model.Dish;
import ru.morozov.graduation.repository.DishRepository;
import ru.morozov.graduation.repository.RestaurantRepository;

import javax.validation.Valid;
import java.net.URI;
import java.util.Set;

import static ru.morozov.graduation.util.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = DishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@CacheConfig(cacheNames = "restaurants")
@RequiredArgsConstructor
public class DishController {
    static final String REST_URL = "/api/admin/";

    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;

    @GetMapping(path = "/dishes/{id}")
    public ResponseEntity<Dish> get(@PathVariable int id) {
        log.info("get dish {}", id);
        return ResponseEntity.of(dishRepository.findById(id));
    }

    @GetMapping("restaurants/{restaurantId}/dishes")
    public Set<Dish> getAllByRestaurant(@PathVariable int restaurantId) {
        return dishRepository.getAllByRestaurant(restaurantId);
    }

    @PostMapping(value = "restaurants/{restaurantId}/dishes", consumes = MediaType.APPLICATION_JSON_VALUE)
    @CacheEvict(allEntries = true)
    public ResponseEntity<Dish> createWithLocation(@Valid @RequestBody Dish dish, @PathVariable int restaurantId) {
        checkNew(dish);
        log.info("create {}", dish);
        dish.setRestaurant(restaurantRepository.getExisted(restaurantId));
        Dish created = dishRepository.save(dish);
        URI uriOfNewResource = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path(REST_URL + "dishes/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @DeleteMapping("dishes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(allEntries = true)
    public void delete(@PathVariable int id) {
        log.info("delete dish {}", id);
        dishRepository.deleteExisted(id);
    }
}
