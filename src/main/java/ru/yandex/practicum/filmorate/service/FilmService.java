package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Optional<Film> findById(long filmId) {
        return filmStorage.findById(filmId);
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public Collection<Film> displayTop10PopularFilms(int count) {
        return filmStorage.findAll().stream().sorted(Comparator.reverseOrder()).limit(count).toList();
    }

    public Film addFilmSmile(long filmId, long userId) {
        //Валидация идентификатора фильма
        Film film = filmStorage.validateId(filmId);
        User user = userStorage.validateId(userId);
        film.getSmiles().add(userId);
        log.info("LOGGER Films: Фильму {} поставлен лайк от пользователя {}!", film.getName(), user.getName());
        return film;
    }

    public void deleteFilmSmile(long filmId, long userId) {
        //Валидация идентификатора фильма
        Film film = filmStorage.validateId(filmId);
        User user = userStorage.validateId(userId);
        film.getSmiles().remove(userId);
        log.info("LOGGER Films: Фильму {} удален лайк  от пользователя {}!", film.getName(), user.getName());
    }
}