package com.estaciona.abc.arduino;

import com.fazecast.jSerialComm.SerialPort;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArduinoService {

    private static final String ARDUINO_DESCRIPTION = "CP2102N USB to UART Bridge Controller";

    private final ArduinoRepository arduinoRepository;

    @Transactional
    List<SerialPort> findConnections() {
        List<SerialPort> result = new ArrayList<>();
        for (SerialPort port : SerialPort.getCommPorts()) {
            if (port.getPortDescription().equals(ARDUINO_DESCRIPTION)) {
                result.add(port);
            }
        }
        return result;
    }

    @Transactional
    public void updateSensors(List<SerialPort> ports) {
        if (ports.isEmpty()) {
            throw new RuntimeException("No ports available!");
        }
        for (SerialPort port : ports) {
            updateSensor(port);
        }
    }

    @Transactional
    public void updateSensor(SerialPort port) {
        String description = port.getDescriptivePortName();
        String comPort = StringUtils.substringBetween(description,"(",")");
        log.info("Opening connection to: [{}]", description);
        port.openPort();
        port.setBaudRate(1200);
        port.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);

        try (InputStream input = port.getInputStream()) {
            Arduino arduino = new Arduino();
            arduino.setPort(comPort);
            arduino.setDescription(description);
            arduino.setOccupied(input.read() < 150);
            arduino.setTimestamp(LocalDateTime.now());

            if (arduinoRepository.findPorts().contains(comPort)) {
                arduinoRepository.updateOccupancy(arduino.getOccupied(), arduino.getPort(), arduino.getTimestamp());
            } else {
                arduinoRepository.save(arduino);
            }
        } catch (Exception e) {
            log.info(e.getMessage());
            throw new RuntimeException("Error reading: [%s]".formatted(description));
        } finally {
            log.info("Closing connection to: [{}]", port.getDescriptivePortName());
            port.closePort();
        }
    }

    @Transactional
    public String sensorList() {
        updateSensors(findConnections());
        List<Arduino> arduinoList = arduinoRepository.findAll();
        return new Gson().toJson(arduinoList);
    }
}
