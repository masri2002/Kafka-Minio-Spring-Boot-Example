package com.minio.example.parsers;

import com.minio.example.dto.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class CsvParsers {
    public List<Person> parseCsv(String content) {
        List<Person> people = new ArrayList<>();

        String[] lines = content.trim().split("\n");

        for (String line : lines) {
            if (line.isBlank() || line.toLowerCase().startsWith("firstname")
                    || line.toLowerCase().startsWith("first_name")
                    || line.toLowerCase().startsWith("name")) {
                log.info("Skipping header/empty line: '{}'", line);
                continue;
            }

            try {
                Person person = parseLineToObject(line);
                people.add(person);
            } catch (Exception e) {
                log.warn("Could not parse line '{}': {}", line, e.getMessage());
            }
        }

        return people;
    }

    private Person parseLineToObject(String line) {
        String[] columns = line.trim().split(",");

        if (columns.length < 4) {
            throw new IllegalArgumentException(
                    "Expected 4 columns (firstName,lastName,age,country) but got " + columns.length
            );
        }

        String firstName = columns[0].trim();
        String lastName = columns[1].trim();
        int age = Integer.parseInt(columns[2].trim());
        String country = columns[3].trim();

        return new Person.PersonBulider()
                .firstName(firstName)
                .lastName(lastName)
                .age(age)
                .country(country)
                .build();
    }

    private Person parseLineToObjectWithPassportId(String line) {
        String[] columns = line.trim().split(",");

        if (columns.length < 4) {
            throw new IllegalArgumentException(
                    "Expected 4 columns (firstName,lastName,age,country) but got " + columns.length
            );
        }

        String firstName = columns[0].trim();
        String lastName = columns[1].trim();
        int age = Integer.parseInt(columns[2].trim());
        String country = columns[3].trim();
        String passportId = columns[4].trim();

        return new Person.PersonBulider()
                .firstName(firstName)
                .lastName(lastName)
                .age(age)
                .country(country)
                .passportId(passportId)
                .build();
    }


}
