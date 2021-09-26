package fr.volkaert.dependency_analyser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class DependencyAnalyserApplication {

    public static void main(String[] args) {
        SpringApplication.run(DependencyAnalyserApplication.class, args);
    }
}
