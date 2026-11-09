package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import java.util.Collection;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public Collection<Film> findAll() { return filmService.findAll(); }

    @GetMapping("/{id}")
    public Optional<Film> findFilm(@PathVariable Long id) {
        Optional<Film> film = filmService.findById(id);
        if (film.isEmpty())
            throw new NotFoundException("Такой id не найден");
        return film;
    }

    @GetMapping("/popular")
    public Collection<Film> displayTop10Films(@RequestParam(defaultValue = "10") Integer count) {
        return filmService.displayTop10PopularFilms(count);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film create(@RequestBody Film film) { return filmService.create(film); }

    @PutMapping
    public Film update(@RequestBody Film film) {
        return filmService.update(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addSmile(@PathVariable Long id,
                         @PathVariable Long userId) {
        return filmService.addFilmSmile(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteSmile(@PathVariable long id, @PathVariable long userId) {
        filmService.deleteFilmSmile(id, userId);
    }
}