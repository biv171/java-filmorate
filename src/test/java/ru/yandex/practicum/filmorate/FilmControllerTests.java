package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import java.time.LocalDate;


@SpringBootTest
class FilmControllerTests {
	Film film = new Film();
	FilmController filmController = new FilmController();

	@Test
	void mustBeExceptionWithEmptyFilmName() {
		try {
			filmController.update(film);
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
			filmController.update(film);
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
			filmController.update(film);
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
			filmController.update(film);
		} catch (ValidationException e) {
			Assertions.assertEquals("Продолжительность фильма должна быть положительным числом!",
					e.getMessage());
		}
	}

	@Test
	void mustBeSuccessWithCorrectDataField() {
		film.setName("Poklonka");
		film.setDuration(100);
		film.setDescription("Описание фильма про судьбу");
		film.setReleaseDate(LocalDate.of(2020,10,10));
		filmController.update(film);

		Assertions.assertEquals("Poklonka", film.getName());
		Assertions.assertEquals(100, film.getDuration());
		Assertions.assertEquals("Описание фильма про судьбу", film.getDescription());
		Assertions.assertEquals(LocalDate.of(2020,10,10), film.getReleaseDate());
	}
}