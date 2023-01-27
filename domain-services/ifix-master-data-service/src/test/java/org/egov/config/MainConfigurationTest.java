//package org.egov.config;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertSame;
//
//class MainConfigurationTest {
//    @Test
//    void testJacksonConverter() {
//        MainConfiguration mainConfiguration = new MainConfiguration();
//        ObjectMapper objectMapper = new ObjectMapper();
//        MappingJackson2HttpMessageConverter actualJacksonConverterResult = mainConfiguration
//                .jacksonConverter(objectMapper);
//
//        assertEquals(2, actualJacksonConverterResult.getSupportedMediaTypes().size());
//        assertSame(objectMapper, actualJacksonConverterResult.getObjectMapper());
//    }
//}
//
