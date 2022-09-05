package ru.morozov.graduation.web.restaurant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import ru.morozov.graduation.model.Restaurant;
import ru.morozov.graduation.repository.DishRepository;
import ru.morozov.graduation.repository.RestaurantRepository;
import ru.morozov.graduation.to.RestaurantTo;
import ru.morozov.graduation.util.RestaurantUtil;

import java.util.List;

import static ru.morozov.graduation.util.validation.ValidationUtil.assureIdConsistent;
import static ru.morozov.graduation.util.validation.ValidationUtil.checkNew;

@Controller
@Slf4j
@CacheConfig(cacheNames = "restaurants")
public abstract class AbstractRestaurantController {

    @Autowired
    private RestaurantRepository repository;
    @Autowired
    private DishRepository dishRepository;

    @Cacheable
    public RestaurantTo get(int id) {
        log.info("get restaurant {}", id);
        return RestaurantUtil.getTo(repository.getExisted(id));
    }

    @Transactional
    @CacheEvict(allEntries = true)
    public void delete(int id) {
        log.info("delete restaurant {}", id);
        dishRepository.deleteByRestaurant_Id(id);
        repository.deleteExisted(id);
    }

    @Cacheable
    public List<RestaurantTo> getAll() {
        log.info("getAll restaurants with menu on today");
        return RestaurantUtil.getTos(repository.getAllWithMenu());
    }

    @CacheEvict(allEntries = true)
    public Restaurant create(Restaurant restaurant) {
        checkNew(restaurant);
        log.info("create {}", restaurant);
        return repository.save(restaurant);
    }

    @CacheEvict(allEntries = true)
    public void update(Restaurant restaurant, int id) {
        assureIdConsistent(restaurant, id);
        log.info("update {}", restaurant);
        repository.save(restaurant);
    }
}