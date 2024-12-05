package ru.extoozy.iotapplication.api.mapper;

import java.util.List;

public interface Mappable<E, D> {
    E toEntity(D dto);

    List<D> toDto(List<E> entity);

    D toDto(E entity);

}
