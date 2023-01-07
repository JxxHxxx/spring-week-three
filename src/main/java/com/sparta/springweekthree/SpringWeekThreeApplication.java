package com.sparta.springweekthree;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SpringWeekThreeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringWeekThreeApplication.class, args);
    }

}
