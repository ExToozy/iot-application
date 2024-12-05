package ru.extoozy.iotapplication.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.extoozy.iotapplication.store.model.SensorEntity;

import java.util.Optional;

public interface SensorRepository extends JpaRepository<SensorEntity, Long> {
    Boolean existsByName(String name);

    Optional<SensorEntity> findByName(String name);
}
