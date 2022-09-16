package org.bat2.vacationworker;

import lombok.extern.slf4j.Slf4j;
import org.bat2.vacationworker.service.TrelloService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootApplication
public class VacationWorkerApplication {

    public static void main(String[] args) {
        SpringApplication.run(VacationWorkerApplication.class, args);
    }

    @Bean
    CommandLineRunner init(TrelloService trelloService) {
        log.info("This is init method");
        return (args) -> {
            final String boards = trelloService.getBoards();
        };
    }
}
