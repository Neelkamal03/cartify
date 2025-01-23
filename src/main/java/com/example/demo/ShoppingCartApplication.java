package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ShoppingCartApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShoppingCartApplication.class, args);
    }
    //Maven helps you identify any issue if their before running your main application , so before running application
    //If there are compilation errors, dependency issues, or test failures, Maven will stop the build and show the errors.
    //Saves time by catching problems before runtime.
    //like a demo run to catch any problem before-hand "**maven clean install**"
}
