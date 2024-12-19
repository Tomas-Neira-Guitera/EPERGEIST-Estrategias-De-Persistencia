package ar.edu.unq.epersgeist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@EnableMongoRepositories()
@SpringBootApplication
@EnableNeo4jRepositories()
public class EpersgeistApplication {

    public static void main(String[] args) {
        SpringApplication.run(EpersgeistApplication.class, args);
    }
}
