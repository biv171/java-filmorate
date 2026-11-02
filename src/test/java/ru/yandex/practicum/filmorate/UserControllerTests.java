package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import java.time.LocalDate;


@SpringBootTest
class UserControllerTests {
    User user = new User();
    UserController userController = new UserController();

    @Test
    void mustBeExceptionWithEmptyEmail() {
        try {
            userController.create(user);
        } catch (ValidationException e) {
            Assertions.assertEquals("Некорректный ввод email: либо пусто, либо нет @", e.getMessage());
        }
    }

    @Test
    void mustBeExceptionWithIncorrectEmail() {
        user.setEmail("xxx");
        try {
            userController.create(user);
        } catch (ValidationException e) {
            Assertions.assertEquals("Некорректный ввод email: либо пусто, либо нет @", e.getMessage());
        }
    }

    @Test
    void mustBeExceptionWithEmptyLogin() {
        user.setEmail("yandex@dss.ru");
        try {
            userController.create(user);
        } catch (ValidationException e) {
            Assertions.assertEquals("Некорректный ввод логина: либо пусто, либо есть пробелы", e.getMessage());
        }
    }

    @Test
    void mustBeExceptionWithBlankInLogin() {
        user.setEmail("yandex@dss.ru");
        user.setName("Mark Tven");

        try {
            userController.create(user);
        } catch (ValidationException e) {
            Assertions.assertEquals("Некорректный ввод логина: либо пусто, либо есть пробелы", e.getMessage());
        }
    }

    @Test
    void ifNameIsEmptyLoginMustBeEqual() {
        user.setLogin("Ivan");
        user.setEmail("yandex@dss.ru");
        userController.create(user);
        Assertions.assertEquals("Ivan", user.getName());
    }

    @Test
    void mustBeExceptionWithDateRealeseBefore1895() {
        user.setLogin("Alexander");
        user.setEmail("yandex@dss.ru");
        user.setBirthday(LocalDate.of(2088, 12, 27));

        try {
            userController.create(user);
        } catch (ValidationException e) {
            Assertions.assertEquals("Некорректная дата дня рождения!", e.getMessage());
        }
    }

    @Test
    void mustBeSuccessWithCreateDataField() {
        user.setLogin("Alexander");
        user.setEmail("yandex@dss.ru");
        user.setBirthday(LocalDate.of(1988, 12, 27));
        userController.create(user);

        Assertions.assertEquals("Alexander", user.getLogin());
        Assertions.assertEquals("Alexander", user.getName());
        Assertions.assertEquals("yandex@dss.ru", user.getEmail());
        Assertions.assertEquals(LocalDate.of(1988,12,27), user.getBirthday());
    }

    @Test
    void mustBeSuccessWithChangeDataField() {
        user.setLogin("Alexander");
        user.setEmail("yandex@dss.ru");
        user.setBirthday(LocalDate.of(1988, 12, 27));
        userController.create(user);

        user.setLogin("Vladislav");
        user.setEmail("google@gm.com");
        user.setBirthday(LocalDate.of(1982, 4, 20));
        userController.update(user);

        Assertions.assertEquals(1, user.getId());
        Assertions.assertEquals("Vladislav", user.getLogin());
        Assertions.assertEquals("Alexander", user.getName());
        Assertions.assertEquals("google@gm.com", user.getEmail());
        Assertions.assertEquals(LocalDate.of(1982,4,20), user.getBirthday());
    }
}