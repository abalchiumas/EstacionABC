package com.estaciona.abc.arduino;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

public interface ArduinoRepository extends JpaRepository<Arduino,Integer> {

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update Arduino s set s.occupied = :occupied, s.timestamp = :timestamp where s.port = :port")
    void updateOccupancy(@Param("occupied") Boolean occupied, @Param("port") String port, @Param("timestamp") LocalDateTime timestamp);

    @Query("select s.port from Arduino s")
    List<String> findPorts();
}