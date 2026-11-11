package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import java.time.LocalDate;


@SpringBootTest
class UserControllerTests {
    private User user;
    private InMemoryUserStorage userStorage;
    private UserService service;
    private UserController controller;

    @BeforeEach
    void setUp() {
        user = new User();
        userStorage = new InMemoryUserStorage();
        service = new UserService(userStorage);
        controller = new UserController(service);
    }

    @Test
    void mustBeExceptionWithEmptyEmail() {
        ValidationException e = Assertions.assertThrows(
                ValidationException.class,
                () -> controller.create(user)
        );
        Assertions.assertEquals("Некорректный ввод email: либо пусто, либо нет @", e.getMessage());
    }

    @Test
    void mustBeExceptionWithIncorrectEmail() {
        user.setEmail("xxx");
        ValidationException e = Assertions.assertThrows(
                ValidationException.class,
                () -> controller.create(user)
        );
        Assertions.assertEquals("Некорректный ввод email: либо пусто, либо нет @", e.getMessage());
    }

    @Test
    void mustBeExceptionWithEmptyLogin() {
        user.setEmail("yandex@dss.ru");
        ValidationException e = Assertions.assertThrows(
                ValidationException.class,
                () -> controller.create(user)
        );
        Assertions.assertEquals("Некорректный ввод логина: либо пусто, либо есть пробелы", e.getMessage());
    }

    @Test
    void mustBeExceptionWithBlankInLogin() {
        user.setEmail("yandex@dss.ru");
        user.setName("Mark Tven");
        ValidationException e = Assertions.assertThrows(
                ValidationException.class,
                () -> controller.create(user)
        );
        Assertions.assertEquals("Некорректный ввод логина: либо пусто, либо есть пробелы", e.getMessage());
    }

    @Test
    void ifNameIsEmptyLoginMustBeEqual() {
        user.setLogin("Ivan");
        user.setEmail("yandex@dss.ru");
        Assertions.assertDoesNotThrow(() -> controller.create(user));
        Assertions.assertEquals("Ivan", user.getName());
    }

    @Test
    void mustBeExceptionWithDateRealeseBefore1895() {
        user.setLogin("Alexander");
        user.setEmail("yandex@dss.ru");
        user.setBirthday(LocalDate.of(2088, 12, 27));

        ValidationException e = Assertions.assertThrows(
                ValidationException.class,
                () -> controller.create(user)
        );
        Assertions.assertEquals("Некорректная дата дня рождения!", e.getMessage());
    }

    @Test
    void mustBeSuccessWithCreateDataField() {
        user.setLogin("Alexander");
        user.setEmail("yandex@dss.ru");
        user.setBirthday(LocalDate.of(1988, 12, 27));
        Assertions.assertDoesNotThrow(() -> controller.create(user));

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
        Assertions.assertDoesNotThrow(() -> controller.create(user));

        user.setLogin("Vladislav");
        user.setEmail("google@gm.com");
        user.setBirthday(LocalDate.of(1982, 4, 20));
        Assertions.assertDoesNotThrow(() -> controller.update(user));

        Assertions.assertEquals(1, user.getId());
        Assertions.assertEquals("Vladislav", user.getLogin());
        Assertions.assertEquals("Alexander", user.getName());
        Assertions.assertEquals("google@gm.com", user.getEmail());
        Assertions.assertEquals(LocalDate.of(1982,4,20), user.getBirthday());
    }
}