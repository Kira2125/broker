package com.example.simplebroker;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URL;
import java.util.Map;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = Application.class
)
public abstract class AbstractSpringBootTest {

    protected final String DEVICE_NAME = "Lenovo";
    protected final Map<String, String> HEADERS = Map.of("X-DEVICE", DEVICE_NAME);

    @Autowired
    protected ObjectMapper objectMapper;

    public URL getPath(String path) {
        return this.getClass().getClassLoader().getResource(path);
    }
}
