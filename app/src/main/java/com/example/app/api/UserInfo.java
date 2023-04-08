package com.example.app.api;

import com.example.app.entity.User;
import com.example.app.jwt.JwtService;
import com.example.app.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserInfo {
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtService jwtService;

    /**
     *   Request "/localhost:8080/get-user-info" with token already authen at filter
     *   so no need any further authen in this api body
     */
    @GetMapping("/get-user-info")
    public ResponseEntity<List<User>> getAllUserInfo(HttpServletRequest request){
        return ResponseEntity.ok(userRepository.findAll());
    }
}
