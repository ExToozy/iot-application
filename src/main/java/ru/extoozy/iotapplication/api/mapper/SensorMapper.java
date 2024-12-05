package ru.extoozy.iotapplication.api.mapper;

import org.mapstruct.Mapper;
import ru.extoozy.iotapplication.api.dto.SensorDto;
import ru.extoozy.iotapplication.store.model.SensorEntity;

@Mapper(componentModel = "spring")
public interface SensorMapper extends Mappable<SensorEntity, SensorDto> {
}
