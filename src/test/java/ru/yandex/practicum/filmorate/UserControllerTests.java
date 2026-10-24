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
            userController.update(user);
        } catch (ValidationException e) {
            Assertions.assertEquals("Некорректный ввод email: либо пусто, либо нет @", e.getMessage());
        }
    }

    @Test
    void mustBeExceptionWithIncorrectEmail() {
        user.setEmail("xxx");
        try {
            userController.update(user);
        } catch (ValidationException e) {
            Assertions.assertEquals("Некорректный ввод email: либо пусто, либо нет @", e.getMessage());
        }
    }

    @Test
    void mustBeExceptionWithEmptyLogin() {
        user.setEmail("yandex@dss.ru");
        try {
            userController.update(user);
        } catch (ValidationException e) {
            Assertions.assertEquals("Некорректный ввод логина: либо пусто, либо есть пробелы", e.getMessage());
        }
    }

    @Test
    void mustBeExceptionWithBlankInLogin() {
        user.setEmail("yandex@dss.ru");
        user.setName("Mark Tven");

        try {
            userController.update(user);
        } catch (ValidationException e) {
            Assertions.assertEquals("Некорректный ввод логина: либо пусто, либо есть пробелы", e.getMessage());
        }
    }

    @Test
    void ifNameIsEmptyLoginMustBeEqual() {
        user.setLogin("Ivan");
        user.setEmail("yandex@dss.ru");
        userController.update(user);
        Assertions.assertEquals("Ivan", user.getName());
    }

    @Test
    void mustBeExceptionWithDateRealeseBefore1895() {
        user.setLogin("Alexander");
        user.setEmail("yandex@dss.ru");
        user.setBirthday(LocalDate.of(2088, 12, 27));

        try {
            userController.update(user);
        } catch (ValidationException e) {
            Assertions.assertEquals("День рождение не может быть в будущем!", e.getMessage());
        }
    }

    @Test
    void mustBeSuccessWithCorrectDataField() {
        user.setLogin("Alexander");
        user.setEmail("yandex@dss.ru");
        user.setBirthday(LocalDate.of(1988, 12, 27));
        userController.update(user);

        Assertions.assertEquals("Alexander", user.getLogin());
        Assertions.assertEquals("Alexander", user.getName());
        Assertions.assertEquals("yandex@dss.ru", user.getEmail());
        Assertions.assertEquals(LocalDate.of(1988,12,27), user.getBirthday());
    }
}