package ru.extoozy.iotapplication.service.measurement;

import ru.extoozy.iotapplication.store.model.MeasurementEntity;

import java.util.List;

public interface MeasurementService {
    MeasurementEntity create(MeasurementEntity entity);

    List<MeasurementEntity> getAll();

    Long getRainyDaysCount();
}
