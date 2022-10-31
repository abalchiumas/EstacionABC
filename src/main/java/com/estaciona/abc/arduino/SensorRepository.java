package com.estaciona.abc.arduino;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface SensorRepository extends JpaRepository<Sensor,Integer> {

//    TODO: fix update
    @Query("update Sensor s set s.occupied = :occupied, s.timestamp = :timestamp where s.description = :description")
    void updateOccupancy(@Param("occupied") Boolean occupied, @Param("description") String description, @Param("timestamp")LocalDate timestamp);

    boolean findByDescription(String description);
}