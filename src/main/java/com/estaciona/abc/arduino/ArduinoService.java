package com.estaciona.abc.arduino;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fazecast.jSerialComm.SerialPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ArduinoService {

    // TODO: there must be a better way to do this
    private static final String ARDUINO_DESCRIPTION = "CP2102N USB to UART Bridge Controller";

    private List<SerialPort> findConnections() {
        List<SerialPort> result = new ArrayList<>();
        for (SerialPort port : SerialPort.getCommPorts()) {
            if (port.getPortDescription().equals(ARDUINO_DESCRIPTION)) {
                result.add(port);
            }
        }
        return result;
    }

    public Map<Integer, Object> iterateThroughPorts(List<SerialPort> ports) {
        if (ports.isEmpty()) {
            throw new RuntimeException("No ports available!");
        }

        Map<Integer,Object> result = new HashMap<>();

        int index = 0;
        for (SerialPort port : ports) {
            result.put(index, readPort(port));
            ++index;
        }

        return result;
    }

    public JsonNode readPort(SerialPort port) {
        String description = port.getDescriptivePortName();
        log.info("Opening connection to: [{}]", description);
        port.openPort();
        port.setBaudRate(1200);
        port.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);

        try (InputStream input = port.getInputStream()) {

            String firstChar = "";
            String secondChar = "";
            StringBuilder message = new StringBuilder();
            while (true) {
                String thirdChar = String.valueOf((char) input.read());
                message.append(thirdChar);
                if (firstChar.equals("}") && secondChar.equals("]") && thirdChar.equals("}")) {
                    break;
                }
                firstChar = secondChar;
                secondChar = thirdChar;
            }
            log.info("Read: {}", message);

            ObjectMapper mapper = new ObjectMapper();
            JsonFactory factory = mapper.getFactory();
            JsonParser parser = factory.createParser(String.valueOf(message));

            return mapper.readTree(parser);
        } catch(Exception e){
            log.info(e.getMessage());
            throw new RuntimeException("Error reading: [%s]".formatted(description));
        } finally {
            log.info("Closing connection to: [{}]", description);
            port.closePort();
        }
    }

    public String createJson() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(iterateThroughPorts(findConnections()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
