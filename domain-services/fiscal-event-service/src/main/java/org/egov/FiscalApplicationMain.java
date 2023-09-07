package org.egov;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"org.egov", "org.egov.web.controllers", "org.egov.config"})
public class FiscalApplicationMain {


    public static void main(String[] args) throws Exception {
        SpringApplication.run(FiscalApplicationMain.class, args);
    }

}
 