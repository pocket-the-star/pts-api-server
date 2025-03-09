package com.pts.api.common.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pts.api.common.config.AutoMockServiceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
@Import(AutoMockServiceConfig.class)
public class BaseIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;
    protected ObjectMapper objectMapper = new ObjectMapper();
}
