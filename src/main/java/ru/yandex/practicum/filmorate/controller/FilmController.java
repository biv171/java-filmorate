package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import lombok.extern.slf4j.Slf4j;
import java.util.Collection;
import java.util.HashMap;
import java.time.LocalDate;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();
    private final LocalDate minDateTime = LocalDate.of(1895, 12, 28);

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @RequestMapping(method = {RequestMethod.POST, RequestMethod.PUT})
    public Film update(@RequestBody Film film) {
        log.info("LOGGER Films: валидация начало процесса");
        if (film.getName() == null || film.getName() == "") {
            log.error("LOGGER Films: имя фильма должно быть указано!!");
            throw new ValidationException("Имя фильма должно быть указано!");
        }
        if (film.getDescription().length() > 200) {
            log.error("LOGGER Films: длина описания не более 200 символов!!");
            throw new ValidationException("Длина описания не более 200 символов!");
        }
        LocalDate dt = film.getReleaseDate();
        if (film.getReleaseDate().isBefore(minDateTime)) {
            log.error("LOGGER Films: дата релиза должна быть не раньше 28.12.1895!");
            throw new ValidationException("Дата релиза должна быть не раньше 28.12.1895!");
        }
        if (film.getDuration() < 0) {
            log.error("LOGGER Films: продолжительность фильма должна быть положительным числом!");
            throw new ValidationException("Продолжительность фильма должна быть положительным числом!");
        }
        log.info("LOGGER Films: валидация данных SUCCESS");

        if (films.containsKey(film.getId())) {
            Film changeFilm = films.get(film.getId());
            changeFilm.setName(film.getName());
            changeFilm.setDescription(film.getDescription());
            changeFilm.setReleaseDate(film.getReleaseDate());
            changeFilm.setDuration(film.getDuration());
            log.info("INFO Films: изменение данных SUCCESS {}", film);
            return changeFilm;
        }

        film.setId(getNextId());
        film.setName(film.getName());
        film.setDescription(film.getDescription());
        film.setReleaseDate(film.getReleaseDate());
        film.setDuration(film.getDuration());

        films.put(film.getId(), film);
        log.info("INFO Films: добавление данных SUCCESS {}", film);
        return film;
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}