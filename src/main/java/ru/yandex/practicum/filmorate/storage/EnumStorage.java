package ru.yandex.practicum.filmorate.storage;

import java.util.List;

public interface EnumStorage<T> {

    T getEnumByEntityId(Long id);

    List<T> getAll();

    T getById(Long id);
}
