package com.minio.example.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Person {
    private String firstName;
    private String lastName;
    private int age;
    private String country;
    private String passportId;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getAge() {
        return age;
    }

    public String getCountry() {
        return country;
    }

    public String getPassportId() {
        return passportId;
    }

    public static class PersonBulider {
        private String firstName;
        private String lastName;
        private int age;
        private String country;
        private String passportId;


        public PersonBulider firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public PersonBulider lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public PersonBulider age(int age) {
            this.age = age;
            return this;
        }

        public PersonBulider country(String country) {
            this.country = country;
            return this;
        }

        public PersonBulider passportId(String passportId) {
            this.passportId = passportId;
            return this;
        }

        public Person build() {
            return new Person(firstName, lastName, age, country, passportId);
        }
    }
}