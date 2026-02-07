package com.Divya.Controller;

import com.Divya.Entity.User;
import com.Divya.Response.WeatherResponse;
import com.Divya.Service.RedisService;
import com.Divya.Service.UserService;
import com.Divya.Service.WeatherAPIservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private WeatherAPIservice weatherAPIservice;

    @GetMapping
    private ResponseEntity<User> findbyuser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User entry = userService.getEntry(userName);
        if(entry!=null){
            return new ResponseEntity<>(entry, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping
    private ResponseEntity<?> updateById(@RequestBody User newuser) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User existinguser = userService.getEntry(userName);
            existinguser.setUserName(newuser.getUserName() != null && !newuser.getUserName().equals("") ? newuser.getUserName() : existinguser.getUserName());
            existinguser.setPassword(newuser.getPassword() != null && !newuser.getPassword().equals("") ? newuser.getPassword() : existinguser.getPassword());
            userService.saveNewUser(existinguser);


        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/Greetings")
    private ResponseEntity<?> Greetings(){
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        String greetings = "";
        WeatherResponse Response = weatherAPIservice.getWeather("Mumbai");
        if(Response !=null) {
            greetings = ", Weather feels like " + Response.getCurrent().getFeelslike();
        }
        return new ResponseEntity<>("Hello " + userName + greetings, HttpStatus.OK);
    }



}
