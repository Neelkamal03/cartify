package com.example.demo.data;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {
    private final UserRepository userRepository;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        createDefaultUserIfNotExists();
    }

    private void createDefaultUserIfNotExists() {
        for (int i = 1; i <= 5; i++) {
            String defaultEmail = "user" + i + "gmail.com";
            if (userRepository.existsByEmail(defaultEmail)) {
                continue;
            }
            User user = new User();
            user.setFirstName("The User");
            user.setLastName("User" + i);
            user.setEmail(defaultEmail);
            user.setPassword("12345");
            userRepository.save(user);
        }
    }
}
//@Component
//Marks this class as a Spring-managed bean.
// Spring automatically detects and registers it as part of the application context.
//ApplicationListener<ApplicationReadyEvent>
//Listens for the ApplicationReadyEvent, which is fired after:
//The application context is completely loaded.
//All beans are created and initialized.
//Any command-line runners or application runners have been executed.
//Data Initialization: Useful for inserting default records like admin users, roles, or settings when the application starts for the first time.