package com.example.app.api;

import com.example.app.dto.LoginRequest;
import com.example.app.dto.LoginResponse;
import com.example.app.dto.RegisterRequest;
import com.example.app.entity.User;
import com.example.app.jwt.JwtService;
import com.example.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth/v1")
public class Authen {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtService jwtService;

    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
        Optional<User> isUser = userRepository.findByUsername(registerRequest.getUsername());
//                .orElseThrow(() -> new UsernameNotFoundException("User Not Exist"));
        User user = new User().builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .email(registerRequest.getEmail())
                .phone(registerRequest.getPhone())
                .build();
        if (!isUser.isPresent()) {
            userRepository.save(user);
            return ResponseEntity.ok().body("Regist Account Success");
        }
        return ResponseEntity.ok().body("User Exist, can not regist");
    }

    @GetMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        Optional<User> user = userRepository.findByUsername(loginRequest.getUsername());
        LoginResponse loginResponse = new LoginResponse();
        if(user.isPresent() && user.get().getUsername().equals(loginRequest.getUsername())
                && passwordEncoder.matches(loginRequest.getPassword(), user.get().getPassword())){
            String jwt = jwtService.generateToken(user.get().getUsername());
            loginResponse.setJwt(jwt);
            loginResponse.setStatus("Login Successfully");
            return ResponseEntity.ok(loginResponse);
        }
        loginResponse.setStatus("User Not  Found");
        return ResponseEntity.ok(loginResponse);
    }
    @GetMapping("/logout")
    public ResponseEntity<String> logout() {
        return null;
    }


}
