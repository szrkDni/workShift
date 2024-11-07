package com.project.springjavafx.springApp;

import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
class SpringJavaFxApplicationTests {
    @BeforeAll
    static void before(){
        System.out.println("before");
    }

    @AfterAll
    static void after(){
        System.out.println("After");
    }

    @Test
    void contextLoads() {
        System.out.println("Alma");
    }

}
