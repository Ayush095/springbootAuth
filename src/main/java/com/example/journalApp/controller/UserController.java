package com.example.journalApp.controller;

import com.example.journalApp.entity.User;
import com.example.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping()
    public List<User> getAll(){
        return userService.getAll();
    }

    @PostMapping()
    public void createUser(@RequestBody User user) {
        userService.saveEntry(user);
    }

    @PutMapping()
    public ResponseEntity<?> updateUser(@RequestBody User user, @PathVariable String username){
        User userInfo = userService.findByUsername(username);
        if(userInfo!=null){
            userInfo.setUsername(user.getUsername());
            userInfo.setPassword(user.getPassword());
            userService.saveEntry(userInfo);
        }
        return new ResponseEntity<>(userInfo, HttpStatus.OK);
    }

}
