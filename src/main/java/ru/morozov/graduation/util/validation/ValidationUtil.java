package ru.morozov.graduation.util.validation;

import lombok.experimental.UtilityClass;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import ru.morozov.graduation.HasId;
import ru.morozov.graduation.error.AppException;
import ru.morozov.graduation.error.IllegalRequestDataException;
import ru.morozov.graduation.model.Dish;
import ru.morozov.graduation.model.Menu;

import java.time.LocalTime;

import static org.springframework.boot.web.error.ErrorAttributeOptions.Include.MESSAGE;

@UtilityClass
public class ValidationUtil {

    public static void checkNew(HasId bean) {
        if (!bean.isNew()) {
            throw new IllegalRequestDataException(bean.getClass().getSimpleName() + " must be new (id=null)");
        }
    }

    //  Conservative when you reply, but accept liberally (http://stackoverflow.com/a/32728226/548473)
    public static void assureIdConsistent(HasId bean, int id) {
        if (bean.isNew()) {
            bean.setId(id);
        } else if (bean.id() != id) {
            throw new IllegalRequestDataException(bean.getClass().getSimpleName() + " must has id=" + id);
        }
    }

    public static void checkModification(int count, int id) {
        if (count == 0) {
            throw new IllegalRequestDataException("Entity with id=" + id + " not found");
        }
    }

    public static <T> T checkExisted(T obj, int id) {
        if (obj == null) {
            throw new IllegalRequestDataException("Entity with id=" + id + " not found");
        }
        return obj;
    }

    public static void assureVoteCanBeChanged(LocalTime changingEndTime, String message) {
        if (LocalTime.now().isAfter(changingEndTime))
            throw new AppException(HttpStatus.CONFLICT, message, ErrorAttributeOptions.of(MESSAGE));
    }

    public static void assureMenuCanHaveDish(Menu menu, Dish dish) {
        if (menu.getRestaurant().id() != dish.getRestaurant().id()) {
            throw new IllegalRequestDataException("Dish with id=" + dish.id() + " can not be added to menu with id " + menu.id());
        }
    }

    //  https://stackoverflow.com/a/65442410/548473
    @NonNull
    public static Throwable getRootCause(@NonNull Throwable t) {
        Throwable rootCause = NestedExceptionUtils.getRootCause(t);
        return rootCause != null ? rootCause : t;
    }
}