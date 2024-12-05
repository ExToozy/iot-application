package ru.extoozy.iotapplication.service.sensor;

import ru.extoozy.iotapplication.store.model.SensorEntity;

public interface SensorService {
    SensorEntity create(SensorEntity entity);

    SensorEntity getByName(String name);
}
