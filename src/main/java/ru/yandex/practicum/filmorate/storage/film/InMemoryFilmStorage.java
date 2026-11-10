package ru.yandex.practicum.filmorate.storage.film;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private final LocalDate minDateTime = LocalDate.of(1895, 12, 28);

    @Override
    public Collection<Film> findAll() {
        return films.values();
    }

    @Override
    public Optional<Film> findById(long filmId) {
        return Optional.ofNullable(films.get(filmId));
    }

    @Override
    public Film delete(Film film) {
        for (Map.Entry<Long, Film> entry : films.entrySet()) {
            if (film.equals(entry.getValue())) {
                films.remove(entry.getKey());
            }
        }
        return film;
    }

    @Override
    public Film create(Film film) {
        // Валидация
        validate(film);
        if (film.getId() == null) {
            film.setId(getNextId());
        }
        film.setName(film.getName());
        film.setDescription(film.getDescription());
        film.setReleaseDate(film.getReleaseDate());
        film.setDuration(film.getDuration());
        films.put(film.getId(), film);
        log.info("INFO Films: добавление данных SUCCESS {}", film);
        return film;
    }

    @Override
    public Film update(Film film) {
        // Валидация
        validate(film);
        if (films.containsKey(film.getId())) {
            Film changeFilm = films.get(film.getId());
            changeFilm.setName(film.getName());
            changeFilm.setDescription(film.getDescription());
            changeFilm.setReleaseDate(film.getReleaseDate());
            changeFilm.setDuration(film.getDuration());
            log.info("INFO Films: изменение данных SUCCESS {}", film);
            return changeFilm;
        } else {
            log.error("LOGGER Films: такого id не существует");
            throw new NotFoundException("Такого id film не существует");
        }
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @Override
    public void validate(Film film) {
        log.info("LOGGER Films: валидация начало процесса");
        if (film.getName() == null || film.getName().isEmpty()) {
            log.error("LOGGER Films: имя фильма должно быть указано!!");
            throw new ValidationException("Имя фильма должно быть указано!");
        }
        if (film.getDescription() == null || film.getDescription().length() > 200) {
            log.error("LOGGER Films: длина описания не более 200 символов!!");
            throw new ValidationException("Длина описания не более 200 символов!");
        }
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(minDateTime)) {
            log.error("LOGGER Films: дата релиза должна быть не раньше 28.12.1895!");
            throw new ValidationException("Дата релиза должна быть не раньше 28.12.1895!");
        }
        if (film.getDuration() == null || film.getDuration() <= 0) {
            log.error("LOGGER Films: продолжительность фильма должна быть положительным числом!");
            throw new ValidationException("Продолжительность фильма должна быть положительным числом!");
        }
        log.info("LOGGER Films: валидация данных SUCCESS");
    }

    @Override
    public Film validateId(long filmId) {
        Optional<Film> film = findById(filmId);
        if (film.isEmpty()) {
            log.error("LOGGER Films: фильм с id = {} не найден", filmId);
            throw new ValidationException("LOGGER Users: фильм с id = {" + filmId +"} не найден");
        }
        return film.get();
    }
}