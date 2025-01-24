package com.example.demo.service.user;

import com.example.demo.dto.UserDto;
import com.example.demo.exception.AlreadyExistsExecption;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.request.CreateUserRequest;
import com.example.demo.request.UpdateUserRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
    }

    @Override
    public User createUser(CreateUserRequest request) {
        boolean user = userRepository.existsByEmail(request.getEmail());
        User newUser = new User();
        if(user) {
            throw new AlreadyExistsExecption("Oops! " + request.getEmail() + "User with this email already exists");
        }
        newUser.setFirstName(request.getFirstName());
        newUser.setLastName(request.getLastName());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(new User());
        return newUser;
//        return Optional.of(request)
//                .filter(user -> !userRepository.existsByEmail(request.getEmail()))
//                .map(req -> {
//                    User user = new User();
//                    user.setFirstName(request.getFirstName());
//                    user.setLastName(request.getLastName());
//                    user.setEmail(request.getEmail());
//                    user.setPassword(request.getPassword());
//                    return userRepository.save(user);
//                }).orElseThrow(()->new AlreadyExistsException("User already exists"));
    }

    @Override
    public User updateUser(UpdateUserRequest request, Long userId) {
        return userRepository.findById(userId)
                .map(existingUser -> {
                    existingUser.setFirstName(request.getFirstName());
                    existingUser.setLastName(request.getLastName());
                    return userRepository.save(existingUser);
                }).orElseThrow(() -> new ResourceNotFoundException("User not found!"));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId).
                ifPresentOrElse(userRepository::delete, () -> {
                    throw new ResourceNotFoundException("User not found");
                });
    }

    @Override
    public UserDto convertUserToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String email=authentication.getName();
        return userRepository.findByEmail(email);
    }
}
