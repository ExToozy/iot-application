package ru.extoozy.iotapplication.api.mapper;

import org.mapstruct.Mapper;
import ru.extoozy.iotapplication.api.dto.MeasurementDto;
import ru.extoozy.iotapplication.store.model.MeasurementEntity;

@Mapper(componentModel = "spring")
public interface MeasurementMapper extends Mappable<MeasurementEntity, MeasurementDto> {
}
