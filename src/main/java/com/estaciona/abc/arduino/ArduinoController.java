package com.estaciona.abc.arduino;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ArduinoController {

    private final ArduinoService arduinoService;

    @GetMapping(value = "/update-sensors", produces = MediaType.APPLICATION_JSON_VALUE)
    public String updateSensors() {
        return arduinoService.sensorList();
    }
}
