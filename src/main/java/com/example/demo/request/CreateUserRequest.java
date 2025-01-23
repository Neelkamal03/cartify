package com.example.demo.request;

import lombok.Data;
import org.hibernate.annotations.NaturalId;

@Data
public class CreateUserRequest {
    private String firstName;
    private String lastName;
    @NaturalId
    private String email;
    private String password;
    //At the time for user creation we are not gonna have Cart and List<Order> of user.
}
