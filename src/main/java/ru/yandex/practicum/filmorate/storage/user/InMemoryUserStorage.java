package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private final LocalDate nowDateTime = LocalDate.now();

    @Override
    public Map<Long, User> findAll() {
        return users;
    }

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public Optional<User> delete(User user) {
        for (Map.Entry<Long, User> entry : users.entrySet()) {
            if (user.equals(entry.getValue())) {
                users.remove(entry.getKey());
            }
        }
        return Optional.ofNullable(user);
    }

    @Override
    public User create(User user) {
        //Валидация
        validate(user);
        if (user.getId() == null) {
            user.setId(getNextId());
        }
        user.setEmail(user.getEmail());
        user.setLogin(user.getLogin());
        user.setName(user.checkNameLogin());
        user.setBirthday(user.getBirthday());
        users.put(user.getId(), user);
        log.info("LOGGER Users: добавление данных SUCCESS {}", user);
        return user;
    }

    @Override
    public User update(User user) {
        //Валидация
        validate(user);
        if (users.containsKey(user.getId())) {
            User changeUser = users.get(user.getId());
            changeUser.setEmail(user.getEmail());
            changeUser.setLogin(user.getLogin());
            changeUser.setName(user.checkNameLogin());
            changeUser.setBirthday(user.getBirthday());
            log.info("LOGGER Users: изменение данных SUCCESS {}", user);
            return changeUser;
        } else {
            log.error("LOGGER Users: такого id не существует");
            throw new NotFoundException("Такого id user не существует");
        }
    }

    public List<User> getFriendsList(long userId) {
        Set friendsId = users.get(userId).getFriends();
        return users.entrySet().stream()
                 .filter(entry -> friendsId.contains(entry.getKey()))
                 .map(Map.Entry::getValue)
                 .collect(Collectors.toList());
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @Override
    public void validate(User user) {
        log.info("LOGGER Users: валидация начало процесса");
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            log.error("LOGGER Users: некорректный ввод email: либо пусто, либо нет @");
            throw new ValidationException("Некорректный ввод email: либо пусто, либо нет @");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.error("LOGGER Users: некорректный ввод логина: либо пусто, либо есть пробелы");
            throw new ValidationException("Некорректный ввод логина: либо пусто, либо есть пробелы");
        }
        if (user.getBirthday() == null || user.getBirthday().isAfter(nowDateTime)) {
            log.error("LOGGER Users: некорректная дата дня рождения!");
            throw new ValidationException("Некорректная дата дня рождения!");
        }
        log.info("LOGGER Users: валидация данных SUCCESS");
    }

    @Override
    public User validateId(long userId) {
        Optional<User> user = findById(userId);
        if (user.isEmpty()) {
            log.error("LOGGER Users: пользователь с id = {} не найден", userId);
            throw new NotFoundException("LOGGER Users: пользователь с id = {" + userId + "} не найден");
        }
        return user.get();
    }
}