package com.example.demo.request;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private String firstName;
    private String lastName;
    //By update, we mean update firstName, LastName etc.
    //We should restrict user from updating user email and password directly.
    //For email and password we cannot directly update email and password it has to be a separate request.
}
