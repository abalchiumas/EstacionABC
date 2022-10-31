package com.estaciona.abc.arduino;

import com.fazecast.jSerialComm.SerialPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArduinoService {

    private static final String ARDUINO_DESCRIPTION = "CP2102N USB to UART Bridge Controller";

    private final SensorRepository sensorRepository;

    public static List<SerialPort> findConnections() {
        List<SerialPort> result = new ArrayList<>();
        for (SerialPort port : SerialPort.getCommPorts()) {
            if (port.getPortDescription().equals(ARDUINO_DESCRIPTION)) {
                result.add(port);
            }
        }
        return result;
    }

    public void updateData(List<SerialPort> ports) {
        if (ports.isEmpty()) {
            throw new RuntimeException("No ports available!");
        }
        for (SerialPort arduino : ports) {
            String description = arduino.getDescriptivePortName();
            log.info("Opening connection to: [{}]", description);
            arduino.openPort();
            arduino.setBaudRate(1200);
            arduino.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);

            Sensor sensor = new Sensor();
            try (InputStream input = arduino.getInputStream()) {
                sensor.setDescription(description);
                sensor.setOccupied(input.read() < 150);
                sensor.setTimestamp(LocalDateTime.now());
                sensorRepository.save(sensor);
//                TODO: fix update
//                if (sensorRepository.findByDescription(description)) {
//                    sensorRepository.updateOccupancy(sensor.getOccupied(), sensor.getDescription(), sensor.getTimestamp());
//                } else {
//                    sensorRepository.save(sensor);
//                }
            } catch (Exception e) {
                throw new RuntimeException("Error reading: [%s]".formatted(description));
            }

            log.info("Closing connection to: [{}]", arduino.getDescriptivePortName());
            arduino.closePort();
        }
    }
}
