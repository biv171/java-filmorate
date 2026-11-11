package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public Collection<User> findAll() {
        return userStorage.findAll().values();
    }

    public Optional<User> findById(Long id) {
        return userStorage.findById(id);
    }

    public Collection<User> getFriendsList(long userId) {
        return userStorage.getFriendsList(userId);
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public Collection<User> getCommonFriends(long firstUserId, long secondUserId) {
        //Валидация идентификатора пользователя
        User firstUser = userStorage.validateId(firstUserId);
        User secondUser = userStorage.validateId(secondUserId);

        Set<Long> firstUserFriends = firstUser.getFriends();
        Set<Long> secondUserFriends = secondUser.getFriends();

        Set<Long> commonFriendsId = firstUserFriends.stream()
                .filter(secondUserFriends::contains)
                .collect(Collectors.toSet());

        return userStorage.findAll().entrySet().stream()
                .filter(entry -> commonFriendsId.contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    public User addFriend(long firstUserId, long secondUserId) {
        //Валидация идентификатора пользователя
        User firstUser = userStorage.validateId(firstUserId);
        User secondUser = userStorage.validateId(secondUserId);

        firstUser.getFriends().add(secondUserId);
        secondUser.getFriends().add(firstUserId);
        log.info("LOGGER Users: Пользователи {} и {} друзья!", firstUser.getName(), secondUser.getName());
        return firstUser;
    }

    public User deleteFriend(long firstUserId, long secondUserId) {
        //Валидация идентификатора пользователя
        User firstUser = userStorage.validateId(firstUserId);
        User secondUser = userStorage.validateId(secondUserId);

        firstUser.getFriends().remove(secondUserId);
        secondUser.getFriends().remove(firstUserId);
        return firstUser;
    }
}