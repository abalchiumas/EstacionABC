package com.estaciona.abc;

import com.estaciona.abc.arduino.ArduinoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GenericController {

    private final ArduinoService arduinoService;

    @GetMapping("/update-sensors")
    public void updateSensors() {
        arduinoService.updateData(ArduinoService.findConnections());
    }
}
