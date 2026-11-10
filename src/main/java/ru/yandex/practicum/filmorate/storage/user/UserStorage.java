package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public interface UserStorage {
    Map<Long, User> findAll();
    Optional<User> findById(Long id);
    Collection<User> getFriendsList(long userId);
    User create(User user);
    User update(User user);
    Optional<User> delete(User user);
    void validate(User user);
    User validateId(long userId);
}