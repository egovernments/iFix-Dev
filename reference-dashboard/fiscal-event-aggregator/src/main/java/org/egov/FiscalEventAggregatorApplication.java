package org.egov;

import lombok.Data;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Data
public class FiscalEventAggregatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(FiscalEventAggregatorApplication.class, args);
    }

}
