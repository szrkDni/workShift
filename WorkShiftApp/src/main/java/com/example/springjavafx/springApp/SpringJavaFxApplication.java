package com.example.springjavafx.springApp;

import com.example.springjavafx.springApp.model.Person;
import com.example.springjavafx.springApp.model.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringJavaFxApplication {
    public static void main(String[] args)
    {
        SpringApplication.run(SpringJavaFxApplication.class, "");
    }

    @Autowired
    PersonRepository personRepository;
    public void printAll() {
        personRepository.save(Person.builder().name("Teszt Elek").age(20).build());
        Iterable<Person> persons = personRepository.findAll();

        for (Person p : persons) {
            System.out.println("Person: " + p);
        }

    }

    public void addTestPerson(){

        personRepository.save(Person.builder().name("Veg Bela").age(40).build());
        System.out.println("Veg Bela hozzaadva! (Very good app)");
    }
}
