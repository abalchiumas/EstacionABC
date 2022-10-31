package com.estaciona.abc.arduino;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@Slf4j
@ExtendWith(MockitoExtension.class)
class ArduinoServiceTest implements WithAssertions {

    @InjectMocks
    ArduinoService arduinoService;

    @Test
    void testFindConnections() {
    }
}