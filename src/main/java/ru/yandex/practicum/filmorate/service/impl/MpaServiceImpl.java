package ru.yandex.practicum.filmorate.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.entity.film.enumerated.MPA;
import ru.yandex.practicum.filmorate.service.MpaService;
import ru.yandex.practicum.filmorate.storage.EnumStorage;
import ru.yandex.practicum.generated.model.dto.MPADTO;
import ru.yandex.practicum.generated.model.dto.MPADTO.NameEnum;

@Slf4j
@Service
@RequiredArgsConstructor
public class MpaServiceImpl implements MpaService {

    @Qualifier("mpaDao")
    private final EnumStorage<MPA> mpaStorage;

    @Override
    public List<MPADTO> getAll() {
        List<MPA> mpaList = mpaStorage.getAll();
        log.debug("Получен список рейтингов {}", mpaList);
        return mpaList.stream().map(it -> {
            MPADTO mpadto = new MPADTO();
            mpadto.setId((long) it.getId());
            mpadto.setName(NameEnum.fromValue(it.getValue()));
            return mpadto;

        }).toList();
    }

    @Override
    public MPADTO getMpaById(Long id) {
        MPA mpa = mpaStorage.getById(id);
        MPADTO mpadto = new MPADTO();
        mpadto.setId((long) mpa.getId());
        mpadto.name(NameEnum.fromValue(mpa.getValue()));
        return mpadto;
    }
}


