package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import lombok.extern.slf4j.Slf4j;
import java.util.Collection;
import java.util.HashMap;
import java.time.LocalDate;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Long, User> users = new HashMap<>();
    private final LocalDate nowDateTime = LocalDate.now();

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @RequestMapping(method = {RequestMethod.POST, RequestMethod.PUT})
    public User update(@RequestBody User user) {
        log.info("LOGGER Users: валидация начало процесса");
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            log.error("LOGGER Users: некорректный ввод email: либо пусто, либо нет @");
            throw new ValidationException("Некорректный ввод email: либо пусто, либо нет @");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.error("LOGGER Users: некорректный ввод логина: либо пусто, либо есть пробелы");
            throw new ValidationException("Некорректный ввод логина: либо пусто, либо есть пробелы");
        }
        if (user.getBirthday().isAfter(nowDateTime)) {
            log.error("LOGGER Users: день рождение не может быть в будущем!");
            throw new ValidationException("День рождение не может быть в будущем!");
        }
        log.info("LOGGER Users: валидация данных SUCCESS");

        if (users.containsKey(user.getId())) {
            User changeUser = users.get(user.getId());
            changeUser.setEmail(user.getEmail());
            changeUser.setLogin(user.getLogin());
            changeUser.setName(user.checkNameLogin());
            changeUser.setBirthday(user.getBirthday());
            log.info("LOGGER Users: изменение данных SUCCESS {}", user);
            return changeUser;
        }

        user.setId(getNextId());
        user.setEmail(user.getEmail());
        user.setLogin(user.getLogin());
        user.setName(user.checkNameLogin());
        user.setBirthday(user.getBirthday());

        users.put(user.getId(), user);
        log.info("LOGGER Users: добавление данных SUCCESS {}", user);
        return user;
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}