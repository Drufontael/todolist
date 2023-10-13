package com.drufontael.todolist.user;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepository repository;
    @PostMapping("/")
    public ResponseEntity create(@RequestBody UserModel userModel){
        var user=repository.findByUsername(userModel.getUsername());
        if(user!=null){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Usuario já existe!");
        }
        var passwordHashed= BCrypt.withDefaults().hashToString(12,userModel.getPassword().toCharArray());
        userModel.setPassword(passwordHashed);
        var userCreated=repository.save(userModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
    }
}
