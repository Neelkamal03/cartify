package com.example.demo.data;

import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Transactional
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Set<String> defaultRoles=Set.of("ROLE_ADMIN", "ROLE_USER");
        createDefaultUserIfNotExists();
        createDefaultRoleIfNotExists(defaultRoles);
        createDefaultAdminIfNotExists();
    }

    private void createDefaultUserIfNotExists() {
        Role userRole=roleRepository.findByName("ROLE_USER").get();
        for (int i = 1; i <= 5; i++) {
            String defaultEmail = "user" + i + "@email.com";
            if (userRepository.existsByEmail(defaultEmail)) {
                continue;
            }
            User user = new User();
            user.setFirstName("The User");
            user.setLastName("User" + i);
            user.setEmail(defaultEmail);
            user.setPassword(passwordEncoder.encode("123456"));
            user.setRoles(Set.of(userRole));
            userRepository.save(user);
        }
    }
    private void createDefaultAdminIfNotExists() {
        Role adminRole=roleRepository.findByName("ROLE_ADMIN").get();
        for (int i = 1; i <= 2; i++) {
            String defaultEmail = "admin" + i + "@email.com";
            if (userRepository.existsByEmail(defaultEmail)) {
                continue;
            }
            User user = new User();
            user.setFirstName("Admin");
            user.setLastName("Admin" + i);
            user.setEmail(defaultEmail);
            user.setPassword(passwordEncoder.encode("123456"));
            user.setRoles(Set.of(adminRole));
            userRepository.save(user);
        }
    }

    private void createDefaultRoleIfNotExists(Set<String> roles){
        roles.stream()
                .filter(role->roleRepository.findByName(role).isEmpty())
                .map(Role:: new).forEach(roleRepository::save);
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