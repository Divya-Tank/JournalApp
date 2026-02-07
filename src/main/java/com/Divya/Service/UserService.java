package com.Divya.Service;

import com.Divya.Config.SecurityConfig;
import com.Divya.Entity.User;
import com.Divya.Repository.UserRepository;
import com.Divya.Repository.UserRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Component
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRepositoryImpl userRepositoryimpl;

    @Autowired
    private SecurityConfig securityConfig;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void saveUser(User user){
        userRepository.save(user);
    }

    public void saveNewUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("User"));
        userRepository.save(user);
    }

    public void saveNewAdminUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("User","Admin"));
        userRepository.save(user);
    }

    public User getEntry(String userName){
        return userRepository.findByUserName(userName);
    }

    public List<User> getAll(){
       return userRepository.findAll();
    }

    public List<User> userforSA(){
        List<User> userforSA = userRepositoryimpl.getUserforSA();
        return userforSA;
    }




}
