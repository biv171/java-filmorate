package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    Long id;
    String email = "";
    String login = "";
    String name = "";
    LocalDate birthday = LocalDate.of(1000,10,10);
    Set<Long> friends = new HashSet<>();

    public String checkNameLogin() {
        if (getName() == null || getName().isBlank())
            return getLogin();
        return getName();
    }
}