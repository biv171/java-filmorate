package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import java.time.LocalDate;


@SpringBootTest
class FilmControllerTests {
	Film film = new Film();
	InMemoryFilmStorage inMemoryFilmStorage = new InMemoryFilmStorage();
	InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
	FilmService filmService = new FilmService(inMemoryFilmStorage, inMemoryUserStorage);
	FilmController filmController = new FilmController(filmService);

	@Test
	void mustBeExceptionWithEmptyFilmName() {
		try {
			filmController.create(film);
		} catch (ValidationException e) {
			Assertions.assertEquals("Имя фильма должно быть указано!", e.getMessage());
		}
	}

	@Test
	void mustBeExceptionWithLengthDescription201() {
		//Иммитируем описание в 201 символ
		StringBuilder string201 = new StringBuilder();
		for (int i = 0; i < 41; i++) {
			string201.append("ABCDE");
		}

		film.setName("Marko Polo");
		film.setDescription(string201.toString());

		try {
			filmController.create(film);
		} catch (ValidationException e) {
			Assertions.assertEquals("Длина описания не более 200 символов!", e.getMessage());
		}
	}

	@Test
	void mustBeExceptionWithDateRealeseBefore1895() {
		film.setName("Marko Polo");
		film.setDescription("Описание");
		film.setReleaseDate(LocalDate.of(1895, 12, 27));

		try {
			filmController.create(film);
		} catch (ValidationException e) {
			Assertions.assertEquals("Дата релиза должна быть не раньше 28.12.1895!", e.getMessage());
		}
	}

	@Test
	void mustBeExceptionWithMinusValueDuration() {
		film.setName("Marko Polo");
		film.setReleaseDate(LocalDate.of(1895, 12, 28));
		film.setDuration(-100);

		try {
			filmController.create(film);
		} catch (ValidationException e) {
			Assertions.assertEquals("Продолжительность фильма должна быть положительным числом!",
					e.getMessage());
		}
	}

	@Test
	void withZeroDurationMustBeException() {
		film.setName("Marko Polo");
		film.setReleaseDate(LocalDate.of(1895, 12, 28));
		film.setDuration(0);

		try {
			filmController.create(film);
		} catch (ValidationException e) {
			Assertions.assertEquals("Продолжительность фильма должна быть положительным числом!",
					e.getMessage());
		}
	}

	@Test
	void mustBeSuccessWithCreateDataField() {
		film.setName("Poklonka");
		film.setDuration(100);
		film.setDescription("Описание фильма про судьбу");
		film.setReleaseDate(LocalDate.of(2020,10,10));
		filmController.create(film);

		Assertions.assertEquals("Poklonka", film.getName());
		Assertions.assertEquals(100, film.getDuration());
		Assertions.assertEquals("Описание фильма про судьбу", film.getDescription());
		Assertions.assertEquals(LocalDate.of(2020,10,10), film.getReleaseDate());
	}

	@Test
	void mustBeSuccessWithChangetDataField() {
		film.setName("Poklonka");
		film.setDuration(100);
		film.setDescription("Описание фильма про судьбу");
		film.setReleaseDate(LocalDate.of(2020,10,10));

		filmController.create(film);

		film.setName("Майор Пейн");
		film.setDuration(150);
		film.setDescription("Описание фильма про маленький паровозик!");
		film.setReleaseDate(LocalDate.of(1999,5,16));

		filmController.update(film);

		Assertions.assertEquals(1, film.getId());
		Assertions.assertEquals("Майор Пейн", film.getName());
		Assertions.assertEquals(150, film.getDuration());
		Assertions.assertEquals("Описание фильма про маленький паровозик!", film.getDescription());
		Assertions.assertEquals(LocalDate.of(1999,5,16), film.getReleaseDate());
	}
}