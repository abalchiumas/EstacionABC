package com.estaciona.abc.arduino;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "SENSOR_DATA", schema = "ESTACIONA_ABC")
@EqualsAndHashCode(exclude = {"id", "timestamp"})
@NoArgsConstructor
@AllArgsConstructor
public class Sensor implements Serializable {

    @Id
    @SequenceGenerator(name = "id_sequence", sequenceName = "id_sequence", allocationSize = 1)
    @GeneratedValue(generator = "id_sequence", strategy = GenerationType.SEQUENCE)
    private Integer id;

    private String description;

    private Boolean occupied;

    private LocalDateTime timestamp;
}