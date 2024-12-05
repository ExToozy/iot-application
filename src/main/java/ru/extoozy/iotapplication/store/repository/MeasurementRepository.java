package ru.extoozy.iotapplication.store.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.extoozy.iotapplication.store.model.MeasurementEntity;

public interface MeasurementRepository extends JpaRepository<MeasurementEntity, Long> {

    Long countByRainingTrue();
}
