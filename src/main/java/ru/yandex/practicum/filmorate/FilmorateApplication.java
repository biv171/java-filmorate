package ru.yandex.practicum.filmorate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "ru.yandex.practicum.filmorate.controller")
public class FilmorateApplication {
	private static final Logger log = LoggerFactory.getLogger(FilmorateApplication.class);

	public static void main(String[] args) {
		((ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME)).setLevel(Level.INFO);
		SpringApplication.run(FilmorateApplication.class, args);
		log.info("LOGGER: program starting FilmorateApplication! 0_0 xxx-yyy-zzz ");
	}
}